package cn.cnic.component.flow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.cnic.base.util.HdfsUtils;
import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.MxGraphUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.dataSource.jpa.domain.DataSourceDomain;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import cn.cnic.component.flow.domain.StopsDomainU;
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
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.mxGraph.mapper.MxCellMapper;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.process.domain.ProcessDomainU;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.process.utils.ProcessPathUtils;
import cn.cnic.component.process.utils.ProcessStopUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.controller.requestVo.RunStopsVo;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;

@Service
public class StopsServiceImpl implements IStopsService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private StopsDomainU stopsDomainU;

    @Resource
    private MxCellMapper mxCellMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private DataSourceDomain dataSourceDomain;

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private TestDataDomain testDataDomain;

    @Resource
    private StopsComponentDomain stopsComponentDomain;

    @Resource
    private IFlow flowImpl;

    @Resource
    private ProcessDomainU processDomainU;

    @Override
    public int deleteStopsByFlowId(String id) {
        return stopsDomainU.updateEnableFlagByFlowId(id, id);
    }

    /**
     * Query stops based on flowId and pagesId
     *
     * @param flowId  Required
     * @param pageIds Can be empty
     * @return
     */
    @Override
    public List<StopsVo> getStopsByFlowIdAndPageIds(String flowId, String[] pageIds) {
        List<Stops> stopsList = stopsDomainU.getStopsListByFlowIdAndPageIds(flowId, pageIds);
        List<StopsVo> stopsVoList = StopsUtils.stopsListPoToVo(stopsList);
        return stopsVoList;
    }

    @Override
    public Integer stopsUpdate(String username, StopsVo stopsVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        if (null == stopsVo) {
            return 0;
        }
        Stops stopsById = stopsDomainU.getStopsById(stopsVo.getId());
        if (null == stopsById) {
            return 0;
        }
        BeanUtils.copyProperties(stopsVo, stopsById);
        stopsById.setLastUpdateDttm(new Date());
        stopsById.setLastUpdateUser(username);
        return stopsDomainU.updateStops(stopsById);
    }

    @Override
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo) {
        return stopsDomainU.updateStopsByFlowIdAndName(stopVo);
    }

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
        Stops stopsById = stopsDomainU.getStopsById(stopId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data is null");
        }
        boolean isCheckpoint = false;
        if ("1".equals(isCheckpointStr)) {
            isCheckpoint = true;
        }
        stopsById.setLastUpdateUser(username);
        stopsById.setLastUpdateDttm(new Date());
        stopsById.setIsCheckpoint(isCheckpoint);
        int updateStopsCheckpoint = stopsDomainU.updateStops(stopsById);
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
        Stops stopsById = stopsDomainU.getStopsById(id);
        if (null == stopsById) {
            return 0;
        }
        stopsById.setLastUpdateUser(username);
        stopsById.setLastUpdateDttm(new Date());
        stopsById.setName(stopName);
        return stopsDomainU.updateStops(stopsById);
    }

    @Override
    public String getStopByNameAndFlowId(String flowId, String stopName) {
        return stopsDomainU.getStopByNameAndFlowId(flowId, stopName);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isAnyEmpty(stopId, stopName, flowId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameter is empty");
        }
        // find flow
        Flow flowById = flowMapper.getFlowById(flowId);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("flow information is empty");
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
        // modify MxCell
        mxCellUpdate.setValue(stopName);
        mxCellUpdate.setLastUpdateDttm(new Date());
        mxCellUpdate.setLastUpdateUser(username);
        int updateMxCell = mxCellMapper.updateMxCell(mxCellUpdate);
        if (updateMxCell <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
        Map<String, Object> rtnMap = new HashMap<>();
        if (null != mxGraphModel) {
            // Convert the mxGraphModel from the query to XML
            String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
            loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
            rtnMap.put("XmlData", loadXml);
        }
        rtnMap.put("code", 200);
        rtnMap.put("errorMsg", "Successfully modified");
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @SuppressWarnings("unchecked")
    public String getStopsPort(String flowId, String sourceId, String targetId, String pathLineId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        // The parameter is judged empty, and it is judged whether each parameter is available. If there is, it returns directly.
        // ('isAnyEmpty' returns true whenever there is a null value)
        if (StringUtils.isAnyEmpty(flowId, sourceId, targetId, pathLineId)) {
            logger.warn("The part of the parameter passed in is empty");
            rtnMap.put("errorMsg", "The part of the parameter passed in is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Find the 'stop' of 'input' and 'output'
        List<Stops> queryInfoList = stopsDomainU.getStopsListByFlowIdAndPageIds(flowId, new String[] { sourceId, targetId });
        // If 'queryInfoList' is empty, or the size of 'queryInfoList' is less than 2, return directly
        if (null == queryInfoList || queryInfoList.size() < 2) {
            logger.warn("Can't find 'source' or 'target'");
            rtnMap.put("errorMsg", "Can't find 'source' or 'target'");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        Stops sourceStop = null;
        Stops targetStop = null;
        // Loop out 'sourceStop' and 'targetStop'
        for (Stops stop : queryInfoList) {
            if (null != stop) {
                String pageId = stop.getPageId();
                if (sourceId.equals(pageId)) {
                    sourceStop = stop;
                } else if (targetId.equals(pageId)) {
                    targetStop = stop;
                }
            }
        }
        // Return directly if 'source' or 'target' is empty
        if (null == sourceStop || null == targetStop) {
            logger.warn("Could not find 'source' or 'target'");
            rtnMap.put("errorMsg", "Could not find 'source' or 'target'");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Handling query port usage and encapsulating maps
        // Get the port type of "sourceStop" and "targetStop"
        PortType sourcePortType = sourceStop.getOutPortType();
        PortType targetPortType = targetStop.getInPortType();
        if (null == sourcePortType || null == targetPortType) {
            logger.warn("'sourceStopOutports' of 'sourceStop' or 'targetStopInports' of 'targetStop' are Null and cannot be output.");
            rtnMap.put("errorMsg", "'sourceStopOutports' of 'sourceStop' or 'targetStopInports' of 'targetStop' are Null and cannot be output.");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (PortType.NONE == sourcePortType || PortType.NONE == targetPortType) {
            logger.warn("‘sourceStopOutports’ of ‘sourceStop’ or ‘targetStopInports’ of ‘targetStop’ is ‘None’ and cannot be output");
            rtnMap.put("errorMsg", "‘sourceStopOutports’ of ‘sourceStop’ or ‘targetStopInports’ of ‘targetStop’ is ‘None’ and cannot be output");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Call 'stopPortUsage' to query the available interfaces. The keys of the returned Map are as follows:
        // 1, key: 'stop' port type enumeration 'text' attribute, value: port can use the quantity ‘Integer’
        // 2, key : 'isSourceStop', value : whether it is 'source'
        // 3, key: 'portUsageMap', value: port details 'map' (key: port name, value: whether available 'boolean')
        // Query the occupancy of the ‘sourceStop’ interface
        Map<String, Object> sourcePortUsageMap = stopPortUsage(true, flowId, sourceStop, pathLineId);
        if (null == sourcePortUsageMap) {
            logger.warn("Query the occupation of the sourceStop interface.");
            rtnMap.put("errorMsg", "Query the occupation of the sourceStop interface.");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Take out the number of available ports
        Integer sourceCounts = (Integer) sourcePortUsageMap.get(sourcePortType.getText());
        // Determine if the number of available ports is greater than 0
        if (null == sourceCounts || sourceCounts <= 0) {
            logger.warn("Query sourceStop no available port");
            rtnMap.put("errorMsg", "Query sourceStop no available port");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Query the occupancy of the targetStop interface
        Map<String, Object> targetPortUsageMap = stopPortUsage(false, flowId, targetStop, pathLineId);
        if (null == targetPortUsageMap) {
            logger.warn("Query the occupancy of the targetStop interface.");
            rtnMap.put("errorMsg", "Query the occupancy of the targetStop interface.");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Take out the number of available ports
        Integer targetCounts = (Integer) targetPortUsageMap.get(targetPortType.getText());
        // Determine if the number of available ports is greater than 0
        if (null == targetCounts || targetCounts <= 0) {
            logger.warn("Query targetStop no available port");
            rtnMap.put("errorMsg", "Query targetStop no available port");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        rtnMap.put("code", 200);
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
        return JsonUtils.toJsonNoException(rtnMap);
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
            usedPathsList = pathsMapper.getPaths(flowId, null, stop.getPageId(), null);

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, null);

            // Given property name
            propertyVoName = "outports";
        } else {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", false);

            // Query the occupancy of the stopVo interface
            usedPathsList = pathsMapper.getPaths(flowId, null, null, stop.getPageId());

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
            usedPathsList = pathsMapper.getPaths(flowId, null, stop.getPageId(), null);

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, null);

            // Given property name
            propertyVoName = "outports";
        } else {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", false);

            // Query the occupancy of the stopVo interface
            usedPathsList = pathsMapper.getPaths(flowId, null, null, stop.getPageId());

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
            pathsMapper.getPaths(flowId, null, stop.getPageId(), null);
        } else {
            // put isSourceStop
            stopPortUsageMap.put("isSourceStop", false);

            // Query 'stopVo' port usage
            usedPathsList = pathsMapper.getPaths(flowId, null, null, stop.getPageId());
        }

        // Determine whether the occupancy of the stopVo interface is empty.
        if (null != usedPathsList) {
            String currentPathsId = "";
            Paths currentPaths = null;
            List<Paths> pathsList = pathsMapper.getPaths(flowId, pathLineId, null, null);
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
            usedPathsList = pathsMapper.getPaths(flowId, null, stop.getPageId(), null);

            // Put the used port into the portUsageMap
            portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, null);

            // Get all ports of stopVo
            stopVoPorts = stop.getOutports();

        } else {
            // Whether put is source
            stopPortUsageMap.put("isSourceStop", false);

            // Query the occupancy of the stopVo interface
            usedPathsList = pathsMapper.getPaths(flowId, null, null, stop.getPageId());

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
    public String fillDatasource(String username, String dataSourceId, String stopId) throws Exception {

        // Get current user
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        // Determine if StopId is empty, if it is, then return, otherwise continue
        if (StringUtils.isBlank(stopId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,stopId is null");
        }
        // Query Stops by "stopId"
        Stops stopsById = stopsDomainU.getStopsById(stopId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,Cannot find Stops with id " + stopId);
        }
        // Get Stops all attributes
        List<Property> propertyList = stopsById.getProperties();
        // Determine if the "stop" attribute with ID "stopId" is empty. Returns if it is empty, otherwise continues.
        if (null == propertyList || propertyList.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,stop property is null");
        }
        // datasource Property Map(Key is the attribute name)
        Map<String, String> dataSourcePropertyMap = new HashMap<>();
        if (StringUtils.isNotBlank(dataSourceId)) {
            DataSource dataSourceById = dataSourceDomain.getDataSourceById(dataSourceId);
            if (null == dataSourceById) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,Cannot find Datasource with id " + dataSourceId);
            }
            // Get Database all attributes
            List<DataSourceProperty> dataSourcePropertyList = dataSourceById.getDataSourcePropertyList();
            // Determine whether the Datasource attribute whose ID is "dataSourceId" is empty. Returns if it is empty, otherwise it is converted to Map.
            if (null == dataSourcePropertyList || dataSourcePropertyList.size() <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,dataSource property is null");
            }
            stopsById.setDataSource(dataSourceById);
            // Loop "datasource" attribute to map
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                // "datasource" attribute name
                String dataSourcePropertyName = dataSourceProperty.getName();
                // Judge empty and lowercase
                if (StringUtils.isNotBlank(dataSourcePropertyName)) {
                    dataSourcePropertyName = dataSourcePropertyName.toLowerCase();
                }
                dataSourcePropertyMap.put(dataSourcePropertyName, dataSourceProperty.getValue());
            }
            // Loop fill "stop"
            for (Property property : propertyList) {
                // "stop" attribute name
                String name = property.getName();
                property.setStops(stopsById);
                // Judge empty
                if (StringUtils.isBlank(name)) {
                    continue;
                }
                // Go to the map of the "datasource" attribute
                String value = dataSourcePropertyMap.get(name.toLowerCase());
                // Judge empty
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                // Assignment
                property.setCustomValue(value);
                property.setIsLocked(true);
                property.setLastUpdateDttm(new Date());
                property.setLastUpdateUser(username);
            }
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
        }
        stopsById.setProperties(propertyList);
        stopsDomainU.saveOrUpdate(stopsById);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        rtnMap.put("code", 200);
        rtnMap.put("errorMsg", "fill success");

        return JsonUtils.toJsonNoException(rtnMap);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        // Determine if StopId is empty, if it is, then return, otherwise continue
        if (StringUtils.isBlank(stopsId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed, stopId is null");
        }
        // Query Stops by "stopId"
        Stops stopsById = stopsDomainU.getStopsById(stopsId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,Cannot find Stops with id " + stopsId);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("isNeedSource", false);
        if (PortType.NONE == stopsById.getInPortType()) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        rtnMap.put("isNeedSource", true);
        String[] portsArr = stopsById.getInports().split(",");
        if (PortType.ANY != stopsById.getInPortType()) {
            rtnMap.put("ports", portsArr);
            return JsonUtils.toJsonNoException(rtnMap);
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
        return JsonUtils.toJsonNoException(rtnMap);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == runStopsVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("runStopsVo is null");
        }
        if (null == runStopsVo.getStopsId()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("stopsId is null");
        }
        // Get stops information according to stopsId
        Stops stopsById = stopsDomainU.getStopsById(runStopsVo.getStopsId());
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("stops no data");
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
        MxCell mxCellDB = mxCellMapper.getMxCellByMxGraphIdAndPageId(flowMxGraphModel.getId(), stopsById.getPageId());
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
            Integer maxStopPageIdByFlowId = stopsDomainU.getMaxStopPageIdByFlowId(flowDB.getId());
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
        
        processDomainU.addProcess(process);
        process = processDomainU.getProcessById(username, isAdmin, process.getId());

        Map<String, Object> stringObjectMap = flowImpl.startFlow(process, null, RunModeType.RUN);
        if (null == stringObjectMap || 200 != ((Integer) stringObjectMap.get("code"))) {
            process.setEnableFlag(false);
            process.setLastUpdateDttm(new Date());
            process.setLastUpdateUser(username);
            processDomainU.updateProcess(process);
            return ReturnMapUtils.setFailedMsgRtnJsonStr((String) stringObjectMap.get("errorMsg"));
        }
        process.setAppId((String) stringObjectMap.get("appId"));
        process.setProcessId((String) stringObjectMap.get("appId"));
        process.setState(ProcessState.STARTED);
        processDomainU.updateProcess(process);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("save process success,update success");
        rtnMap.put("processId", process.getId());
        return JsonUtils.toJsonNoException(rtnMap);

    }
    
    @Override
    public String checkDatasourceLinked(String datasourceId) {
        if (StringUtils.isBlank(datasourceId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("datasourceId is null");
        }
        List<String> stopsNamesByDatasourceId = stopsDomainU.getStopsNamesByDatasourceId(datasourceId);
        if (null == stopsNamesByDatasourceId || stopsNamesByDatasourceId.size() <= 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("isLinked", false);
        }
        Map<String, Object> setSucceededCustomParam = ReturnMapUtils.setSucceededCustomParam("isLinked", true);
        return ReturnMapUtils.appendValuesToJson(setSucceededCustomParam, "stopsNameList", stopsNamesByDatasourceId);
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
