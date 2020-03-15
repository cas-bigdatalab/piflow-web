package com.nature.component.process.utils;

import com.nature.base.util.SqlUtils;
import com.nature.base.util.SvgUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessParentType;
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
        processGroup.setId(SqlUtils.getUUID32());
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
                    processGroupPath.setId(SqlUtils.getUUID32());
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
                process.setId(SqlUtils.getUUID32());
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
                            processStop.setId(SqlUtils.getUUID32());
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
                                        processStopProperty.setId(SqlUtils.getUUID32());
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

                            // 取出stops的自定义属性
                            List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
                            // stops的属性判空
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
                    List<ProcessPath> processPathList = new ArrayList<>();
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
                    processGroupList.add(processGroupNew);
                }
            }
            processGroup.setProcessGroupList(processGroupList);
        }

        return processGroup;
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
        }
        return processGroupVo;
    }


}
