package cn.cnic.third.utils;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.StopState;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdFlowInfoVoUtils {

    public static Process setProcess(Process process, ThirdFlowInfoVo thirdFlowInfoVo) {

        if (null == thirdFlowInfoVo || null == process) {
        	return process;
        }
        process.setLastUpdateUser("syncTask");
        process.setLastUpdateDttm(new Date());
        process.setProgress(thirdFlowInfoVo.getProgress());
        String thirdFlowInfoVoState = thirdFlowInfoVo.getState();
        if (StringUtils.isNotBlank(thirdFlowInfoVoState)) {
            ProcessState processState = null;
            if ("NEW".equals(thirdFlowInfoVoState) || "NEW_SAVING".equals(thirdFlowInfoVoState)) {
                processState = ProcessState.INIT;
            } else if ("RUNNING".equals(thirdFlowInfoVoState)) {
                processState = ProcessState.SUBMITTED;
            } else if ("FINISHED".equals(thirdFlowInfoVoState)) {
                processState = ProcessState.COMPLETED;
            } else {
                processState = ProcessState.selectGender(thirdFlowInfoVoState);
            }

            process.setState(processState);
        }
        //process.setName(thirdFlowInfoVo.getName());
        //process.setProcessId(thirdFlowInfoVo.getPid());
        process.setProcessId(thirdFlowInfoVo.getId());
        process.setStartTime(DateUtils.strCstToDate(thirdFlowInfoVo.getStartTime()));
        process.setEndTime(DateUtils.strCstToDate(thirdFlowInfoVo.getEndTime()));
        Map<String, ThirdFlowInfoStopVo> stopsMap = new HashMap<>();
        List<ProcessStop> processStopList = process.getProcessStopList();
        List<ThirdFlowInfoStopsVo> stops = thirdFlowInfoVo.getStops();
        if (CollectionUtils.isEmpty(stops) || CollectionUtils.isEmpty(processStopList)) {
        	return process;
        }
        for (ThirdFlowInfoStopsVo thirdFlowInfoStopsVo : stops) {
            if (null != thirdFlowInfoStopsVo) {
                ThirdFlowInfoStopVo stopVo = thirdFlowInfoStopsVo.getStop();
                if (null != stopVo) {
                    stopsMap.put(stopVo.getName(), stopVo);
                }
            }
        }
        for (ProcessStop processStop : processStopList) {
            if (null != processStop) {
                ThirdFlowInfoStopVo stopVo = stopsMap.get(processStop.getName());
                if (null != stopVo) {
                    processStop.setState(StopState.selectGender(stopVo.getState()));
                    processStop.setStartTime(DateUtils.strCstToDate(stopVo.getStartTime()));
                    processStop.setEndTime(DateUtils.strCstToDate(stopVo.getEndTime()));
                }
            }
        }
        process.setProcessStopList(processStopList);
        return process;
    }
}
