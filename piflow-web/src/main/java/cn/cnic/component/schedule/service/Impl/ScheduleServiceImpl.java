package cn.cnic.component.schedule.service.Impl;

import cn.cnic.base.BaseHibernateModelNoId;
import cn.cnic.base.BaseHibernateModelNoIdUtils;
import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.ScheduleState;
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

    @Resource
    private ScheduleMapper scheduleMapper;

    @Resource
    private ISchedule scheduleImpl;

    @Override
    public String getScheduleVoListPage(boolean isAdmin, String username, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        Page<ScheduleVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        scheduleMapper.getScheduleVoList(isAdmin, username, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String addSchedule(String username, ScheduleVo scheduleVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
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
        schedule.setId(UUIDUtils.getUUID32());

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
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        ScheduleVo scheduleVoById = scheduleMapper.getScheduleVoById(isAdmin, username, id);
        if (null == scheduleVoById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("scheduleVo", scheduleVoById);
    }

    @Override
    public String updateSchedule(boolean isAdmin, String username, ScheduleVo scheduleVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == scheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        if (StringUtils.isBlank(scheduleVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        // query
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, scheduleVo.getId());
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID " + scheduleById.getId());
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
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, id);
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        int delSchedule = scheduleMapper.delScheduleById(isAdmin, username, id);
        if (delSchedule <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("del failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
    }

    @Override
    public String startSchedule(boolean isAdmin, String username, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, id);
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        Map<String, Object> thirdScheduleMap = scheduleImpl.scheduleStart(username, scheduleById);
        if (200 != (int) thirdScheduleMap.get("code")) {
            return JsonUtils.toJsonNoException(thirdScheduleMap);
        }
        scheduleById.setStatus(ScheduleState.RUNNING);
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        scheduleById.setScheduleId((String) thirdScheduleMap.get("scheduleId"));
        int update = scheduleMapper.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
    }

    @Override
    public String stopSchedule(boolean isAdmin, String username, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : id is null");
        }
        Schedule scheduleById = scheduleMapper.getScheduleById(isAdmin, username, id);
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Data does not exist");
        }
        if (StringUtils.isBlank(scheduleById.getScheduleId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : scheduleId is null");
        }
        String scheduleStopMsg = scheduleImpl.scheduleStop(scheduleById.getScheduleId());
        if (StringUtils.isBlank(scheduleStopMsg) || scheduleStopMsg.contains("Exception") || scheduleStopMsg.contains("error")) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call failed");
        }
        if (StringUtils.isBlank(scheduleStopMsg) || !scheduleStopMsg.contains("ok!")) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call failed");
        }
        scheduleById.setStatus(ScheduleState.STOP);
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        int update = scheduleMapper.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
    }

}
