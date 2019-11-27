package com.nature.component.flow.service.impl;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.StatefulRtnBaseUtils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.*;
import com.nature.component.flow.service.IPathsService;
import com.nature.component.flow.service.IStopsService;
import com.nature.component.flow.utils.StopsUtil;
import com.nature.component.flow.vo.PathsVo;
import com.nature.component.flow.vo.StopsVo;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.mapper.flow.PathsMapper;
import com.nature.mapper.flow.StopsMapper;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StopsServiceImpl implements IStopsService {

    @Autowired
    private StopsMapper stopsMapper;

    @Autowired
    private MxCellMapper mxCellMapper;

    @Autowired
    private PathsMapper pathsMapper;

    Logger logger = LoggerUtil.getLogger();

    @Override
    public int deleteStopsByFlowId(String id) {
        return stopsMapper.updateEnableFlagByFlowId(id);
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
        List<StopsVo> stopsVoList = StopsUtil.stopsListPoToVo(stopsList);
        return stopsVoList;
    }

    @Override
    public Integer stopsUpdate(StopsVo stopsVo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        if (null != stopsVo) {
            Stops stopsById = stopsMapper.getStopsById(stopsVo.getId());
            if (null != stopsById) {
                BeanUtils.copyProperties(stopsVo, stopsById);
                stopsById.setLastUpdateDttm(new Date());
                stopsById.setLastUpdateUser(username);
                int i = stopsMapper.updateStops(stopsById);
                return i;
            }
        }
        return 0;
    }

    @Override
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo) {
        return stopsMapper.updateStopsByFlowIdAndName(stopVo);
    }

    /**
     * Modify the isCheckpoint field
     *
     * @param stopId
     * @param isCheckpoint
     * @return
     */
    @Override
    public int updateStopsCheckpoint(String stopId, boolean isCheckpoint) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        if (StringUtils.isNotBlank(stopId)) {
            Stops stopsById = stopsMapper.getStopsById(stopId);
            if (null != stopsById) {
                stopsById.setLastUpdateUser(username);
                stopsById.setLastUpdateDttm(new Date());
                stopsById.setIsCheckpoint(isCheckpoint);
                return stopsMapper.updateStops(stopsById);
            }
        }
        return 0;
    }

    @Override
    public int updateStopsNameById(String id, String stopName) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(stopName)) {
            Stops stopsById = stopsMapper.getStopsById(id);
            if (null != stopsById) {
                stopsById.setLastUpdateUser(username);
                stopsById.setLastUpdateDttm(new Date());
                stopsById.setName(stopName);
                return stopsMapper.updateStops(stopsById);
            }
        }
        return 0;
    }

    @Override
    public String getStopByNameAndFlowId(String flowId, String stopName) {
        return stopsMapper.getStopByNameAndFlowId(flowId, stopName);
    }

    @Override
    public StatefulRtnBase updateStopName(String stopId, Flow flowById, String stopName, String pageId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        List<MxCell> root = null;
        if (null != flowById) {
            MxGraphModel mxGraphModel = flowById.getMxGraphModel();
            if (null != mxGraphModel) {
                root = mxGraphModel.getRoot();
            }
        }
        if (null == root && root.size() == 0) {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No flow information,update failed ");
            logger.info(flowById.getId() + "The artboard information is empty and the update failed.");
            return statefulRtnBase;
        }
        //Check if name is the same name
        String checkResult = this.getStopByNameAndFlowId(flowById.getId(), stopName);
        if (StringUtils.isNotBlank(checkResult)) {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Name already exists");
            logger.info(stopName + "The name has been repeated and the save failed.");
        } else {
            int updateStopsNameById = this.updateStopsNameById(stopId, stopName);
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
        Map sourcePortUsageMap = stopPortUsage(true, flowId, sourceStop, pathLineId);
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
        Map targetPortUsageMap = stopPortUsage(false, flowId, targetStop, pathLineId);
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
    private Map stopPortUsage(boolean isSourceStop, String flowId, Stops stop, String pathLineId) {
        // Map for return
        // Map content
        // 1, key: 'stop' port type enumeration 'text' attribute, value: port can use the quantity ‘Integer’
        // 2, key : 'isSourceStop', value : whether it is 'source'
        // 3, key: 'portUsageMap', value: port details 'map' (key: port name, value: whether available 'boolean')
        Map<String, Object> stopPortUsageMap = null;
        if (null != stop) {
            stopPortUsageMap = new HashMap<String, Object>();
            // Number of used ports
            Integer usedPathsCounts = 0;
            // Number of available ports
            Integer availablePathsCounts = 0;
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
    private Map portStrToMap(Map<String, Boolean> portUsageMap, Boolean isSourceStop, List<Paths> usedPathsList, String stopVoPorts) {
        if (null != portUsageMap) {
            if (null != usedPathsList && usedPathsList.size() > 0) {
                String currentPathsId = "";
                Paths currentPaths = null;//pathsServiceImpl.getPathsByFlowIdAndPageId(flowId, pathLineId);
                if (null != currentPaths) {
                    currentPathsId = currentPaths.getId();
                }
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

}
