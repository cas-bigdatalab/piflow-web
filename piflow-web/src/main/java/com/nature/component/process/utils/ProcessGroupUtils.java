package com.nature.component.process.utils;

import com.nature.base.util.SqlUtils;
import com.nature.base.util.SvgUtils;
import com.nature.base.vo.UserVo;
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

    public static ProcessGroup flowGroupToProcessGroup(FlowGroup flowGroup, UserVo user) {
        ProcessGroup processGroup = null;
        if (null != flowGroup) {
            String username = (null != user) ? user.getUsername() : "-1";
            processGroup = new ProcessGroup();
            // copy flowGroup信息到processGroup
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
            // flow to remove flowGroup
            List<Flow> flowList = flowGroup.getFlowList();
            // flowList isEmpty
            if (null != flowList && flowList.size() > 0) {
                // List of stop of process
                List<Process> processList = new ArrayList<>();
                // Loop flowList
                for (Flow flow : flowList) {
                    // isEmpty
                    if (null != flow) {
                        Process process = ProcessUtils.flowToProcess(flow, user);
                        process.setProcessGroup(processGroup);
                        processList.add(process);
                    }
                }
                processGroup.setProcessList(processList);
            }
            // Get the paths information of flow
            List<FlowGroupPaths> pathsList = flowGroup.getFlowGroupPathsList();
            // isEmpty
            if (null != pathsList && pathsList.size() > 0) {
                List<ProcessGroupPath> processGroupPathList = new ArrayList<>();
                // Loop paths information
                for (FlowGroupPaths flowGroupPaths : pathsList) {
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
