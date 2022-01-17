package cn.cnic.component.schedule.service.Impl;

import java.util.Date;
import java.util.Map;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowGroupDomain;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.domain.ProcessGroupDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.schedule.domain.ScheduleDomain;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.service.IScheduleService;
import cn.cnic.component.schedule.vo.ScheduleVo;
import cn.cnic.third.service.ISchedule;


@Service
public class ScheduleServiceImpl implements IScheduleService {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final ProcessGroupDomain processGroupDomain;
    private final FlowGroupDomain flowGroupDomain;
    private final ScheduleDomain scheduleDomain;
    private final ProcessDomain processDomain;
    private final FlowDomain flowDomain;
    private final ISchedule scheduleImpl;

    @Autowired
    public ScheduleServiceImpl(ProcessGroupDomain processGroupDomain,
                               FlowGroupDomain flowGroupDomain,
                               ScheduleDomain scheduleDomain,
                               ProcessDomain processDomain,
                               FlowDomain flowDomain,
                               ISchedule scheduleImpl) {
        this.processGroupDomain = processGroupDomain;
        this.flowGroupDomain = flowGroupDomain;
        this.scheduleDomain = scheduleDomain;
        this.processDomain = processDomain;
        this.flowDomain = flowDomain;
        this.scheduleImpl = scheduleImpl;
    }

    @Override
    public String getScheduleVoListPage(boolean isAdmin, String username, Integer offset, Integer limit, String param) {
        //Determine paging conditions
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        // Load paging plug-in
        Page<ScheduleVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        // search
        scheduleDomain.getScheduleVoList(isAdmin, username, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String addSchedule(String username, ScheduleVo scheduleVo) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        // Judge whether the 'scheduleVo' is empty
        if (null == scheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        Schedule schedule = new Schedule();
        // Copy scheduleVo to schedule
        BeanUtils.copyProperties(scheduleVo, schedule);
        
        // basic properties (required when creating)
        schedule.setCrtDttm(new Date());
        schedule.setCrtUser(username);
        // basic properties
        schedule.setEnableFlag(true);
        schedule.setLastUpdateUser(username);
        schedule.setLastUpdateDttm(new Date());
        schedule.setVersion(0L);
        
        //set uuid
        schedule.setId(UUIDUtils.getUUID32());

        // save
        int insert = scheduleDomain.insert(schedule);

        if (insert <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("add failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
    }

    /**
     * get ScheduleVo by id
     *
     * @param isAdmin  is admin
     * @param username username
     * @param id       schedule id
     * @return json
     */
    @Override
    public String getScheduleVoById(boolean isAdmin, String username, String id) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        // search
        ScheduleVo scheduleVoById = scheduleDomain.getScheduleVoById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleVoById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("scheduleVo", scheduleVoById);
    }

    @Override
    public String updateSchedule(boolean isAdmin, String username, ScheduleVo scheduleVo) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        // Judge whether the 'scheduleVo' is empty
        if (null == scheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        // Judge whether the 'scheduleVo Id' is empty
        if (StringUtils.isBlank(scheduleVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, scheduleVo.getId());
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID " + scheduleVo.getId());
        }
        // Copy scheduleVo data to scheduleById
        BeanUtils.copyProperties(scheduleVo, scheduleById);
        //set Operator information
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        int update = scheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
    }

    @Override
    public String delSchedule(boolean isAdmin, String username, String id) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        // delete
        int delSchedule = scheduleDomain.delScheduleById(isAdmin, username, id);
        // Judge whether it is successful or not
        if (delSchedule <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("del failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
    }

    @Override
    public String startSchedule(boolean isAdmin, String username, String id) {
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        String scheduleRunTemplateId = scheduleById.getScheduleRunTemplateId();
        // Judge whether the 'scheduleRunTemplateId' is empty
        if (StringUtils.isBlank(scheduleRunTemplateId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, Flow or Group is null");
        }
        String type = scheduleById.getType();
        // Judge whether the 'type' is empty
        if (StringUtils.isBlank(type)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, type is null");
        }
        String scheduleProcessTemplateId;
        Process process = null;
        ProcessGroup processGroup = null;
        try {
            // Distinguish types
            if ("FLOW".equals(type)) {
                // query
                Flow flowById = flowDomain.getFlowById(scheduleRunTemplateId);
                // Judge whether the query result is empty
                if (null == flowById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, flow data is null");
                }
                // flow convert process
                process = ProcessUtils.flowToProcess(flowById, username,true);
                if (null == process) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                int addProcess_i = processDomain.addProcess(process);
                if (addProcess_i <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                scheduleProcessTemplateId = process.getId();
            } else if ("FLOW_GROUP".equals(type)) {
                // query
                FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(scheduleRunTemplateId);
                // Judge whether the query result is empty
                if (null == flowGroupById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, Flow data is null");
                }
                // flowGroup convert processGroup
                processGroup = ProcessGroupUtils.flowGroupToProcessGroup(flowGroupById, username, RunModeType.RUN, true);
                if (null == processGroup) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                int addProcessGroup_i = processGroupDomain.addProcessGroup(processGroup);
                if (addProcessGroup_i <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, processGroup convert failed");
                }
                scheduleProcessTemplateId = processGroup.getId();
            } else {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("type error or process is null");
            }
        } catch (Exception e) {
            logger.error("error", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        // call start schedule
        Map<String, Object> thirdScheduleMap = scheduleImpl.scheduleStart(scheduleById, process, processGroup);
        // Judge whether it is successful or not
        if (200 != (int) thirdScheduleMap.get("code")) {
            return JsonUtils.toJsonNoException(thirdScheduleMap);
        }

        // update
        scheduleById.setStatus(ScheduleState.RUNNING);
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        scheduleById.setScheduleId((String) thirdScheduleMap.get("scheduleId"));
        scheduleById.setScheduleProcessTemplateId(scheduleProcessTemplateId);
        // save
        int update = scheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
    }

    @Override
    public String stopSchedule(boolean isAdmin, String username, String id) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : illegal user");
        }
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : id is null");
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Data does not exist");
        }
        // Judge whether the 'scheduleId' is empty
        if (StringUtils.isBlank(scheduleById.getScheduleId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : scheduleId is null");
        }
        // call stop schedule
        String scheduleStopMsg = scheduleImpl.scheduleStop(scheduleById.getScheduleId());
        // Judge whether it is successful or not
        if (StringUtils.isBlank(scheduleStopMsg) || scheduleStopMsg.contains("Exception") || scheduleStopMsg.contains("error")) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call failed");
        }
        // Judge whether it is successful or not
        if (StringUtils.isBlank(scheduleStopMsg) || scheduleStopMsg.contains("failed")) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call failed");
        }
        // update
        scheduleById.setStatus(ScheduleState.STOP);
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        // save
        int update = scheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
    }

}
