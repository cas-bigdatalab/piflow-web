package cn.cnic.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.flow.mapper.FlowGroupMapper;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.third.service.ISchedule;
import cn.cnic.third.vo.schedule.ThirdScheduleEntryVo;
import cn.cnic.third.vo.schedule.ThirdScheduleVo;
import net.sf.json.JSONObject;

@Component
public class ScheduleImpl implements ISchedule {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    FlowMapper flowMapper;

    @Resource
    FlowGroupMapper flowGroupMapper;

    @Override
    public Map<String, Object> scheduleStart(Schedule schedule, Process process, ProcessGroup processGroup) {
        if (null == schedule) {
            return ReturnMapUtils.setFailedMsg("failed, schedule is null");
        }
        String type = schedule.getType();
        Map<String, Object> scheduleContentMap = new HashMap<>();
        if ("FLOW".equals(type) && null != process) {
            scheduleContentMap = ProcessUtils.processToMap(process, "", RunModeType.RUN);
        } else if ("FLOW_GROUP".equals(type) && null != processGroup) {
            scheduleContentMap = ProcessUtils.processGroupToMap(processGroup, RunModeType.RUN);
        } else {
            return ReturnMapUtils.setFailedMsg("type error or process is null");
        }
        Map<String, Object> requestParamMap = new HashMap<>();
        requestParamMap.put("expression", schedule.getCronExpression());
        requestParamMap.put("startDate", DateUtils.dateTimeToStr(schedule.getPlanStartTime()));
        requestParamMap.put("endDate", DateUtils.dateTimeToStr(schedule.getPlanEndTime()));
        requestParamMap.put("schedule", scheduleContentMap);
        String sendPostData = HttpUtils.doPost(SysParamsCache.getScheduleStartUrl(), requestParamMap, null);

        //===============================临时===============================
        //String formatJson = JsonUtils.toFormatJsonNoException(requestParamMap);
        //String path = FileUtils.createJsonFile(formatJson, processGroup.getName(), SysParamsCache.VIDEOS_PATH);
        //logger.info(path);
        //String sendPostData = HttpUtils.doPost(SysParamsCache.getScheduleStartUrl(), path, null);
        //===============================临时===============================

        if (StringUtils.isBlank(sendPostData) || sendPostData.contains("Exception") || sendPostData.contains("error")) {
            return ReturnMapUtils.setFailedMsg("Error : Interface call failed");
        }
        return ReturnMapUtils.setSucceededCustomParam("scheduleId", sendPostData);
    }

    @Override
    public String scheduleStop(String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("scheduleId", scheduleId);
        String sendPostData = HttpUtils.doPost(SysParamsCache.getScheduleStopUrl(), map, null);
        return sendPostData;
    }

    @Override
    public ThirdScheduleVo scheduleInfo(String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("scheduleId", scheduleId);
        String sendGetData = HttpUtils.doGet(SysParamsCache.getScheduleInfoUrl(), map, null);
        if (StringUtils.isBlank(sendGetData) || sendGetData.contains("Exception")) {
            logger.warn("Error : Interface call failed");
            return null;
        }
        // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
        JSONObject obj = JSONObject.fromObject(sendGetData).getJSONObject("schedule");// Convert a json string to a json object
        // Needed when there is a List in jsonObj
        @SuppressWarnings("rawtypes")
        Map<String, Class> classMap = new HashMap<String, Class>();
        // Key is the name of the List in jsonObj, and the value is a generic class of list
        classMap.put("entryList", ThirdScheduleEntryVo.class);
        // Convert a json object to a java object
        ThirdScheduleVo thirdScheduleVo = (ThirdScheduleVo) JSONObject.toBean(obj, ThirdScheduleVo.class, classMap);
        return thirdScheduleVo;
    }

}
