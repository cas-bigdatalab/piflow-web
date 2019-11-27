package com.nature.third.utils;

import com.nature.base.util.DateUtils;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.StopState;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessGroup;
import com.nature.component.process.model.ProcessStop;
import com.nature.third.vo.flowGroup.*;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdFlowGroupInfoResponseUtils {

    public static ProcessGroup setProcessGroup(ProcessGroup processGroup, ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse) {

        if (null != thirdFlowGroupInfoResponse && null != processGroup) {
            Map<String, ThirdFlowInfoResponse> flowInfoResponseMap = new HashMap<>();
            List<ThirdFlowInfoOutResponse> flows = thirdFlowGroupInfoResponse.getFlows();
            processGroup.setLastUpdateUser("syncTask");
            processGroup.setLastUpdateDttm(new Date());
            processGroup.setProgress(thirdFlowGroupInfoResponse.getProgress());
            if (StringUtils.isNotBlank(thirdFlowGroupInfoResponse.getState())) {
                processGroup.setState(ProcessState.selectGender(thirdFlowGroupInfoResponse.getState()));
            }
            //process.setProcessId(thirdFlowGroupInfoResponse.getPid());
            processGroup.setProcessId(thirdFlowGroupInfoResponse.getId());
            processGroup.setName(thirdFlowGroupInfoResponse.getName());
            processGroup.setStartTime(DateUtils.strCstToDate(thirdFlowGroupInfoResponse.getStartTime()));
            processGroup.setEndTime(DateUtils.strCstToDate(thirdFlowGroupInfoResponse.getEndTime()));
            List<Process> processList = processGroup.getProcessList();
            if (CollectionUtils.isNotEmpty(flows) && CollectionUtils.isNotEmpty(processList)) {
                for (ThirdFlowInfoOutResponse thirdFlowInfoOutResponse : flows) {
                    if (null != thirdFlowInfoOutResponse) {
                        ThirdFlowInfoResponse flowInfoResponse = thirdFlowInfoOutResponse.getFlow();
                        if (null != flowInfoResponse) {
                            flowInfoResponseMap.put(flowInfoResponse.getName(), flowInfoResponse);
                        }
                    }
                }
                for (Process process : processList) {
                    if (null != process) {
                        ThirdFlowInfoResponse flowInfoResponse = flowInfoResponseMap.get(process.getName());
                        if (null != flowInfoResponse) {
                            setProcess(process, flowInfoResponse);
                        }
                    }
                }
            }
        }
        return processGroup;
    }

    public static Process setProcess(Process process, ThirdFlowInfoResponse flowInfoResponse) {

        if (null != flowInfoResponse && null != process) {
            Map<String, ThirdFlowStopInfoResponse> stopsMap = new HashMap<>();
            List<ThirdFlowStopInfoOutResponse> stops = flowInfoResponse.getStops();
            process.setLastUpdateUser("syncTask");
            process.setLastUpdateDttm(new Date());
            process.setProgress(flowInfoResponse.getProgress());
            if(StringUtils.isNotBlank(flowInfoResponse.getState())){
                process.setState(ProcessState.selectGender(flowInfoResponse.getState()));
            }
            //process.setProcessId(flowInfoResponse.getPid());
            process.setProcessId(flowInfoResponse.getId());
            process.setAppId(flowInfoResponse.getId());
            process.setName(flowInfoResponse.getName());
            process.setStartTime(DateUtils.strCstToDate(flowInfoResponse.getStartTime()));
            process.setEndTime(DateUtils.strCstToDate(flowInfoResponse.getEndTime()));
            List<ProcessStop> processStopList = process.getProcessStopList();
            if (CollectionUtils.isNotEmpty(stops) && CollectionUtils.isNotEmpty(processStopList)) {
                for (ThirdFlowStopInfoOutResponse thirdFlowStopInfoOutResponse : stops) {
                    if (null != thirdFlowStopInfoOutResponse) {
                        ThirdFlowStopInfoResponse stopInfoResponse = thirdFlowStopInfoOutResponse.getStop();
                        if (null != stopInfoResponse) {
                            stopsMap.put(stopInfoResponse.getName(), stopInfoResponse);
                        }
                    }
                }
                for (ProcessStop processStop : processStopList) {
                    if (null != processStop) {
                        ThirdFlowStopInfoResponse stopInfoResponse = stopsMap.get(processStop.getName());
                        if (null != stopInfoResponse) {
                            processStop.setState(StopState.selectGender(stopInfoResponse.getState()));
                            processStop.setStartTime(DateUtils.strCstToDate(stopInfoResponse.getStartTime()));
                            processStop.setEndTime(DateUtils.strCstToDate(stopInfoResponse.getEndTime()));
                        }
                    }
                }
            }
        }
        return process;
    }
}
