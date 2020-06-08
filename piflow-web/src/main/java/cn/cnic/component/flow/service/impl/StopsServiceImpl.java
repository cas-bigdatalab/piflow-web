package cn.cnic.component.flow.service.impl;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.StatefulRtnBaseUtils;
import cn.cnic.base.vo.StatefulRtnBase;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.dataSource.model.DataSource;
import cn.cnic.component.dataSource.model.DataSourceProperty;
import cn.cnic.component.flow.model.*;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.mxGraph.model.MxCell;
import cn.cnic.component.mxGraph.model.MxGraphModel;
import cn.cnic.domain.flow.StopsDomain;
import cn.cnic.mapper.flow.PathsMapper;
import cn.cnic.mapper.flow.StopsMapper;
import cn.cnic.mapper.mxGraph.MxCellMapper;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StopsServiceImpl implements IStopsService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private StopsMapper stopsMapper;

    @Resource
    private MxCellMapper mxCellMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private DataSourceDomain dataSourceDomain;

    @Resource
    private StopsDomain stopsDomain;

    @Override
    public int deleteStopsByFlowId(String id) {
        if (StringUtils.isBlank(id)) {
            return 0;
        }
        Stops stopsById = stopsDomain.getStopsById(id);
        if (null == stopsById) {
            return 0;
        }
        stopsById.setEnableFlag(false);
        stopsDomain.saveOrUpdate(stopsById);
        return 1;
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
        List<Stops> stopsList = stopsMapper.getStopsListByFlowIdAndPageIds(flowId, pageIds);
        List<StopsVo> stopsVoList = StopsUtils.stopsListPoToVo(stopsList);
        return stopsVoList;
    }

    @Override
    public Integer stopsUpdate(String username, StopsVo stopsVo) {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        if (null == stopsVo) {
            return 0;
        }
        Stops stopsById = stopsMapper.getStopsById(stopsVo.getId());
        if (null == stopsById) {
            return 0;
        }
        BeanUtils.copyProperties(stopsVo, stopsById);
        stopsById.setLastUpdateDttm(new Date());
        stopsById.setLastUpdateUser(username);
        return stopsMapper.updateStops(stopsById);
    }

    @Override
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo) {
        return stopsMapper.updateStopsByFlowIdAndName(stopVo);
    }

    /**
     * Modify the isCheckpoint field
     *
     * @param stopId
     * @param isCheckpointStr
     * @return
     */
    @Override
    public String updateStopsCheckpoint(String username, String stopId, String isCheckpointStr) {

        if (!StringUtils.isAnyEmpty(stopId, isCheckpointStr)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Partial incoming parameters are empty");
        }
        Stops stopsById = stopsMapper.getStopsById(stopId);
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
        int updateStopsCheckpoint = stopsMapper.updateStops(stopsById);
        if (updateStopsCheckpoint > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Saved successfully");
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Database save failed");
        }

    }

    @Override
    public int updateStopsNameById(String username, String id, String stopName) {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        if (StringUtils.isBlank(id) || StringUtils.isBlank(stopName)) {
            return 0;
        }
        Stops stopsById = stopsMapper.getStopsById(id);
        if (null == stopsById) {
            return 0;
        }
        stopsById.setLastUpdateUser(username);
        stopsById.setLastUpdateDttm(new Date());
        stopsById.setName(stopName);
        return stopsMapper.updateStops(stopsById);
    }

    @Override
    public String getStopByNameAndFlowId(String flowId, String stopName) {
        return stopsMapper.getStopByNameAndFlowId(flowId, stopName);
    }

    @Override
    public StatefulRtnBase updateStopName(String username, String stopId, Flow flowById, String stopName, String pageId) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        if (StringUtils.isBlank(username)) {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Illegal users");
            logger.info("Illegal users");
            return statefulRtnBase;
        }
        List<MxCell> root = null;
        if (null != flowById) {
            MxGraphModel mxGraphModel = flowById.getMxGraphModel();
            if (null != mxGraphModel) {
                root = mxGraphModel.getRoot();
            }
        }
        if (null == root || root.size() == 0) {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No flow information,update failed ");
            logger.info(flowById.getId() + "The drawing board information is empty and the update failed.");
            return statefulRtnBase;
        }
        //Check if name is the same name
        String checkResult = this.getStopByNameAndFlowId(flowById.getId(), stopName);
        if (StringUtils.isNotBlank(checkResult)) {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Name already exists");
            logger.info(stopName + "The name has been repeated and the save failed.");
        } else {
            int updateStopsNameById = this.updateStopsNameById(username, stopId, stopName);
            if (updateStopsNameById > 0) {
                for (MxCell mxCell : root) {
                    if (null != mxCell) {
                        if (mxCell.getPageId().equals(pageId)) {
                            mxCell.setValue(stopName);
                            mxCell.setLastUpdateDttm(new Date());
                            mxCell.setLastUpdateUser(username);
                            int updateMxCell = mxCellMapper.updateMxCell(mxCell);
                            if (updateMxCell > 0) {
                                logger.info("Successfully modified");
                                statefulRtnBase = StatefulRtnBaseUtils.setSuccessdMsg("Update success");
                            }
                        }
                    }
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Update failed");
                logger.info("Modify stopName failed");
            }
        }
        return statefulRtnBase;
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
        List<Stops> queryInfoList = stopsMapper.getStopsListByFlowIdAndPageIds(flowId, new String[]{sourceId, targetId});
        // If 'queryInfoList' is empty, or the size of 'queryInfoList' is less than 2, return directly
        if (null == queryInfoList || queryInfoList.size() < 2) {
            logger.warn("Can't find 'source' or 'target'");
            rtnMap.put("errorMsg", "Can't find 'source' or 'target'");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        Stops sourceStop = null;
        Stops targetStop = null;
        //Loop out 'sourceStop' and 'targetStop'
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
     * 1, key: 'stop' port type enumeration 'text' attribute, value: port can use the quantity ‘Integer’
     * 2, key : 'isSourceStop', value : whether it is 'source'
     * 3, key: 'portUsageMap', value: port details 'map' (key: port name, value: whether available 'boolean')
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
     * @param usedPathsList The information of the line queried by the database represents the port that has been used (excluding the port used by the current line)
     * @param stopVoPorts   Port information in stop, all ports
     * @return
     */
    private Map<String, Boolean> portStrToMap(Map<String, Boolean> portUsageMap, Boolean isSourceStop, List<Paths> usedPathsList, String stopVoPorts) {
        if (null != portUsageMap) {
            if (null != usedPathsList && usedPathsList.size() > 0) {
                String currentPathsId = "";
                /*
                Paths currentPaths = null;
                // Paths currentPaths = pathsServiceImpl.getPathsByFlowIdAndPageId(flowId, pathLineId);
                if (null != currentPaths) {
                    currentPathsId = currentPaths.getId();
                }
                */
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

    @Override
    @Transactional
    public String fillDatasource(String username, String dataSourceId, String stopId) {

        // Get current user
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        // Determine if StopId is empty, if it is, then return, otherwise continue
        if (StringUtils.isBlank(stopId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("fill failed,stopId is null");
        }
        // Query Stops by "stopId"
        Stops stopsById = stopsDomain.getStopsById(stopId);
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
                // Judge empty
                if (StringUtils.isNotBlank(name)) {
                    // Go to the map of the "datasource" attribute
                    String value = dataSourcePropertyMap.get(name.toLowerCase());
                    // Judge empty
                    if (StringUtils.isNotBlank(value)) {
                        // Assignment
                        property.setCustomValue(value);
                        property.setIsLocked(true);
                        property.setLastUpdateDttm(new Date());
                        property.setLastUpdateUser(username);
                    }
                }
            }
        } else {
            // Loop fill "stop"
            for (Property property : propertyList) {
                // "stop" attribute isSelect
                Boolean isLocked = property.getIsLocked();
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
        stopsDomain.saveOrUpdate(stopsById);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        rtnMap.put("code", 200);
        rtnMap.put("errorMsg", "fill success");

        return JsonUtils.toJsonNoException(rtnMap);
    }

}
