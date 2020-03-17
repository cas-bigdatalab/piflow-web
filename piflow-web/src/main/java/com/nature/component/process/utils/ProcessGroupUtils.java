package com.nature.component.process.utils;

import com.nature.base.util.SqlUtils;
import com.nature.base.util.SvgUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessParentType;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.*;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.*;
import com.nature.component.process.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessGroupUtils {

    public static ProcessGroup flowGroupToProcessGroup(FlowGroup flowGroup, String username, RunModeType runModeType) {
        ProcessGroup processGroup = new ProcessGroup();
        // copy FlowGroup to ProcessGroup
        BeanUtils.copyProperties(flowGroup, processGroup);
        // set base info
        processGroup.setCrtDttm(new Date());
        processGroup.setCrtUser(username);
        processGroup.setLastUpdateDttm(new Date());
        processGroup.setLastUpdateUser(username);
        processGroup.setEnableFlag(true);
        // Take out the sketchpad information of 'flowgroup'
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        // The 'flowGroup' palette information changes to 'viewXml'
        String viewXml = SvgUtils.mxGraphModelToViewXml(mxGraphModel, true, false);
        // set viewXml
        processGroup.setViewXml(viewXml);
        // set flowGroupId
        processGroup.setFlowId(flowGroup.getId());

        processGroup.setRunModeType(runModeType);
        processGroup.setProcessParentType(ProcessParentType.GROUP);

        // Get the paths information of flow
        List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
        // isEmpty
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            List<ProcessGroupPath> processGroupPathList = new ArrayList<>();
            // Loop paths information
            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                // isEmpty
                if (null != flowGroupPaths) {
                    ProcessGroupPath processGroupPath = new ProcessGroupPath();
                    // Copy flowGroupPaths information into processGroupPath
                    BeanUtils.copyProperties(flowGroupPaths, processGroupPath);
                    // Set basic information
                    processGroupPath.setCrtDttm(new Date());
                    processGroupPath.setCrtUser(username);
                    processGroupPath.setLastUpdateDttm(new Date());
                    processGroupPath.setLastUpdateUser(username);
                    processGroupPath.setEnableFlag(true);
                    // Associated foreign key
                    processGroupPath.setProcessGroup(processGroup);
                    processGroupPathList.add(processGroupPath);
                }
            }
            processGroup.setProcessGroupPathList(processGroupPathList);
        }

        // flow to remove flowGroup
        List<Flow> flowList = flowGroup.getFlowList();
        // flowList isEmpty
        if (null != flowList && flowList.size() > 0) {
            // List of stop of process
            List<Process> processList = new ArrayList<>();
            // Loop flowList
            for (Flow flow : flowList) {
                // isEmpty
                if (null == flow) {
                    continue;
                }

                Process process = new Process();
                // copy flow to process
                BeanUtils.copyProperties(flow, process);
                // set base info
                process.setCrtDttm(new Date());
                process.setCrtUser(username);
                process.setLastUpdateDttm(new Date());
                process.setLastUpdateUser(username);
                process.setEnableFlag(true);
                // Take out flow's Sketchpad information
                MxGraphModel flowMxGraphModel = flow.getMxGraphModel();
                // Flow Sketchpad information to viewxml
                String processViewXml = SvgUtils.mxGraphModelToViewXml(flowMxGraphModel, false, false);
                // set viewXml
                process.setViewXml(processViewXml);
                // set flowId
                process.setFlowId(flow.getId());
                process.setProcessParentType(ProcessParentType.GROUP);
                process.setRunModeType(runModeType);
                // Stops to remove flow
                List<Stops> stopsList = flow.getStopsList();
                // stopsList isEmpty
                if (null != stopsList && stopsList.size() > 0) {
                    // List of stop of process
                    List<ProcessStop> processStopList = new ArrayList<>();
                    // Loop stopsList
                    for (Stops stops : stopsList) {
                        // isEmpty
                        if (null != stops) {
                            ProcessStop processStop = new ProcessStop();
                            // copy stops的信息到processStop中
                            BeanUtils.copyProperties(stops, processStop);
                            // set base info
                            processStop.setCrtDttm(new Date());
                            processStop.setCrtUser(username);
                            processStop.setLastUpdateDttm(new Date());
                            processStop.setLastUpdateUser(username);
                            processStop.setEnableFlag(true);
                            // link foreign key
                            processStop.setProcess(process);
                            // Take out the stops attribute
                            List<Property> properties = stops.getProperties();
                            // Empty attribute of stops
                            if (null != properties && properties.size() > 0) {
                                List<ProcessStopProperty> processStopPropertyList = new ArrayList<>();
                                // Attributes of loop stops
                                for (Property property : properties) {
                                    // isEmpty
                                    if (null != property) {
                                        ProcessStopProperty processStopProperty = new ProcessStopProperty();
                                        // Copy property information into processStopProperty
                                        BeanUtils.copyProperties(property, processStopProperty);
                                        // Set basic information
                                        processStopProperty.setCrtDttm(new Date());
                                        processStopProperty.setCrtUser(username);
                                        processStopProperty.setLastUpdateDttm(new Date());
                                        processStopProperty.setLastUpdateUser(username);
                                        processStopProperty.setEnableFlag(true);
                                        // Associated foreign key
                                        processStopProperty.setProcessStop(processStop);
                                        processStopPropertyList.add(processStopProperty);
                                    }
                                }
                                processStop.setProcessStopPropertyList(processStopPropertyList);
                            }

                            // Take out the custom properties of stops
                            List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
                            // Empty attribute of stops
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
                    List<ProcessPath> processPathList = new ArrayList<>();
                    // Loop paths information
                    for (Paths paths : pathsList) {
                        // isEmpty
                        if (null != paths) {
                            ProcessPath processPath = new ProcessPath();
                            // Copy paths information into processPath
                            BeanUtils.copyProperties(paths, processPath);
                            // Set basic information
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
                process.setProcessGroup(processGroup);
                processList.add(process);
            }
            processGroup.setProcessList(processList);
        }

        List<FlowGroup> flowGroupList = flowGroup.getFlowGroupList();
        if (null != flowGroupList && flowGroupList.size() > 0) {
            // List of stop of process
            List<ProcessGroup> processGroupList = new ArrayList<>();
            // Loop flowGroupList
            for (FlowGroup flowGroupList_i : flowGroupList) {
                ProcessGroup processGroupNew = flowGroupToProcessGroup(flowGroupList_i, username, runModeType);
                if (null != processGroupNew) {
                    processGroupNew.setProcessGroup(processGroup);
                    processGroupList.add(processGroupNew);
                }
            }
            processGroup.setProcessGroupList(processGroupList);
        }

        return processGroup;
    }

    public static List<ProcessGroup> copyProcessGroupList(List<ProcessGroup> processGroupList, ProcessGroup processGroup, UserVo currentUser, RunModeType runModeType) {
        List<ProcessGroup> processGroupListCopy = null;
        if (null != processGroupList && processGroupList.size() > 0) {
            processGroupListCopy = new ArrayList<>();
            for (ProcessGroup processGroup_new : processGroupList) {
                ProcessGroup processGroupCopy = copyProcessGroup(processGroup_new, currentUser, runModeType);
                if (null != processGroupCopy) {
                    processGroupCopy.setProcessGroup(processGroup);
                    processGroupListCopy.add(processGroupCopy);
                }
            }
        }
        return processGroupListCopy;
    }

    public static ProcessGroup copyProcessGroup(ProcessGroup processGroup, UserVo currentUser, RunModeType runModeType) {
        ProcessGroup processGroupCopy = null;
        if (null != currentUser) {
            String username = currentUser.getUsername();
            if (null != processGroup) {
                processGroupCopy = new ProcessGroup();
                processGroupCopy.setCrtUser(username);
                processGroupCopy.setCrtDttm(new Date());
                processGroupCopy.setLastUpdateUser(username);
                processGroupCopy.setLastUpdateDttm(new Date());
                processGroupCopy.setEnableFlag(true);
                processGroupCopy.setState(ProcessState.STARTED);
                processGroupCopy.setRunModeType(null != runModeType ? runModeType : RunModeType.RUN);
                processGroupCopy.setName(processGroup.getName());
                processGroupCopy.setPageId(processGroup.getPageId());
                processGroupCopy.setDescription(processGroup.getDescription());
                processGroupCopy.setViewXml(processGroup.getViewXml());
                processGroupCopy.setFlowId(processGroup.getFlowId());
                processGroupCopy.setParentProcessId(StringUtils.isNotBlank(processGroup.getParentProcessId()) ? processGroup.getParentProcessId() : processGroup.getProcessId());
                processGroupCopy.setProcessParentType(ProcessParentType.GROUP);
                // processGroupPathList
                List<ProcessGroupPath> processGroupPathList = processGroup.getProcessGroupPathList();
                processGroupCopy.setProcessGroupPathList(copyProcessGroupPathList(processGroupPathList, processGroupCopy, username));
                // processList
                List<Process> processList = processGroup.getProcessList();
                processGroupCopy.setProcessList(copyProcessList(processList, currentUser, runModeType, processGroupCopy));
                // processGroupList
                List<ProcessGroup> processGroupList = processGroup.getProcessGroupList();
                processGroup.setProcessGroupList(copyProcessGroupList(processGroupList, processGroupCopy, currentUser, runModeType));
            }
        }
        return processGroupCopy;
    }

    public static List<Process> copyProcessList(List<Process> processList, UserVo currentUser, RunModeType runModeType, ProcessGroup processGroup) {
        List<Process> processListCopy = null;
        if (null != processList && processList.size() > 0) {
            processListCopy = new ArrayList<>();
            for (Process process : processList) {
                Process processCopy = copyProcess(process, currentUser, runModeType, processGroup);
                processListCopy.add(processCopy);
            }
        }
        return processListCopy;
    }

    public static Process copyProcess(Process process, UserVo currentUser, RunModeType runModeType, ProcessGroup processGroup) {
        Process processCopy = null;
        if (null != currentUser) {
            String username = currentUser.getUsername();
            if (StringUtils.isNotBlank(username) && null != process) {
                processCopy = new Process();
                processCopy.setCrtUser(username);
                processCopy.setCrtDttm(new Date());
                processCopy.setLastUpdateUser(username);
                processCopy.setLastUpdateDttm(new Date());
                processCopy.setEnableFlag(true);
                processCopy.setState(ProcessState.INIT);
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
                processCopy.setPageId(process.getPageId());
                processCopy.setProcessGroup(processGroup);
                processCopy.setProcessParentType(ProcessParentType.GROUP);
                List<ProcessPath> processPathList = process.getProcessPathList();
                if (null != processPathList && processPathList.size() > 0) {
                    List<ProcessPath> processPathListCopy = new ArrayList<ProcessPath>();
                    for (ProcessPath processPath : processPathList) {
                        if (null != processPath) {
                            ProcessPath processPathCopy = new ProcessPath();
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
                                List<ProcessStopProperty> processStopPropertyListCopy = new ArrayList<>();
                                for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                                    if (null != processStopProperty) {
                                        ProcessStopProperty processStopPropertyCopy = new ProcessStopProperty();
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

    public static List<ProcessGroupPath> copyProcessGroupPathList(List<ProcessGroupPath> processGroupPathList, ProcessGroup processGroupCopy, String username) {
        List<ProcessGroupPath> processGroupPathListCopy = null;
        if (null != processGroupPathList && processGroupPathList.size() > 0) {
            processGroupPathListCopy = new ArrayList<>();
            for (ProcessGroupPath processGroupPath : processGroupPathList) {
                ProcessGroupPath processGroupPathCopy = copyProcessGroupPath(processGroupPath, processGroupCopy, username);
                if (null != processGroupPathCopy) {
                    processGroupPathListCopy.add(processGroupPathCopy);
                }
            }
            processGroupCopy.setProcessGroupPathList(processGroupPathListCopy);
        }
        return processGroupPathListCopy;
    }

    public static ProcessGroupPath copyProcessGroupPath(ProcessGroupPath processGroupPath, ProcessGroup processGroupCopy, String username) {
        ProcessGroupPath processGroupPathCopy = null;
        if (null != processGroupPath) {
            processGroupPathCopy = new ProcessGroupPath();
            processGroupPathCopy.setCrtDttm(new Date());
            processGroupPathCopy.setCrtUser(username);
            processGroupPathCopy.setLastUpdateDttm(new Date());
            processGroupPathCopy.setLastUpdateUser(username);
            processGroupPathCopy.setEnableFlag(true);
            processGroupPathCopy.setFrom(processGroupPath.getFrom());
            processGroupPathCopy.setTo(processGroupPath.getTo());
            processGroupPathCopy.setInport(processGroupPath.getInport());
            processGroupPathCopy.setOutport(processGroupPath.getOutport());
            processGroupPathCopy.setPageId(processGroupPath.getPageId());
            processGroupPathCopy.setProcessGroup(processGroupCopy);
        }
        return processGroupPathCopy;
    }

    public static ProcessGroupVo processGroupPoToVo(ProcessGroup processGroup) {
        ProcessGroupVo processGroupVo = null;
        if (null != processGroup) {
            processGroupVo = new ProcessGroupVo();
            BeanUtils.copyProperties(processGroup, processGroupVo);
            processGroupVo.setProgress(StringUtils.isNotBlank(processGroup.getProgress()) ? processGroup.getProgress() : "0.00");
            List<Process> processList = processGroup.getProcessList();
            if (null != processList && processList.size() > 0) {
                List<ProcessVo> processVoList = new ArrayList<>();
                for (Process process : processList) {
                    ProcessVo processVo = ProcessUtils.processPoToVo(process);
                    if (null != processVo) {
                        processVo.setState(process.getState());
                        processVoList.add(processVo);
                    }
                }
                processGroupVo.setProcessVoList(processVoList);
            }
            List<ProcessGroupPath> processGroupPathList = processGroup.getProcessGroupPathList();
            if (null != processGroupPathList && processGroupPathList.size() > 0) {
                List<ProcessGroupPathVo> processGroupPathVoList = new ArrayList<>();
                for (ProcessGroupPath processGroupPath : processGroupPathList) {
                    if (null != processGroupPath) {
                        ProcessGroupPathVo processGroupPathVo = new ProcessGroupPathVo();
                        BeanUtils.copyProperties(processGroupPath, processGroupPathVo);
                        processGroupPathVoList.add(processGroupPathVo);
                    }
                }
                processGroupVo.setProcessGroupPathVoList(processGroupPathVoList);
            }
            List<ProcessGroup> processGroupList = processGroup.getProcessGroupList();
            if (null != processGroupList && processGroupList.size() > 0) {
                List<ProcessGroupVo> processGroupVoList = new ArrayList<>();
                for (ProcessGroup processGroup_i : processGroupList) {
                    ProcessGroupVo processGroupVo_copy = processGroupPoToVo(processGroup_i);
                    if (null != processGroupVo_copy) {
                        processGroupVoList.add(processGroupVo_copy);
                    }
                }
                processGroupVo.setProcessGroupVoList(processGroupVoList);
            }
        }
        return processGroupVo;
    }


}
