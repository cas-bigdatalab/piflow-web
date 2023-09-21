package cn.cnic.third.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.common.Eunm.ComponentFileType;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.process.entity.ProcessGroupPath;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.Eunm.RunModeType;
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

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();
    private final StopsComponentDomain stopsComponentDomain;

    public ScheduleImpl(StopsComponentDomain stopsComponentDomain) {
        this.stopsComponentDomain = stopsComponentDomain;
    }

    @Override
    public Map<String, Object> scheduleStart(Schedule schedule, Process process, ProcessGroup processGroup) {
        if (null == schedule) {
            return ReturnMapUtils.setFailedMsg("failed, schedule is null");
        }
        String type = schedule.getType();
        Map<String, Object> scheduleContentMap = new HashMap<>();
        if ("FLOW".equals(type) && null != process) {
            //String json = ProcessUtil.processToJson(process, checkpoint, runModeType);
            //String formatJson = JsonFormatTool.formatJson(json);
            List<ProcessStop> processStopList = process.getProcessStopList();
            if (processStopList == null || processStopList.size() == 0) {
                return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("Stop"));
            }else {
                for (ProcessStop processStop : processStopList) {
                    StopsComponent stops = stopsComponentDomain.getOnlyStopsComponentByBundle(processStop.getBundel());
                    if (stops == null) {
                        return ReturnMapUtils.setFailedMsg(MessageConfig.DATA_ERROR_MSG());
                    }
                    processStop.setComponentType(stops.getComponentType());
                    if (ComponentFileType.PYTHON == processStop.getComponentType()) {
                        processStop.setDockerImagesName(stops.getDockerImagesName());
                    }
                }
            }
            scheduleContentMap = ProcessUtils.processToMap(process, "", RunModeType.RUN, process.getFlowGlobalParamsList());
        } else if ("FLOW_GROUP".equals(type) && null != processGroup) {
            scheduleContentMap = this.processGroupToMap(processGroup, RunModeType.RUN);
        } else {
            return ReturnMapUtils.setFailedMsg(MessageConfig.SCHEDULED_TYPE_OR_DATA_ERROR_MSG());
        }
        Map<String, Object> requestParamMap = new HashMap<>();
        requestParamMap.put("expression", schedule.getCronExpression());
        requestParamMap.put("startDate", DateUtils.dateTimeToStr(schedule.getPlanStartTime()));
        requestParamMap.put("endDate", DateUtils.dateTimeToStr(schedule.getPlanEndTime()));
        requestParamMap.put("schedule", scheduleContentMap);
        String sendPostData = HttpUtils.doPostParmaMap(ApiConfig.getScheduleStartUrl(), requestParamMap, null);

        //===============================临时===============================
        //String formatJson = JsonUtils.toFormatJsonNoException(requestParamMap);
        //String path = FileUtils.createJsonFile(formatJson, processGroup.getName(), ApiConfig.VIDEOS_PATH);
        //logger.info(path);
        //String sendPostData = HttpUtils.doPost(ApiConfig.getScheduleStartUrl(), path, null);
        //===============================临时===============================

        if (StringUtils.isBlank(sendPostData)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (sendPostData.contains("Exception") || sendPostData.contains("error") || sendPostData.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
            return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededCustomParam("scheduleId", sendPostData);
    }

    public Map<String, Object> processGroupToMap(ProcessGroup processGroup, RunModeType runModeType) {

        Map<String, Object> rtnMap = new HashMap<>();
        Map<String, Object> flowGroupVoMap = new HashMap<>();
        flowGroupVoMap.put("name", processGroup.getName());
        flowGroupVoMap.put("uuid", processGroup.getId());

        // all process
        Map<String, Process> processesMap = new HashMap<>();
        Map<String, ProcessGroup> processGroupsMap = new HashMap<>();

        List<Process> processList = processGroup.getProcessList();
        if (null != processList && processList.size() > 0) {
            List<Map<String, Object>> processesListMap = new ArrayList<>();
            for (Process process : processList) {

                List<ProcessStop> processStopList = process.getProcessStopList();
                if (processStopList == null || processStopList.size() == 0) {
//                    continue;
//                    return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("Stop"));
                }else {
                    for (ProcessStop processStop : processStopList) {
                        StopsComponent stops = stopsComponentDomain.getOnlyStopsComponentByBundle(processStop.getBundel());
                        if (stops == null) {
                            return ReturnMapUtils.setFailedMsg(MessageConfig.DATA_ERROR_MSG());
                        }
                        processStop.setComponentType(stops.getComponentType());
                        if (ComponentFileType.PYTHON == processStop.getComponentType()) {
                            processStop.setDockerImagesName(stops.getDockerImagesName());
                        }
                    }
                }
                processesMap.put(process.getPageId(), process);
                Map<String, Object> processMap = ProcessUtils.processToMap(process, null, runModeType, process.getFlowGlobalParamsList());
                processesListMap.add(processMap);
            }
            flowGroupVoMap.put("flows", processesListMap);
        }

        List<ProcessGroup> processGroupList = processGroup.getProcessGroupList();
        if (null != processGroupList && processGroupList.size() > 0) {
            List<Map<String, Object>> processesGroupListMap = new ArrayList<>();
            for (ProcessGroup processGroupNew : processGroupList) {
                processGroupsMap.put(processGroupNew.getPageId(), processGroupNew);
                Map<String, Object> processGroupMap = processGroupToMap(processGroupNew, runModeType);
                processesGroupListMap.add(processGroupMap);
            }
            flowGroupVoMap.put("groups", processesGroupListMap);

        }

        List<ProcessGroupPath> processGroupPathList = processGroup.getProcessGroupPathList();
        if (null != processGroupPathList && processGroupPathList.size() > 0) {
            List<Map<String, Object>> pathListMap = new ArrayList<>();
            for (ProcessGroupPath processGroupPath : processGroupPathList) {
                if (null != processGroupPath) {
                    Map<String, Object> pathMap = new HashMap<>();
                    String formName = "";
                    String toName = "";
                    String from = processGroupPath.getFrom();
                    String to = processGroupPath.getTo();
                    if (null != processesMap.get(from)) {
                        formName = processesMap.get(from).getName();
                    } else if (null != processGroupsMap.get(from)) {
                        formName = processGroupsMap.get(from).getName();
                    }
                    if (null != processesMap.get(to)) {
                        toName = processesMap.get(to).getName();
                    } else if (null != processGroupsMap.get(to)) {
                        toName = processGroupsMap.get(to).getName();
                    }
                    pathMap.put("after", formName);
                    pathMap.put("entry", toName);
                    pathListMap.add(pathMap);
                }
            }
            flowGroupVoMap.put("conditions", pathListMap);
        }
        rtnMap.put("group", flowGroupVoMap);
        return rtnMap;
    }

    @Override
    public String scheduleStop(String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("scheduleId", scheduleId);
        String sendPostData = HttpUtils.doPostParmaMap(ApiConfig.getScheduleStopUrl(), map, null);
        return sendPostData;
    }

    @Override
    public ThirdScheduleVo scheduleInfo(String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("scheduleId", scheduleId);
        String sendGetData = HttpUtils.doGet(ApiConfig.getScheduleInfoUrl(), map, null);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Error : " + MessageConfig.INTERFACE_CALL_SUCCEEDED_VALUE_NULL_ERROR_MSG());
            return null;
        }
        if (sendGetData.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || sendGetData.contains("Exception")) {
            logger.warn("Error : " + MessageConfig.INTERFACE_CALL_ERROR_MSG());
            return null;
        }
        // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
        // Convert a json string to a json object
        JSONObject obj = JSONObject.fromObject(sendGetData).getJSONObject("schedule");
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
