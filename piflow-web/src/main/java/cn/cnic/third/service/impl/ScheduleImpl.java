package cn.cnic.third.service.impl;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.JsonUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.mapper.FlowGroupMapper;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.mapper.ProcessGroupMapper;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.third.service.ISchedule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduleImpl implements ISchedule {

    @Resource
    FlowMapper flowMapper;

    @Resource
    FlowGroupMapper flowGroupMapper;

    @Override
    public String scheduleStart(String username, Schedule schedule) {
        if (null == schedule) {
            return "failed, schedule is null";
        }
        String scheduleRunTemplateId = schedule.getScheduleRunTemplateId();
        if (StringUtils.isBlank(scheduleRunTemplateId)) {
            return "failed, Flow or Group is null";
        }
        String type = schedule.getType();
        Map<String, Object> scheduleContentMap = new HashMap<>();
        if ("FLOW".equals(type)) {
            Flow flowById = flowMapper.getFlowById(scheduleRunTemplateId);
            if (null == flowById) {
                return "failed, flow data is null";
            }
            Process process = ProcessUtils.flowToProcess(flowById, username);
            if (null == process) {
                return "failed, process convert failed";
            }
            scheduleContentMap = ProcessUtils.processToMap(process, "", RunModeType.RUN);

        } else if ("FLOW_GROUP".equals(type)) {
            FlowGroup flowGroupById = flowGroupMapper.getFlowGroupById(scheduleRunTemplateId);
            if (null == flowGroupById) {
                return "failed, Flow data is null";
            }
            ProcessGroup processGroup = ProcessGroupUtils.flowGroupToProcessGroup(flowGroupById, username, RunModeType.RUN);
            if (null == processGroup) {
                return "failed, process convert failed";
            }
            scheduleContentMap = ProcessUtils.processGroupToMap(processGroup, RunModeType.RUN);
        } else {
            return "type error";
        }
        Map<String, Object> requestParamMap = new HashMap<>();
        requestParamMap.put("expression", schedule.getCronExpression());
        requestParamMap.put("startDate", DateUtils.dateTimeToStr(schedule.getPlanStartTime()));
        requestParamMap.put("endDate", DateUtils.dateTimeToStr(schedule.getPlanEndTime()));
        requestParamMap.put("schedule", scheduleContentMap);

        String sendPostData = HttpUtils.doPost(SysParamsCache.getScheduleStopUrl(), requestParamMap, null);
        return sendPostData;
    }

    @Override
    public String scheduleStop(String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("scheduleId", scheduleId);
        String sendPostData = HttpUtils.doPost(SysParamsCache.getScheduleStopUrl(), map, null);
        return sendPostData;
    }

    @Override
    public String scheduleInfo(String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("scheduleId", scheduleId);
        String sendGetData = HttpUtils.doGet(SysParamsCache.getScheduleInfoUrl(), map, null);
        return sendGetData;
    }

}
