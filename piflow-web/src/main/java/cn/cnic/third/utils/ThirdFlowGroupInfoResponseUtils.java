package cn.cnic.third.utils;

import cn.cnic.base.util.DateUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.StopState;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.third.vo.flowGroup.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdFlowGroupInfoResponseUtils {

    public static ProcessGroup setProcessGroup(ProcessGroup processGroup, ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse) {

        if (null != thirdFlowGroupInfoResponse && null != processGroup) {
            processGroup.setLastUpdateUser("syncTask");
            processGroup.setLastUpdateDttm(new Date());
            //processGroup.setProgress(thirdFlowGroupInfoResponse.getProgress());
            if (StringUtils.isNotBlank(thirdFlowGroupInfoResponse.getState())) {
                processGroup.setState(ProcessState.selectGender(thirdFlowGroupInfoResponse.getState()));
            }
            //process.setProcessId(thirdFlowGroupInfoResponse.getPid());
            processGroup.setProcessId(thirdFlowGroupInfoResponse.getId());
            processGroup.setName(thirdFlowGroupInfoResponse.getName());
            processGroup.setStartTime(DateUtils.strCstToDate(thirdFlowGroupInfoResponse.getStartTime()));
            processGroup.setEndTime(DateUtils.strCstToDate(thirdFlowGroupInfoResponse.getEndTime()));
            if (StringUtils.isBlank(processGroup.getAppId())) {
                processGroup.setAppId(thirdFlowGroupInfoResponse.getId());
            }

            Map<String, ThirdFlowInfoResponse> flowInfoResponseMap = new HashMap<>();
            List<ThirdFlowInfoOutResponse> flows = thirdFlowGroupInfoResponse.getFlows();
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

            Map<String, ThirdFlowGroupInfoResponse> flowGrpupInfoResponseMap = new HashMap<>();
            List<ThirdFlowGroupInfoOutResponse> flowGroups = thirdFlowGroupInfoResponse.getGroups();
            List<ProcessGroup> processGroupList = processGroup.getProcessGroupList();
            if (CollectionUtils.isNotEmpty(flowGroups) && CollectionUtils.isNotEmpty(processGroupList)) {
                for (ThirdFlowGroupInfoOutResponse thirdFlowGroupInfoOutResponse_i : flowGroups) {
                    if (null != thirdFlowGroupInfoOutResponse_i) {
                        ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse_i = thirdFlowGroupInfoOutResponse_i.getGroup();
                        if (null != thirdFlowGroupInfoResponse_i) {
                            flowGrpupInfoResponseMap.put(thirdFlowGroupInfoResponse_i.getName(), thirdFlowGroupInfoResponse_i);
                        }
                    }
                }
                for (ProcessGroup processGroup_i : processGroupList) {
                    if (null != processGroup_i) {
                        ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse_copy = flowGrpupInfoResponseMap.get(processGroup_i.getName());
                        if (null != thirdFlowGroupInfoResponse_copy) {
                            setProcessGroup(processGroup_i, thirdFlowGroupInfoResponse_copy);
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
            if (StringUtils.isNotBlank(flowInfoResponse.getState())) {
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
