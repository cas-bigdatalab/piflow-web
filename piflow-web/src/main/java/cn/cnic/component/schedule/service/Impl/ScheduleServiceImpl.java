package cn.cnic.component.schedule.service.Impl;

import cn.cnic.base.BaseHibernateModelNoId;
import cn.cnic.base.BaseHibernateModelNoIdUtils;
import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.mapper.FlowGroupMapper;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.process.domain.ProcessGroupDomainU;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.domain.ProcessDomainU;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.mapper.ScheduleMapper;
import cn.cnic.component.schedule.service.IScheduleService;
import cn.cnic.component.schedule.vo.ScheduleVo;
import cn.cnic.third.service.ISchedule;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ScheduleMapper scheduleMapper;

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private FlowGroupMapper flowGroupMapper;

    @Resource
    private ProcessDomainU processDomainU;

    @Resource
    private ProcessGroupDomainU processGroupDomainU;

    @Resource
    private ISchedule scheduleImpl;

    @Override
    public String getScheduleVoListPage(boolean isAdmin, String username, Integer offset, Integer limit, String param) {
        //Determine paging conditions
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        // Load paging plug-in
        Page<ScheduleVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        // search
        scheduleMapper.getScheduleVoList(isAdmin, username, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
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

        // Initialization base field value does not include ID
        BaseHibernateModelNoId baseHibernateModelNoId = BaseHibernateModelNoIdUtils.newBaseHibernateModelNoId(username);
        //Copy base field value to schedule
        BeanUtils.copyProperties(baseHibernateModelNoId, schedule);
        //set uuid
        schedule.setId(UUIDUtils.getUUID32());

        // save
        int insert = scheduleMapper.insert(schedule);

        if (insert <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("add failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
    }

    /**
     * get ScheduleVo by id
     *
     * @param isAdmin  is admin
     * @param username username
     * @param id       schedule id
     * @return json
     */
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
        ScheduleVo scheduleVoById = scheduleMapper.getScheduleVoById(isAdmin, username, id);
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
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, scheduleVo.getId());
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID " + scheduleVo.getId());
        }
        // Copy scheduleVo data to scheduleById
        BeanUtils.copyProperties(scheduleVo, scheduleById);
        //set Operator information
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        int update = scheduleMapper.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
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
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        // delete
        int delSchedule = scheduleMapper.delScheduleById(isAdmin, username, id);
        // Judge whether it is successful or not
        if (delSchedule <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("del failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
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
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, id);
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
                Flow flowById = flowMapper.getFlowById(scheduleRunTemplateId);
                // Judge whether the query result is empty
                if (null == flowById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, flow data is null");
                }
                // flow convert process
                process = ProcessUtils.flowToProcess(flowById, username,true);
                if (null == process) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                int addProcess_i = processDomainU.addProcess(process);
                if (addProcess_i <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                scheduleProcessTemplateId = process.getId();
            } else if ("FLOW_GROUP".equals(type)) {
                // query
                FlowGroup flowGroupById = flowGroupMapper.getFlowGroupById(scheduleRunTemplateId);
                // Judge whether the query result is empty
                if (null == flowGroupById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, Flow data is null");
                }
                // flowGroup convert processGroup
                processGroup = ProcessGroupUtils.flowGroupToProcessGroup(flowGroupById, username, RunModeType.RUN, true);
                if (null == processGroup) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                int addProcessGroup_i = processGroupDomainU.addProcessGroup(processGroup);
                if (addProcessGroup_i <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, processGroup convert failed");
                }
                scheduleProcessTemplateId = processGroup.getId();
            } else {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("type error or process is null");
            }
        } catch (Exception e) {
            logger.error("error", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
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
        int update = scheduleMapper.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
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
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, id);
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
        int update = scheduleMapper.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
    }

}
