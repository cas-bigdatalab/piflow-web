package cn.cnic.component.schedule.service.Impl;

import cn.cnic.base.BaseHibernateModelNoId;
import cn.cnic.base.BaseHibernateModelNoIdUtils;
import cn.cnic.base.util.*;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.mapper.ScheduleMapper;
import cn.cnic.component.schedule.service.IScheduleService;
import cn.cnic.component.schedule.vo.ScheduleVo;
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

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ScheduleMapper scheduleMapper;

    @Override
    public String getScheduleListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        Page<Schedule> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        scheduleMapper.getScheduleList(isAdmin, username, param);
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

    @Override
    public String updateSchedule(String username, boolean isAdmin, ScheduleVo scheduleVo) {
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
        BeanUtils.copyProperties(scheduleVo,scheduleById);
        //set Operator information
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        int update = scheduleMapper.update(scheduleById);
        return null;
    }

    @Override
    public String delSchedule(String username, String id) {
        return null;
    }


}
