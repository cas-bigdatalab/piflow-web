package com.nature.component.process.utils;

import com.nature.base.util.SqlUtils;
import com.nature.base.util.SvgUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessParentType;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.dataSource.model.DataSource;
import com.nature.component.dataSource.model.DataSourceProperty;
import com.nature.component.flow.model.*;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.process.model.*;
import com.nature.component.process.model.Process;
import com.nature.component.process.vo.ProcessPathVo;
import com.nature.component.process.vo.ProcessStopPropertyVo;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.component.process.vo.ProcessVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class ProcessUtils {

    public static ProcessVo processOnePoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setCrtDttm(process.getCrtDttm());
        }
        return processVo;
    }

    public static ProcessVo processPoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            List<ProcessStop> processStopList = process.getProcessStopList();
            List<ProcessPath> processPathList = process.getProcessPathList();
            if (CollectionUtils.isNotEmpty(processStopList)) {
                List<ProcessStopVo> processStopVos = processStopListPoToVo(processStopList);
                if (CollectionUtils.isNotEmpty(processStopVos)) {
                    processVo.setProcessStopVoList(processStopVos);
                }
            }
            if (CollectionUtils.isNotEmpty(processPathList)) {
                List<ProcessPathVo> processPathVos = processPathListPoToVo(processPathList);
                if (CollectionUtils.isNotEmpty(processPathVos)) {
                    processVo.setProcessPathVoList(processPathVos);
                }
            }
        }
        return processVo;
    }

    public static List<ProcessStopVo> processStopListPoToVo(List<ProcessStop> processStopList) {
        List<ProcessStopVo> processStopVoList = null;
        if (null != processStopList && processStopList.size() > 0) {
            processStopVoList = new ArrayList<ProcessStopVo>();
            for (ProcessStop processStop : processStopList) {
                ProcessStopVo processStopVo = processStopPoToVo(processStop);
                if (null != processStopVo) {
                    processStopVoList.add(processStopVo);
                }
            }
        }
        return processStopVoList;
    }

    public static ProcessStopVo processStopPoToVo(ProcessStop processStop) {
        ProcessStopVo processStopVo = null;
        if (null != processStop) {
            processStopVo = new ProcessStopVo();
            BeanUtils.copyProperties(processStop, processStopVo);
            processStopVo.setState((null != processStop.getState() ? processStop.getState().name() : ""));
            List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
            if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                List<ProcessStopPropertyVo> processStopPropertyVoList = new ArrayList<ProcessStopPropertyVo>();
                for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                    if (null != processStopProperty) {
                        ProcessStopPropertyVo processStopPropertyVo = new ProcessStopPropertyVo();
                        BeanUtils.copyProperties(processStopProperty, processStopPropertyVo);
                        processStopPropertyVoList.add(processStopPropertyVo);
                    }
                }
                processStopVo.setProcessStopPropertyVoList(processStopPropertyVoList);
            }
        }
        return processStopVo;
    }

    public static List<ProcessPathVo> processPathListPoToVo(List<ProcessPath> processPathList) {
        List<ProcessPathVo> processPathVoList = null;
        if (null != processPathList && processPathList.size() > 0) {
            processPathVoList = new ArrayList<ProcessPathVo>();
            for (ProcessPath processPath : processPathList) {
                ProcessPathVo processPathVo = processPathPoToVo(processPath);
                if (null != processPathVo) {
                    processPathVoList.add(processPathVo);
                }
            }
        }
        return processPathVoList;
    }

    public static ProcessPathVo processPathPoToVo(ProcessPath processPath) {
        ProcessPathVo processPathVo = null;
        if (null != processPath) {
            processPathVo = new ProcessPathVo();
            BeanUtils.copyProperties(processPath, processPathVo);
        }
        return processPathVo;
    }

    public static Process flowToProcess(Flow flow, UserVo user) {
        Process process = null;
        if (null != flow) {
            String username = (null != user) ? user.getUsername() : "-1";
            process = new Process();
            // Copy flow information to process
            BeanUtils.copyProperties(flow, process);
            // Set basic information
            process.setId(SqlUtils.getUUID32());
            process.setCrtDttm(new Date());
            process.setCrtUser(username);
            process.setLastUpdateDttm(new Date());
            process.setLastUpdateUser(username);
            process.setEnableFlag(true);
            FlowGroup flowGroup = flow.getFlowGroup();
            //Set default
            process.setProcessParentType(ProcessParentType.PROCESS);
            if (null != flowGroup) {
                FlowProject flowProject = flowGroup.getFlowProject();
                if (null != flowProject) {
                    process.setProcessParentType(ProcessParentType.PROJECT);
                } else {
                    process.setProcessParentType(ProcessParentType.GROUP);
                }
            }
            // Take out the flow board information of the flow
            MxGraphModel mxGraphModel = flow.getMxGraphModel();
            // Flow artboard information is converted to ViewXml
            String viewXml = SvgUtils.mxGraphModelToViewXml(mxGraphModel, false, false);
            // set viewXml
            process.setViewXml(viewXml);
            // set flowId
            process.setFlowId(flow.getId());
            // Stops to remove flow
            List<Stops> stopsList = flow.getStopsList();
            // stopsList isEmpty
            if (null != stopsList && stopsList.size() > 0) {
                // List of stop of process
                List<ProcessStop> processStopList = new ArrayList<ProcessStop>();
                // Loop stopsList
                for (Stops stops : stopsList) {
                    // isEmpty
                    if (null != stops) {
                        ProcessStop processStop = new ProcessStop();
                        // Copy stops information into processStop
                        BeanUtils.copyProperties(stops, processStop);
                        // Set basic information
                        processStop.setId(SqlUtils.getUUID32());
                        processStop.setCrtDttm(new Date());
                        processStop.setCrtUser(username);
                        processStop.setLastUpdateDttm(new Date());
                        processStop.setLastUpdateUser(username);
                        processStop.setEnableFlag(true);
                        // Associate foreign key
                        processStop.setProcess(process);
                        // Remove the properties of stops
                        List<Property> properties = stops.getProperties();
                        // Determine if the stops attribute is empty
                        if (null != properties && properties.size() > 0) {
                            List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();
                            Map<String, String> dataSourcePropertyMap = new HashMap<>();
                            DataSource dataSource = stops.getDataSource();
                            if (null != dataSource) {
                                List<DataSourceProperty> dataSourcePropertyList = dataSource.getDataSourcePropertyList();
                                if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
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
                                }
                            }
                            // Attributes of loop stops
                            for (Property property : properties) {
                                // isEmpty
                                if (null != property) {
                                    ProcessStopProperty processStopProperty = new ProcessStopProperty();
                                    // Copy property information into processStopProperty
                                    BeanUtils.copyProperties(property, processStopProperty);
                                    // Set basic information
                                    processStopProperty.setId(SqlUtils.getUUID32());
                                    processStopProperty.setCrtDttm(new Date());
                                    processStopProperty.setCrtUser(username);
                                    processStopProperty.setLastUpdateDttm(new Date());
                                    processStopProperty.setLastUpdateUser(username);
                                    processStopProperty.setEnableFlag(true);
                                    // "stop" attribute name
                                    String name = property.getName();
                                    // Judge empty
                                    if (StringUtils.isNotBlank(name)) {
                                        // Go to the map of the "datasource" attribute
                                        String value = dataSourcePropertyMap.get(name.toLowerCase());
                                        // Judge empty
                                        if (StringUtils.isNotBlank(value)) {
                                            // Assignment
                                            processStopProperty.setCustomValue(value);
                                        }
                                    }
                                    // Associated foreign key
                                    processStopProperty.setProcessStop(processStop);
                                    processStopPropertyList.add(processStopProperty);
                                }
                            }
                            processStop.setProcessStopPropertyList(processStopPropertyList);
                        }

                        // Take out the custom properties of stops
                        List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
                        // Determine if the stops attribute is empty
                        if (null != customizedPropertyList && customizedPropertyList.size() > 0) {
                            List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = new ArrayList<>();
                            // Attributes of loop stops
                            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                                // isEmpty
                                if (null != customizedProperty) {
                                    ProcessStopCustomizedProperty processStopCustomizedProperty = new ProcessStopCustomizedProperty();
                                    // Copy customizedProperty information into processStopCustomizedProperty
                                    BeanUtils.copyProperties(customizedProperty, processStopCustomizedProperty);
                                    // Set basic information
                                    processStopCustomizedProperty.setId(SqlUtils.getUUID32());
                                    processStopCustomizedProperty.setCrtDttm(new Date());
                                    processStopCustomizedProperty.setCrtUser(username);
                                    processStopCustomizedProperty.setLastUpdateDttm(new Date());
                                    processStopCustomizedProperty.setLastUpdateUser(username);
                                    processStopCustomizedProperty.setEnableFlag(true);
                                    // Associated foreign key
                                    processStopCustomizedProperty.setProcessStop(processStop);
                                    processStopCustomizedPropertyList.add(processStopCustomizedProperty);
                                }
                            }
                            processStop.setProcessStopCustomizedPropertyList(processStopCustomizedPropertyList);
                        }
                        processStopList.add(processStop);
                    }
                }
                process.setProcessStopList(processStopList);
            }
            // Get the paths information of flow
            List<Paths> pathsList = flow.getPathsList();
            // isEmpty
            if (null != pathsList && pathsList.size() > 0) {
                List<ProcessPath> processPathList = new ArrayList<ProcessPath>();
                // Loop paths information
                for (Paths paths : pathsList) {
                    // isEmpty
                    if (null != paths) {
                        ProcessPath processPath = new ProcessPath();
                        // Copy paths information into processPath
                        BeanUtils.copyProperties(paths, processPath);
                        // Set basic information
                        processPath.setId(SqlUtils.getUUID32());
                        processPath.setCrtDttm(new Date());
                        processPath.setCrtUser(username);
                        processPath.setLastUpdateDttm(new Date());
                        processPath.setLastUpdateUser(username);
                        processPath.setEnableFlag(true);
                        // Associated foreign key
                        processPath.setProcess(process);
                        processPathList.add(processPath);
                    }
                }
                process.setProcessPathList(processPathList);
            }
        }
        return process;
    }

    public static Process copyProcessAndNew(Process process, UserVo currentUser, RunModeType runModeType) {
        Process processCopy = null;
        if (null != currentUser) {
            String username = currentUser.getUsername();
            if (StringUtils.isNotBlank(username) && null != process) {
                processCopy = new Process();
                processCopy.setId(SqlUtils.getUUID32());
                processCopy.setCrtUser(username);
                processCopy.setCrtDttm(new Date());
                processCopy.setLastUpdateUser(username);
                processCopy.setLastUpdateDttm(new Date());
                processCopy.setEnableFlag(true);
                processCopy.setState(ProcessState.STARTED);
                processCopy.setRunModeType(null != runModeType ? runModeType : RunModeType.RUN);
                processCopy.setName(process.getName());
                processCopy.setDriverMemory(process.getDriverMemory());
                processCopy.setExecutorNumber(process.getExecutorNumber());
                processCopy.setExecutorMemory(process.getExecutorMemory());
                processCopy.setExecutorCores(process.getExecutorCores());
                processCopy.setDescription(process.getDescription());
                processCopy.setViewXml(process.getViewXml());
                processCopy.setFlowId(process.getFlowId());
                processCopy.setParentProcessId(StringUtils.isNotBlank(process.getParentProcessId()) ? process.getParentProcessId() : process.getProcessId());
                processCopy.setProcessParentType(ProcessParentType.PROCESS);
                ProcessGroup processGroup = process.getProcessGroup();
                if (null != processGroup) {
                    processCopy.setProcessParentType(ProcessParentType.GROUP);
                }
                List<ProcessPath> processPathList = process.getProcessPathList();
                if (null != processPathList && processPathList.size() > 0) {
                    List<ProcessPath> processPathListCopy = new ArrayList<ProcessPath>();
                    for (ProcessPath processPath : processPathList) {
                        if (null != processPath) {
                            ProcessPath processPathCopy = new ProcessPath();
                            processPathCopy.setId(SqlUtils.getUUID32());
                            processPathCopy.setCrtDttm(new Date());
                            processPathCopy.setCrtUser(username);
                            processPathCopy.setLastUpdateDttm(new Date());
                            processPathCopy.setLastUpdateUser(username);
                            processPathCopy.setEnableFlag(true);
                            processPathCopy.setFrom(processPath.getFrom());
                            processPathCopy.setTo(processPath.getTo());
                            processPathCopy.setInport(processPath.getInport());
                            processPathCopy.setOutport(processPath.getOutport());
                            processPathCopy.setPageId(processPath.getPageId());
                            processPathCopy.setProcess(processCopy);
                            processPathListCopy.add(processPathCopy);
                        }
                    }
                    processCopy.setProcessPathList(processPathListCopy);
                }
                List<ProcessStop> processStopList = process.getProcessStopList();
                if (null != processStopList && processStopList.size() > 0) {
                    List<ProcessStop> processStopListCopy = new ArrayList<ProcessStop>();
                    for (ProcessStop processStop : processStopList) {
                        if (null != processStop) {
                            ProcessStop processStopCopy = new ProcessStop();
                            processStopCopy.setId(SqlUtils.getUUID32());
                            processStopCopy.setCrtDttm(new Date());
                            processStopCopy.setCrtUser(username);
                            processStopCopy.setLastUpdateDttm(new Date());
                            processStopCopy.setLastUpdateUser(username);
                            processStopCopy.setEnableFlag(true);
                            processStopCopy.setBundel(processStop.getBundel());
                            processStopCopy.setName(processStop.getName());
                            processStopCopy.setDescription(processStop.getDescription());
                            processStopCopy.setGroups(processStop.getGroups());
                            processStopCopy.setInports(processStop.getInports());
                            processStopCopy.setInPortType(processStop.getInPortType());
                            processStopCopy.setOutports(processStop.getOutports());
                            processStopCopy.setOutPortType(processStop.getOutPortType());
                            processStopCopy.setOwner(processStop.getOwner());
                            processStopCopy.setPageId(processStop.getPageId());
                            processStopCopy.setProcess(processCopy);
                            List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                            if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                                List<ProcessStopProperty> processStopPropertyListCopy = new ArrayList<ProcessStopProperty>();
                                for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                                    if (null != processStopProperty) {
                                        ProcessStopProperty processStopPropertyCopy = new ProcessStopProperty();
                                        processStopPropertyCopy.setId(SqlUtils.getUUID32());
                                        processStopPropertyCopy.setCrtDttm(new Date());
                                        processStopPropertyCopy.setCrtUser(username);
                                        processStopPropertyCopy.setLastUpdateDttm(new Date());
                                        processStopPropertyCopy.setLastUpdateUser(username);
                                        processStopPropertyCopy.setEnableFlag(true);
                                        processStopPropertyCopy.setCustomValue(processStopProperty.getCustomValue());
                                        processStopPropertyCopy.setName(processStopProperty.getName());
                                        processStopPropertyCopy.setAllowableValues(processStopProperty.getAllowableValues());
                                        processStopPropertyCopy.setDescription(processStopProperty.getDescription());
                                        processStopPropertyCopy.setDisplayName(processStopProperty.getDisplayName());
                                        processStopPropertyCopy.setRequired(processStopProperty.getRequired());
                                        processStopPropertyCopy.setSensitive(processStopPropertyCopy.getSensitive());
                                        processStopPropertyCopy.setProcessStop(processStopCopy);
                                        processStopPropertyListCopy.add(processStopPropertyCopy);
                                    }
                                }
                                processStopCopy.setProcessStopPropertyList(processStopPropertyListCopy);
                            }
                            processStopListCopy.add(processStopCopy);
                        }
                    }
                    processCopy.setProcessStopList(processStopListCopy);
                }
            }
        }
        return processCopy;
    }

}
