package cn.cnic.component.flow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.mxGraph.utils.MxGraphUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import cn.cnic.component.flow.entity.CustomizedProperty;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.process.utils.ProcessPathUtils;
import cn.cnic.component.process.utils.ProcessStopUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.controller.requestVo.RunStopsVo;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;


@Service
public class StopsServiceImpl implements IStopsService {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private FlowDomain flowDomain;
    @Autowired
    private DataSourceDomain dataSourceDomain;
    @Autowired
    private TestDataDomain testDataDomain;
    @Autowired
    private StopsComponentDomain stopsComponentDomain;
    @Autowired
    private ProcessDomain processDomain;
    @Autowired
    private IFlow flowImpl;

    /**
     * Modify the isCheckpoint field
     *
     * @param stopId
     * @param isCheckpointStr
     * @return
     * @throws Exception
     */
    @Override
    public String updateStopsCheckpoint(String username, String stopId, String isCheckpointStr) throws Exception {
        if (StringUtils.isAnyEmpty(stopId, isCheckpointStr)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Partial incoming parameters are empty");
        }
        Stops stopsById = flowDomain.getStopsById(stopId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        boolean isCheckpoint = false;
        if ("1".equals(isCheckpointStr)) {
            isCheckpoint = true;
        }
        stopsById.setLastUpdateUser(username);
        stopsById.setLastUpdateDttm(new Date());
        stopsById.setIsCheckpoint(isCheckpoint);
        int updateStopsCheckpoint = flowDomain.updateStops(stopsById);
        if (updateStopsCheckpoint > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Saved successfully");
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Database save failed");
        }

    }

    /**
     * Modify "stopName" based on id
     *
     * @param id
     * @param stopName
     * @return
     * @throws Exception
     */
    @Override
    public int updateStopsNameById(String username, String id, String stopName) throws Exception {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        if (StringUtils.isBlank(id) || StringUtils.isBlank(stopName)) {
            return 0;
        }
        Stops stopsById = flowDomain.getStopsById(id);
        if (null == stopsById) {
            return 0;
        }
        stopsById.setLastUpdateUser(username);
        stopsById.setLastUpdateDttm(new Date());
        stopsById.setName(stopName);
        return flowDomain.updateStops(stopsById);
    }

    @Override
    public String getStopByNameAndFlowId(String flowId, String stopName) {
        return flowDomain.getStopByNameAndFlowId(flowId, stopName);
    }

    /**
     * updateStopName
     * 
     * @param username
     * @param isAdmin
     * @param stopId
     * @param flowId
     * @param stopName
     * @param pageId
     * @return
     * @throws Exception
     */
    @Override
    public String updateStopName(String username, boolean isAdmin, String stopId, String flowId, String stopName, String pageId) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isAnyEmpty(stopId, stopName, flowId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        // find flow
        Flow flowById = flowDomain.getFlowById(flowId);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(flowId));
        }
        // Judge whether it is an example
        boolean isExample = flowById.getIsExample();
        if (isExample) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Example cannot be modified");
        }
        // Determine whether to be an administrator or create a user
        String crtUser = flowById.getCrtUser();
        if (!isAdmin && (!username.equals(crtUser))) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No permission to modify, please contact the administrator");
        }
        // Determine whether the Sketchpad node exists
        List<MxCell> root = null;
        MxGraphModel mxGraphModel = flowById.getMxGraphModel();
        if (null != mxGraphModel) {
            root = mxGraphModel.getRoot();
        }
        if (null == root || root.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow '" + flowById.getId() + "' The drawing board information is empty and the update failed.");
        }
        // Determine whether the 'Stops' exists
        List<Stops> stopsList = flowById.getStopsList();
        if (null == stopsList || stopsList.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow '" + flowById.getId() + "' The Stops is empty and the update failed.");
        }
        // Check if name is the same name
        String checkResult = this.getStopByNameAndFlowId(flowById.getId(), stopName);
        if (StringUtils.isNotBlank(checkResult)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The name '" + stopName + "' has been repeated and the save failed.");
        }
        // Get the MxCell node to be modified
        MxCell mxCellUpdate = null;
        for (MxCell mxCell : root) {
            if (null == mxCell) {
                continue;
            }
            if (mxCell.getPageId().equals(pageId)) {
                mxCellUpdate = mxCell;
                break;
            }
        }
        if (null == mxCellUpdate) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The Stops node you want to modify does not exist");
        }
        // modify Stops
        int updateStopsNameById = this.updateStopsNameById(username, stopId, stopName);
        if (updateStopsNameById <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
        // modify MxCell
        mxCellUpdate.setValue(stopName);
        mxCellUpdate.setLastUpdateDttm(new Date());
        mxCellUpdate.setLastUpdateUser(username);
        int updateMxCell = flowDomain.updateMxCell(mxCellUpdate);
        if (updateMxCell <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }

        if (null == mxGraphModel) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
        }
        // Convert the mxGraphModel from the query to XML
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
        loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("XmlData", loadXml);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getStopsPort(String flowId, String sourceId, String targetId, String pathLineId) {
        // The parameter is judged empty, and it is judged whether each parameter is available. If there is, it returns directly.
        // ('isAnyEmpty' returns true whenever there is a null value)
        if (StringUtils.isAnyEmpty(flowId, sourceId, targetId, pathLineId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The part of the parameter passed in is empty");
        }
        // Find the 'stop' of 'input' and 'output'
        List<Stops> queryInfoList = flowDomain.getStopsListByFlowIdAndPageIds(flowId, new String[] { sourceId, targetId });
        // If 'queryInfoList' is empty, or the size of 'queryInfoList' is less than 2, return directly
        if (null == queryInfoList || queryInfoList.size() < 2) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Can't find 'source' or 'target'");
        }
        Stops sourceStop = null;
        Stops targetStop = null;
        // Loop out 'sourceStop' and 'targetStop'
        for (Stops stop : queryInfoList) {
            if (null == stop) {
                continue;
            }
            String pageId = stop.getPageId();
            if (sourceId.equals(pageId)) {
                sourceStop = stop;
            } else if (targetId.equals(pageId)) {
                targetStop = stop;
            }
        }
        // Return directly if 'source' or 'target' is empty
        if (null == sourceStop || null == targetStop) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Could not find 'source' or 'target'");
        }
        // Handling query port usage and encapsulating maps
        // Get the port type of "sourceStop" and "targetStop"
        PortType sourcePortType = sourceStop.getOutPortType();
        PortType targetPortType = targetStop.getInPortType();
        if (null == sourcePortType || null == targetPortType) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("'sourceStopOutports' of 'sourceStop' or 'targetStopInports' of 'targetStop' are Null and cannot be output.");
        }
        if (PortType.NONE == sourcePortType || PortType.NONE == targetPortType) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("‘sourceStopOutports’ of ‘sourceStop’ or ‘targetStopInports’ of ‘targetStop’ is ‘None’ and cannot be output");
        }
        // Call 'stopPortUsage' to query the available interfaces. The keys of the returned Map are as follows:
        // 1, key: 'stop' port type enumeration 'text' attribute, value: port can use the quantity ‘Integer’
        // 2, key : 'isSourceStop', value : whether it is 'source'
        // 3, key: 'portUsageMap', value: port details 'map' (key: port name, value: whether available 'boolean')
        // Query the occupancy of the ‘sourceStop’ interface
        Map<String, Object> sourcePortUsageMap = stopPortUsage(true, flowId, sourceStop, pathLineId);
        if (null == sourcePortUsageMap) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Query the occupation of the sourceStop interface.");
        }
        // Take out the number of available ports
        Integer sourceCounts = (Integer) sourcePortUsageMap.get(sourcePortType.getText());
        // Determine if the number of available ports is greater than 0
        if (null == sourceCounts || sourceCounts <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Query sourceStop no available port");
        }
        // Query the occupancy of the targetStop interface
        Map<String, Object> targetPortUsageMap = stopPortUsage(false, flowId, targetStop, pathLineId);
        if (null == targetPortUsageMap) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Query the occupancy of the targetStop interface.");
        }
        // Take out the number of available ports
        Integer targetCounts = (Integer) targetPortUsageMap.get(targetPortType.getText());
        // Determine if the number of available ports is greater than 0
        if (null == targetCounts || targetCounts <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Query targetStop no available port");
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        // Put the number of available ports into the returned map
        rtnMap.put("sourceCounts", sourceCounts);
        rtnMap.put("targetCounts", targetCounts);
        // Take out the detailed occupancy of the port and put it in the returned map.
        rtnMap.put("sourcePortUsageMap", (Map<String, Boolean>) sourcePortUsageMap.get("portUsageMap"));
        rtnMap.put("targetPortUsageMap", (Map<String, Boolean>) targetPortUsageMap.get("portUsageMap"));
        // Stop the port type into the returned map
        rtnMap.put("sourceType", sourcePortType);
        rtnMap.put("targetType", targetPortType);
        // The name of stop is placed in the returned map
        rtnMap.put("sourceName", sourceStop.getName());
        rtnMap.put("targetName", targetStop.getName());
        // stop route filter rule put 'rtnMap'
        rtnMap.put("sourceFilter", sourcePortUsageMap.get("sourceFilter"));
        rtnMap.put("targetFilter", targetPortUsageMap.get("targetFilter"));
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * Query port usage
     *
     * @param isSourceStop
     * @param flowId
     * @param stop
     * @param pathLineId
     * @return map key
     */
    private Map<String, Object> stopPortUsage(boolean isSourceStop, String flowId, Stops stop, String pathLineId) {
        // Map for return
        // Map content
        // 1, key: 'stop' port type enumeration 'text' attribute, value: port can use the quantity ‘Integer’
        // 2, key : 'isSourceStop', value : whether it is 'source'
        // 3, key: 'portUsageMap', value: port details 'map' (key: port name, value: whether available 'boolean')
        Map<String, Object> stopPortUsageMap = null;
        if (null != stop) {
            stopPortUsageMap = new HashMap<String, Object>();
            // List of used interfaces
            List<Paths> usedPathsList = null;

            // Port details map (using information)
            Map<String, Boolean> portUsageMap = new HashMap<String, Boolean>();

            // Get the port type of ‘stopVo’
            PortType stopPortType = (isSourceStop ? stop.getOutPortType() : stop.getInPortType());
            switch (stopPortType) {
                case DEFAULT:
                    stopTypeDefault(isSourceStop, flowId, stop, pathLineId, stopPortUsageMap, usedPathsList);
                    break;
                case USER_DEFAULT:
                    stopTypeUserDefault(isSourceStop, flowId, stop, pathLineId, stopPortUsageMap, portUsageMap);
                    break;
                case ANY:
                    stopTypeAny(isSourceStop, flowId, stop, pathLineId, stopPortUsageMap, portUsageMap);
                case ROUTE:
                    stopTypeRoute(isSourceStop, flowId, stop, pathLineId, stopPortUsageMap, portUsageMap);
                    break;
                case NONE:
                    // NONE means no port available
                    stopPortUsageMap.put(PortType.NONE.getText(), 0);
                    logger.info("stopPortType is'" + stopPortType + "'. No action, return null");
                    break;
                default:
                    stopPortUsageMap = null;
                    logger.info("stopPortType is '" + stopPortType + "'. No action, return null");
                    break;
            }
        }
        return stopPortUsageMap;
    }

    /**
     * Handling ‘stop’ ports of type 'ANY’
     *
     * @param isSourceStop
     * @param flowId
     * @param stop
     * @param pathLineId
     * @param stopPortUsageMap
     * @param portUsageMap
     */
    private void stopTypeAny(boolean isSourceStop, String flowId, Stops stop, String pathLineId, Map<String, Object> stopPortUsageMap, Map<String, Boolean> portUsageMap) {
        List<Paths> usedPathsList;// stopVo all ports
        String stopVoPortsAny = "";

        // To get the attribute name, because the Stop port of the Any type is stored in the attribute.
        String propertyVoName = "";

        // The number of available interfaces is unlimited, for the temporary use of 99999
        stopPortUsageMap.put(PortType.ANY.getText(), 99999);

        // Determine if stopVo is source
        if (isSourceStop) {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", true);

            // Query the occupancy of the stopVo interface
            usedPathsList = flowDomain.getPaths(flowId, null, stop.getPageId(), null);

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, null);

            // Given property name
            propertyVoName = "outports";
        } else {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", false);

            // Query the occupancy of the stopVo interface
            usedPathsList = flowDomain.getPaths(flowId, null, null, stop.getPageId());

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, false, usedPathsList, null);

            // Given property name
            propertyVoName = "outports";

        }
        // Get the list of properties of stopVo
        List<Property> propertyList = stop.getProperties();
        // Loop attribute to get the attributes of the storage port
        for (Property property : propertyList) {
            if (null != property) {
                // Determine whether it is the attribute of the storage port
                if (propertyVoName.equals(property.getName())) {
                    // Get all ports of stopVo
                    stopVoPortsAny = property.getCustomValue();
                    break;
                }
            }
        }
        // Put all port information into the portUsageMap
        portUsageMap = this.portStrToMap(portUsageMap, null, null, stopVoPortsAny);
        // Port usage
        stopPortUsageMap.put("portUsageMap", portUsageMap);
    }

    /**
     * Handling ‘stop’ ports of type 'ANY’
     *
     * @param isSourceStop
     * @param flowId
     * @param stop
     * @param pathLineId
     * @param stopPortUsageMap
     * @param portUsageMap
     */
    private void stopTypeRoute(boolean isSourceStop, String flowId, Stops stop, String pathLineId, Map<String, Object> stopPortUsageMap, Map<String, Boolean> portUsageMap) {
        List<Paths> usedPathsList;// stopVo all ports
        String stopVoPortsAny = "";

        // To get the attribute name, because the Stop port of the Any type is stored in the attribute.
        String propertyVoName = "";

        // The number of available interfaces is unlimited, for the temporary use of 99999
        stopPortUsageMap.put(PortType.ROUTE.getText(), 99999);

        // Determine if stopVo is source
        if (isSourceStop) {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", true);

            // Query the occupancy of the stopVo interface
            usedPathsList = flowDomain.getPaths(flowId, null, stop.getPageId(), null);

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, null);

            // Given property name
            propertyVoName = "outports";
        } else {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", false);

            // Query the occupancy of the stopVo interface
            usedPathsList = flowDomain.getPaths(flowId, null, null, stop.getPageId());

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, false, usedPathsList, null);

            // Given property name
            propertyVoName = "outports";

        }
        // Get the list of properties of stopVo
        List<Property> propertyList = stop.getProperties();
        // Loop attribute to get the attributes of the storage port
        for (Property property : propertyList) {
            if (null != property) {
                // Determine whether it is the attribute of the storage port
                if (propertyVoName.equals(property.getName())) {
                    // Get all ports of stopVo
                    stopVoPortsAny = property.getCustomValue();
                    break;
                }
            }
        }
        // Put all port information into the portUsageMap
        portUsageMap = this.portStrToMap(portUsageMap, null, null, stopVoPortsAny);
        // Port usage
        stopPortUsageMap.put("portUsageMap", portUsageMap);
        // route filter
        List<CustomizedProperty> customizedPropertyList = stop.getCustomizedPropertyList();
        List<Map<String, String>> customizedPropertyListMap = new ArrayList<>();
        if (null != customizedPropertyList && customizedPropertyList.size() > 0) {
            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                Map<String, String> customizedPropertyMap = new HashMap<>();
                customizedPropertyMap.put("name", customizedProperty.getName());
                customizedPropertyMap.put("customValue", customizedProperty.getCustomValue());
                customizedPropertyMap.put("description", customizedProperty.getDescription());
                customizedPropertyListMap.add(customizedPropertyMap);
            }
        }
        if (isSourceStop) {
            stopPortUsageMap.put("sourceFilter", customizedPropertyListMap);
        } else {
            stopPortUsageMap.put("targetFilter", customizedPropertyListMap);
        }
    }

    /**
     * Handling ‘stop’ ports of type 'DEFAULT’
     *
     * @param isSourceStop
     * @param flowId
     * @param stop
     * @param pathLineId
     * @param stopPortUsageMap
     * @param usedPathsList
     */
    private void stopTypeDefault(boolean isSourceStop, String flowId, Stops stop, String pathLineId, Map<String, Object> stopPortUsageMap, List<Paths> usedPathsList) {
        Integer availablePathsCounts;// The default number of available ports for DEFAULT is 1.
        availablePathsCounts = 1;

        // Determine if ‘stopVo’ is ‘source’
        if (isSourceStop) {
            // put isSourceStop
            stopPortUsageMap.put("isSourceStop", true);
            // Query 'stopVo' port usage
            flowDomain.getPaths(flowId, null, stop.getPageId(), null);
        } else {
            // put isSourceStop
            stopPortUsageMap.put("isSourceStop", false);

            // Query 'stopVo' port usage
            usedPathsList = flowDomain.getPaths(flowId, null, null, stop.getPageId());
        }

        // Determine whether the occupancy of the stopVo interface is empty.
        if (null != usedPathsList) {
            String currentPathsId = "";
            Paths currentPaths = null;
            List<Paths> pathsList = flowDomain.getPaths(flowId, pathLineId, null, null);
            if (null != pathsList && pathsList.size() == 1) {
                currentPaths = pathsList.get(0);
            }
            if (null != currentPaths) {
                currentPathsId = currentPaths.getId();
            }
            // Loop and current line comparison, exclude current line
            for (Paths paths : usedPathsList) {
                if (null != paths) {
                    if (currentPathsId.equals(paths.getId())) {
                        continue;
                    }
                    availablePathsCounts = availablePathsCounts - 1;
                }
            }
        }
        // Determine the number of available interfaces
        if (null != availablePathsCounts && availablePathsCounts < 0) {
            logger.warn("The number of available ports is negative, and the default is reset to 0.");
            availablePathsCounts = 0;
        }
        // Put the number of available interfaces
        stopPortUsageMap.put(PortType.DEFAULT.getText(), availablePathsCounts);
    }

    /**
     * Handling ‘stop’ ports of type 'USER_DEFAULT’
     *
     * @param isSourceStop
     * @param flowId
     * @param stop
     * @param pathLineId
     * @param stopPortUsageMap
     * @param portUsageMap
     */
    private void stopTypeUserDefault(boolean isSourceStop, String flowId, Stops stop, String pathLineId, Map<String, Object> stopPortUsageMap, Map<String, Boolean> portUsageMap) {
        List<Paths> usedPathsList;
        Integer usedPathsCounts;
        Integer availablePathsCounts;// stopVo all ports
        String stopVoPorts = "";
        // Determine if stopVo is source
        if (isSourceStop) {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", true);

            // Query the occupancy of the stopVo interface
            usedPathsList = flowDomain.getPaths(flowId, null, stop.getPageId(), null);

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, null);

            // Get all ports of stopVo
            stopVoPorts = stop.getOutports();

        } else {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", false);

            // Query the occupancy of the stopVo interface
            usedPathsList = flowDomain.getPaths(flowId, null, null, stop.getPageId());

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, false, usedPathsList, null);

            // Get all ports of stopVo
            stopVoPorts = stop.getInports();
        }
        // Currently, only the used ports are placed in the portUsageMap, and all the longest ones are the number of used ports.
        usedPathsCounts = portUsageMap.size();
        // Put all port information into the portUsageMap
        portUsageMap = this.portStrToMap(portUsageMap, null, null, stopVoPorts);
        // portUsageMap has just been put into all ports, so the size of portUsageMap minus the number of used ports is the number of available ports.
        availablePathsCounts = portUsageMap.size() - usedPathsCounts;
        // Put the number of available interfaces
        stopPortUsageMap.put(PortType.USER_DEFAULT.getText(), availablePathsCounts);

        // Port usage
        stopPortUsageMap.put("portUsageMap", portUsageMap);
    }

    /**
     * Put port data into portUsageMap
     *
     * @param portUsageMap  All ports and their usage
     * @param isSourceStop  Is it sourceStop?
     * @param usedPathsList The information of the line queried by the database
     *                      represents the port that has been used (excluding the
     *                      port used by the current line)
     * @param stopVoPorts   Port information in stop, all ports
     * @return
     */
    private Map<String, Boolean> portStrToMap(Map<String, Boolean> portUsageMap, Boolean isSourceStop, List<Paths> usedPathsList, String stopVoPorts) {
        if (null != portUsageMap) {
            if (null != usedPathsList && usedPathsList.size() > 0) {
                String currentPathsId = "";
                // Paths currentPaths = null;
                // Paths currentPaths = pathsServiceImpl.getPathsByFlowIdAndPageId(flowId, pathLineId);
                // if (null != currentPaths) {
                //     currentPathsId = currentPaths.getId();
                // }
                if (null != isSourceStop && isSourceStop) {
                    // Put the port that has been found into the map
                    for (Paths paths : usedPathsList) {
                        if (null != paths && StringUtils.isNotBlank(paths.getOutport())) {
                            if (currentPathsId.equals(paths.getId())) {
                                portUsageMap.put(paths.getOutport(), true);
                            } else {
                                portUsageMap.put(paths.getOutport(), false);
                            }
                        }
                    }
                } else {
                    // Put the port that has been found into the map
                    for (Paths paths : usedPathsList) {
                        if (null != paths && StringUtils.isNotBlank(paths.getInport())) {
                            if (currentPathsId.equals(paths.getId())) {
                                portUsageMap.put(paths.getInport(), true);
                            } else {
                                portUsageMap.put(paths.getInport(), false);
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(stopVoPorts)) {
                // The string of all ports of stopVo is converted to a String array.
                String[] stopVoPortsSplit = stopVoPorts.split(",");
                // Determine whether the conversion is successful and the judgment is null
                if (null != stopVoPortsSplit && stopVoPortsSplit.length > 0) {
                    // Loop array
                    for (String portName : stopVoPortsSplit) {
                        if (StringUtils.isNotBlank(portName)) {
                            // In the portUsageMap, the value is taken according to the port name. If it is obtained, the description is released.
                            Boolean isUsed = portUsageMap.get(portName);
                            // Before putting all the used ports into the map, if you can't get it, it means that it has not been put in or used.
                            if (null == isUsed) {
                                portUsageMap.put(portName, true);
                            }
                        }
                    }
                }
            }
        }
        return portUsageMap;
    }

    /**
     * fill datasource to stop
     *
     * @param dataSourceId
     * @param stopId
     * @return
     * @throws Exception
     */
    @Override
    public String fillDatasource(String username, boolean isAdmin, String dataSourceId, String stopId) throws Exception {

        // Get current user
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Determine if StopId is empty, if it is, then return, otherwise continue
        if (StringUtils.isBlank(stopId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopId"));
        }
        // Query Stops by "stopId"
        Stops stopsById = flowDomain.getStopsById(stopId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(stopId));
        }
        // Get Stops all attributes
        List<Property> propertyList = stopsById.getProperties();
        // Determine if the "stop" attribute with ID "stopId" is empty. Returns if it is empty, otherwise continues.
        if (null == propertyList || propertyList.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,stop property is null");
        }
        if (StringUtils.isNotBlank(dataSourceId)) {
            DataSource dataSourceById = dataSourceDomain.getDataSourceById(username, isAdmin, dataSourceId);
            if (null == dataSourceById) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,Cannot find Datasource with id " + dataSourceId);
            }
            // Get Database all attributes
            List<DataSourceProperty> dataSourcePropertyList = dataSourceById.getDataSourcePropertyList();
            // Determine whether the Datasource attribute whose ID is "dataSourceId" is empty. Returns if it is empty, otherwise it is converted to Map.
            if (null == dataSourcePropertyList || dataSourcePropertyList.size() <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,dataSource property is null");
            }
            stopsById = StopsUtils.fillStopsPropertiesByDatasource(stopsById, dataSourceById, username);
        } else {
            // Loop fill "stop"
            for (Property property : propertyList) {
                // "stop" attribute isSelect
                Boolean isLocked = property.getIsLocked();
                property.setStops(stopsById);
                // Judge empty
                if (null != isLocked && isLocked) {
                    // Assignment
                    property.setCustomValue("");
                    property.setIsLocked(false);
                    property.setLastUpdateDttm(new Date());
                    property.setLastUpdateUser(username);
                }
            }
            stopsById.setDataSource(null);
            stopsById.setProperties(propertyList);
        }
        flowDomain.saveOrUpdate(stopsById);
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * isNeedSource
     *
     * @param username
     * @param isAdmin
     * @param stopsId
     * @return
     */
    @Override
    public String isNeedSource(String username, boolean isAdmin, String stopsId) {
        // Get current user
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Determine if StopId is empty, if it is, then return, otherwise continue
        if (StringUtils.isBlank(stopsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopsId"));
        }
        // Query Stops by "stopId"
        Stops stopsById = flowDomain.getStopsById(stopsId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(stopsId));
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("isNeedSource", false);
        if (PortType.NONE == stopsById.getInPortType()) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        rtnMap.put("isNeedSource", true);
        String[] portsArr = stopsById.getInports().split(",");
        if (PortType.ANY != stopsById.getInPortType()) {
            rtnMap.put("ports", portsArr);
            return ReturnMapUtils.toJson(rtnMap);
        }
        List<Property> properties = stopsById.getProperties();
        for (Property property : properties) {
            if (!"inports".equals(property.getName()) ) {
                continue;
            }
            String customValue = property.getCustomValue();
            if (null != customValue) {
                portsArr = customValue.split(",");
            }
            break;
        }
        rtnMap.put("ports", portsArr);
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * run stops
     *
     * @param username
     * @param isAdmin
     * @param runStopsVo
     * @return
     * @throws Exception
     */
    @Override
    public String runStops(String username, boolean isAdmin, RunStopsVo runStopsVo) throws Exception {
        // Get current user
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == runStopsVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (null == runStopsVo.getStopsId()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopsId"));
        }
        // Get stops information according to stopsId
        Stops stopsById = flowDomain.getStopsById(runStopsVo.getStopsId());
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(runStopsVo.getStopsId()));
        }
        Flow flowDB = stopsById.getFlow();
        if (null == flowDB) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The Flow of Stops is empty ");
        }
        MxGraphModel flowMxGraphModel = flowDB.getMxGraphModel();
        // Determine whether flowMxGraphModel is empty
        if (null == flowMxGraphModel) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("MxGraphModel information error");
        }
        // Take out the id array of testData
        String[] testDataIds = runStopsVo.getTestDataIds();
        // Take out the ports array
        String[] ports = runStopsVo.getPorts();
        // Path of testData in HDFS
        List<Map<String, String>> hdfsUrlArray = new ArrayList<>();
        if (null != testDataIds && testDataIds.length > 0) {
            if (null == ports) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataIds and ports do not correspond");
            }
            if (ports.length != testDataIds.length) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataIds and ports do not correspond");
            }
            // Loop testDataIds, and write the queried testData data to HDFS
            for (int i = 0; i < testDataIds.length; i++) {
                // Get the schema
                // Query the field according to Id List<Map<String,String>> key1=ID key2=FIELD_NAME
                List<LinkedHashMap<String, Object>> schemaIdAndNameList = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataIds[i]);
                if (null == schemaIdAndNameList || schemaIdAndNameList.size() <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("testData error");
                }
                String schema = "";
                for (LinkedHashMap<String, Object> testDataSchema : schemaIdAndNameList) {
                    if (StringUtils.isNotBlank(schema)) {
                        schema += ",";
                    }
                    schema += testDataSchema.get("FIELD_NAME");
                }

                // Get the testDataValues
                String testDataValueStr = "";
                List<LinkedHashMap<String, Object>> schemaValueList = testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataIds[i], schemaIdAndNameList);
                if (null == schemaValueList || schemaValueList.size() <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("testData values error");
                }
                for (LinkedHashMap<String, Object> testDataSchemaValues : schemaValueList) {
                    String testDataLineStr = "";
                    for (String key : testDataSchemaValues.keySet()) {
                        if ("dataRow".equals(key)) {
                            continue;
                        }
                        if (StringUtils.isNotBlank(testDataLineStr)) {
                            testDataLineStr += "☔";
                        }
                        testDataLineStr += testDataSchemaValues.get(key).toString();
                    }
                    testDataValueStr += (testDataLineStr + "\n");
                }
                // Get the time to make a rough name
                long currentTimeMillis = System.currentTimeMillis();
                // Get HDFS address
                String testDataPathUrl = flowImpl.getTestDataPathUrl();
                // HDFS storage address
                String hdfsUrl = testDataPathUrl + currentTimeMillis + i + ".csv";
                // Write testData to HDFS
                HdfsUtils.writeData(hdfsUrl, testDataValueStr);
                Map<String, String> hdfsUrlObj = new HashMap<>();
                hdfsUrlObj.put("csvpath", hdfsUrl);
                hdfsUrlObj.put("header", "false");
                hdfsUrlObj.put("delimiter", "☔");
                hdfsUrlObj.put("schema", schema);
                // The location should correspond to testDataIds
                hdfsUrlArray.add(i, hdfsUrlObj);
            }
        }
        // Used to store stops converted to process
        Process process = new Process();
        // Copy basic information
        BeanUtils.copyProperties(flowDB, process);
        ProcessUtils.initProcessBasicPropertiesNoId(process, username);
        // Monitoring component information
        List<ProcessStop> processStopsList = new ArrayList<>();
        // The stops queried by stopId are transferred to processStops
        ProcessStop stopsToProcessStop = ProcessUtils.stopsToProcessStop(stopsById, username, isAdmin);
        if (null == stopsToProcessStop) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Stops conversion to ProcessStop failed ");
        }
        processStopsList.add(stopsToProcessStop);
        // Monitor connection information
        List<ProcessPath> processPathsList = new ArrayList<>();
        // Monitor page information
        MxGraphModel mxGraphModel = MxGraphModelUtils.initMxGraphModelBasicPropertiesNoId(null, username, isAdmin);
        // Basic page information of monitoring elements
        List<MxCell> rootMxCell = MxCellUtils.initMxCell(username, mxGraphModel);
        MxCell mxCellDB = flowDomain.getMxCellByMxGraphIdAndPageId(flowMxGraphModel.getId(), stopsById.getPageId());
        MxCell processMxCell = new MxCell();
        BeanUtils.copyProperties(mxCellDB, processMxCell);
        processMxCell = MxCellUtils.initMxCellBasicPropertiesNoId(processMxCell, username);
        rootMxCell.add(processMxCell);
        // Determine whether to execute
        if (runStopsVo.getIsRunFollowUp()) {
            
            // get stopsList
            List<Stops> stopsList = flowDB.getStopsList();
            //stopsList conversion map (key is pageId)
            Map<String, Stops> stopsMap = new HashMap<>();
            for (Stops stops : stopsList) {
                if (null == stops) {
                    continue;
                }
                stopsMap.put(stops.getPageId(), stops);
            }
            
            // get pathsList
            List<Paths> pathsList = flowDB.getPathsList();

            // pathsList conversion map (key is the pageId of the source)
            Map<String, List<Paths>> pathsMap = new HashMap<>();
            for (Paths paths : pathsList) {
                if (null == paths) {
                    continue;
                }
                List<Paths> pathsListByMap = pathsMap.get(paths.getFrom());
                if (null == pathsListByMap) {
                    pathsListByMap = new ArrayList<>();
                }
                pathsListByMap.add(paths);
                pathsMap.put(paths.getFrom(), pathsListByMap);
            }

            // get mxGraphModelRoot
            List<MxCell> flowMxGraphModelRoot = flowMxGraphModel.getRoot();
            // Determine whether flowMxGraphModelRoot is empty
            if (null == flowMxGraphModelRoot || flowMxGraphModelRoot.size() <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("MxGraphModel information error");
            }
            Map<String, MxCell> flowMxGraphModelRootMap = new HashMap<>();
            // flowMxGraphModelRoot conversion map
            for (MxCell mxCell : flowMxGraphModelRoot) {
                if (null == mxCell) {
                    continue;
                }
                flowMxGraphModelRootMap.put(mxCell.getPageId(), mxCell);
            }
            
            String stopsPageId = stopsById.getPageId();
            String pathspageId = "";

            List<Paths> pathsListByStopsPageId = pathsMap.get(stopsPageId);
            List<Paths> extractPaths = extractPaths(pathsMap, pathsListByStopsPageId);
            for(Paths paths : extractPaths) {
                if (null == paths) {
                    break;
                }
                pathspageId = paths.getPageId();
                String targetStopsPageId = paths.getTo();
                if (StringUtils.isBlank(targetStopsPageId)) {
                    break;
                }
                Stops targetStops = stopsMap.get(targetStopsPageId);
                if (null == targetStops) {
                    break;
                }
                ProcessStop processStop = ProcessUtils.stopsToProcessStop(targetStops, username, false);
                if (null == processStop) {
                    break;
                }
                // Associate foreign key
                processStop.setProcess(process);

                ProcessPath processPath = ProcessPathUtils.pathsToProcessPath(paths, username, false);
                if (null == processPath) {
                    break;
                }
                // Associated foreign key
                processPath.setProcess(process);

                MxCell pathsMxCell = flowMxGraphModelRootMap.get(pathspageId);
                MxCell pathsMxCellNew = MxCellUtils.copyMxCell(pathsMxCell, username, false);
                if (null == pathsMxCellNew) {
                    break;
                }
                pathsMxCellNew.setMxGraphModel(mxGraphModel);
                MxCell stopsMxCell = flowMxGraphModelRootMap.get(targetStopsPageId);
                MxCell stopsMxCellNew = MxCellUtils.copyMxCell(stopsMxCell, username, false);
                if (null == stopsMxCellNew) {
                    break;
                }
                stopsMxCellNew.setMxGraphModel(mxGraphModel);

                processStopsList.add(processStop);
                processPathsList.add(processPath);
                rootMxCell.add(stopsMxCellNew);
                rootMxCell.add(pathsMxCellNew);
            }
        }

        // Determine whether the HDFS value is empty
        if (hdfsUrlArray.size() > 0) {
            StopsComponent stopsComponentByBundle = stopsComponentDomain.getStopsComponentByBundle("cn.piflow.bundle.csv.CsvParser");
            Integer maxStopPageIdByFlowId = flowDomain.getMaxStopPageIdByFlowId(flowDB.getId());
            for (int i = 0; i < hdfsUrlArray.size(); i++) {
                // Get the corresponding url data information
                Map<String, String> hdfsUrlObj = hdfsUrlArray.get(i);
                // Generate processStop according to stopsComponentByBundle
                ProcessStop processStop = ProcessStopUtils.copyStopsComponentToProcessStop(stopsComponentByBundle,
                        username, false);
                if (null == processStop) {
                    continue;
                }
                // set pageId
                processStop.setPageId("" + (maxStopPageIdByFlowId + 1 + i) + "");
                // Fill in relevant attributes
                List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                    String nameLowerCase = processStopProperty.getName().toLowerCase();
                    if (StringUtils.isBlank(nameLowerCase)) {
                        continue;
                    }
                    processStopProperty.setCustomValue(hdfsUrlObj.get(nameLowerCase));
                }
                processStop.setProcessStopPropertyList(processStopPropertyList);
                processStop.setName(processStop.getName() + System.currentTimeMillis() + i);
                processStopsList.add(processStop);
                // Generate page processStop information
                //MxCell processMxCell_stops = MxCellUtils.AddMxCellNode(username, processStop.getPageId(), stopsComponentByBundle.getName(), "/images/" + stopsComponentByBundle.getName() + "_128x128.png");
                //rootMxCell.add(processMxCell_stops);

                String outPortName = PortType.DEFAULT.getText().equals(stopsComponentByBundle.getOutports()) ? "" : stopsComponentByBundle.getOutports();
                String inPortName = PortType.DEFAULT.getText().equals(ports[i]) ? "" : ports[i];

                // Generate processPath information
                ProcessPath processPath = ProcessPathUtils.initProcessPathBasicPropertiesNoId(null, username);
                processPath.setFrom("" + (maxStopPageIdByFlowId + 1 + i) + "");
                processPath.setOutport(outPortName);
                processPath.setTo(stopsById.getPageId());
                processPath.setInport(inPortName);
                processPath.setPageId("" + (0 - (maxStopPageIdByFlowId + 1 + i)) + "");
                processPath.setProcess(process);
                processPathsList.add(processPath);
                // Generate page processPath information
                //MxCell processMxCell_line = MxCellUtils.AddMxCellLine(username, processPath.getPageId());
                //processMxCell_line.setSource(processPath.getFrom());
                //processMxCell_line.setTarget(processPath.getTo());
                //rootMxCell.add(processMxCell_line);
            }
        }

        mxGraphModel.setRoot(rootMxCell);
        process.setMxGraphModel(mxGraphModel);
        process.setProcessStopList(processStopsList);
        process.setProcessPathList(processPathsList);
        process.setLastUpdateUser(username);
        process.setLastUpdateDttm(new Date());
        
        processDomain.addProcess(process);
        process = processDomain.getProcessById(username, isAdmin, process.getId());

        Map<String, Object> stringObjectMap = flowImpl.startFlow(process, null, RunModeType.RUN);
        if (null == stringObjectMap || 200 != ((Integer) stringObjectMap.get("code"))) {
            process.setEnableFlag(false);
            process.setLastUpdateDttm(new Date());
            process.setLastUpdateUser(username);
            processDomain.updateProcess(process);
            return ReturnMapUtils.setFailedMsgRtnJsonStr((String) stringObjectMap.get("errorMsg"));
        }
        process.setAppId((String) stringObjectMap.get("appId"));
        process.setProcessId((String) stringObjectMap.get("appId"));
        process.setState(ProcessState.STARTED);
        processDomain.updateProcess(process);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("save process success,update success");
        rtnMap.put("processId", process.getId());
        return ReturnMapUtils.toJson(rtnMap);

    }
    
    @Override
    public String checkDatasourceLinked(String datasourceId) {
        if (StringUtils.isBlank(datasourceId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("datasourceId is null");
        }
        List<String> stopsNamesByDatasourceId = flowDomain.getStopsNamesByDatasourceId(datasourceId);
        if (null == stopsNamesByDatasourceId || stopsNamesByDatasourceId.size() <= 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("isLinked", false);
        }
        Map<String, Object> setSucceededCustomParam = ReturnMapUtils.setSucceededCustomParam("isLinked", true);
        return ReturnMapUtils.appendValuesToJson(setSucceededCustomParam, "stopsNameList", stopsNamesByDatasourceId);
    }

    @Override
    public String addStopCustomizedProperty(String username, StopsCustomizedPropertyVo stopsCustomizedPropertyVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_OPERATION_MSG());
        }
        if (null == stopsCustomizedPropertyVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String stopId = stopsCustomizedPropertyVo.getStopId();
        if (StringUtils.isBlank(stopId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopId"));
        }
        List<CustomizedProperty> customizedPropertyListByStopsIdAndName = flowDomain.getCustomizedPropertyListByStopsIdAndName(stopId, stopsCustomizedPropertyVo.getName());
        if (null != customizedPropertyListByStopsIdAndName && customizedPropertyListByStopsIdAndName.size() > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DUPLICATE_NAME_PLEASE_MODIFY_MSG("StopsCustomizedPropertyName"));
        }
        Stops stopsById = flowDomain.getStopsById(stopId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(stopId));
        }
        CustomizedProperty customizedProperty = new CustomizedProperty();
        BeanUtils.copyProperties(stopsCustomizedPropertyVo, customizedProperty);
        String id = UUIDUtils.getUUID32();
        customizedProperty.setId(id);
        customizedProperty.setCrtDttm(new Date());
        customizedProperty.setCrtUser(username);
        customizedProperty.setLastUpdateDttm(new Date());
        customizedProperty.setLastUpdateUser(username);
        customizedProperty.setEnableFlag(true);

        customizedProperty.setStops(stopsById);
        int optDataCount = flowDomain.addCustomizedProperty(customizedProperty);
        if (optDataCount > 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopPageId", stopsById.getPageId());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
        }

    }

    @Override
    public String updateStopsCustomizedProperty(String username, StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == stopsCustomizedPropertyVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String id = stopsCustomizedPropertyVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopId"));
        }
        int optDataCount = flowDomain.updateCustomizedPropertyCustomValue(username, stopsCustomizedPropertyVo.getCustomValue(), id);
        if (optDataCount > 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("value", stopsCustomizedPropertyVo.getCustomValue());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
    }

    @Override
    public String deleteStopsCustomizedProperty(String username, String customPropertyId) {
        int optDataCount = 0;
        optDataCount = flowDomain.updateEnableFlagByStopId(username, customPropertyId);
        if (optDataCount <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String deleteRouterStopsCustomizedProperty(String username, String customPropertyId) {
        return this.deleteStopsCustomizedProperty(username,customPropertyId);
    }

    @Override
    public String getRouterStopsCustomizedProperty(String customPropertyId) {
        if (StringUtils.isBlank(customPropertyId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        CustomizedProperty customizedPropertyById = flowDomain.getCustomizedPropertyById(customPropertyId);
        if (null == customizedPropertyById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(customPropertyId));
        }
        Stops stops = customizedPropertyById.getStops();
        if (null == stops || null == stops.getFlow()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        String flowId = stops.getFlow().getId();
        String stopsPageId = stops.getPageId();
        if (StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("remove failed , stopID or flowID data error");
        }
        List<PathsVo> pathsVoList = new ArrayList<>();
        List<Paths> pathsList = flowDomain.getPaths(flowId, null, stopsPageId, null);
        if (null == pathsList || pathsList.size() <= 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("pathsVoList", pathsVoList);
        }
        for (Paths paths : pathsList) {
            Stops stopByFlowIdAndStopPageId = flowDomain.getStopByFlowIdAndStopPageId(flowId, paths.getTo());
            if (null == stopByFlowIdAndStopPageId) {
                continue;
            }
            PathsVo pathsVo = new PathsVo();
            BeanUtils.copyProperties(paths, pathsVo);
            pathsVo.setFrom(stops.getName());
            pathsVo.setTo(stopByFlowIdAndStopPageId.getName());
            pathsVoList.add(pathsVo);
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("pathsVoList", pathsVoList);
    }
    
    private List<Paths> extractPaths(Map<String, List<Paths>> pathsMap, List<Paths> pathsList) {
        if (null == pathsMap) {
            return null;
        }
        if (null == pathsList || pathsList.size() <= 0) {
            return null;
        }
        List<Paths> all = new ArrayList<>();
        all.addAll(pathsList);
        for (Paths paths : pathsList) {
            List<Paths> list = pathsMap.get(paths.getTo());
            List<Paths> extractPaths = extractPaths(pathsMap, list);
            if (null == extractPaths || extractPaths.size() <= 0) {
                break;
            }
            all.addAll(extractPaths);
        }
        return all;
    }

}
