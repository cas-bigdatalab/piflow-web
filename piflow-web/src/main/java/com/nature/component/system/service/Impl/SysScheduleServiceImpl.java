package com.nature.component.system.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ScheduleState;
import com.nature.component.system.model.SysSchedule;
import com.nature.component.system.service.ISysScheduleService;
import com.nature.component.system.vo.SysScheduleVo;
import com.nature.domain.system.SysScheduleDomain;
import com.nature.mapper.system.SysScheduleMapper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysScheduleServiceImpl implements ISysScheduleService {

    Logger logger = LoggerUtil.getLogger();

    //Injection task scheduling
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SysScheduleDomain sysScheduleDomain;

    @Resource
    private SysScheduleMapper sysScheduleMapper;

    @Override
    public String getScheduleListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
            Page page = PageHelper.startPage(offset, limit);
            sysScheduleMapper.getSysScheduleList(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String createJob(SysScheduleVo sysScheduleVo) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("code", 500);
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (null == currentUser) {
                rtnMap.put("errorMsg", "Illegal users");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            if (null == sysScheduleVo) {
                rtnMap.put("errorMsg", "Parameter is empty");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            SysSchedule sysSchedule = new SysSchedule();
            BeanUtils.copyProperties(sysScheduleVo, sysSchedule);
            sysSchedule.setCrtDttm(new Date());
            sysSchedule.setCrtUser(currentUser.getUsername());
            sysSchedule.setLastUpdateDttm(new Date());
            sysSchedule.setLastUpdateUser(currentUser.getUsername());
            sysSchedule.setStatus(ScheduleState.INIT);
            sysScheduleDomain.saveOrUpdate(sysSchedule);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Created successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Create failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    @Override
    public String runOnce(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("code", 500);
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (null == currentUser) {
                rtnMap.put("errorMsg", "Illegal users");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            if (StringUtils.isBlank(sysScheduleId)) {
                rtnMap.put("errorMsg", "id is empty");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
            if (null == sysScheduleById) {
                rtnMap.put("errorMsg", "The task corresponding to the current Id does not exist");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            String jobName = sysScheduleById.getJobName();
            if (StringUtils.isBlank(jobName)) {
                return "Task name is empty";
            }
            QuartzUtils.runOnce(scheduler, jobName);
        } catch (Exception e) {
            return "Run failed";
        }
        return "Run successfully";
    }

    @Override
    public String startJob(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("code", 500);
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (null == currentUser) {
                rtnMap.put("errorMsg", "Illegal users");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            if (StringUtils.isBlank(sysScheduleId)) {
                rtnMap.put("errorMsg", "id为空");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
            if (null == sysScheduleById) {
                rtnMap.put("errorMsg", "当前Id对于的任务不存在");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            QuartzUtils.createScheduleJob(scheduler, sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "启动成功");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "启动失败");
            logger.error("启动失败", e);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    @Override
    public String pauseJob(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("code", 500);
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (null == currentUser) {
                rtnMap.put("errorMsg", "Illegal users");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            if (StringUtils.isBlank(sysScheduleId)) {
                rtnMap.put("errorMsg", "id为空");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
            if (null == sysScheduleById) {
                rtnMap.put("errorMsg", "当前Id对于的任务不存在");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            String jobName = sysScheduleById.getJobName();
            if (StringUtils.isBlank(jobName)) {
                return "任务名称为空";
            }
            QuartzUtils.pauseScheduleJob(scheduler, jobName);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "暂停成功");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "暂停失败");
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    @Override
    public String resume(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("code", 500);
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (null == currentUser) {
                rtnMap.put("errorMsg", "Illegal users");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            if (StringUtils.isBlank(sysScheduleId)) {
                rtnMap.put("errorMsg", "id为空");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
            if (null == sysScheduleById) {
                rtnMap.put("errorMsg", "当前Id对于的任务不存在");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            String jobName = sysScheduleById.getJobName();
            if (StringUtils.isBlank(jobName)) {
                return "任务名称为空";
            }
            QuartzUtils.resumeScheduleJob(scheduler, "test1");
        } catch (Exception e) {
            return "启动失败";
        }
        return "启动成功";
    }

    @Override
    public String update(SysScheduleVo sysScheduleVo) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("code", 500);
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (null == currentUser) {
                rtnMap.put("errorMsg", "Illegal users");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            if (null == sysScheduleVo) {
                rtnMap.put("errorMsg", "Parameter is empty");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            SysSchedule sysSchedule = new SysSchedule();
            BeanUtils.copyProperties(sysScheduleVo, sysSchedule);
            //进行测试所以写死
            sysSchedule.setJobClass("com.nature.schedule.MyTask");
            sysSchedule.setJobName("test1");
            sysSchedule.setCronExpression("10 * * * * ?");
            QuartzUtils.updateScheduleJob(scheduler, sysSchedule);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "启动成功");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "启动失败");
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }
}
