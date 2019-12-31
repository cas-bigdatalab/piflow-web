package com.nature.component.system.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ScheduleRunResultType;
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
import javax.transaction.Transactional;
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

    /**
     * Paging query schedule
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    @Override
    public String getScheduleListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
			Page<SysScheduleVo> page = PageHelper.startPage(offset, limit);
            sysScheduleMapper.getSysScheduleList(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Get schedule by id
     *
     * @param scheduleId
     * @return
     */
    public String getScheduleById(String scheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        SysScheduleVo sysScheduleVo = sysScheduleMapper.getSysScheduleById(scheduleId);
        if (null != sysScheduleVo) {
            rtnMap.put("code", 200);
            rtnMap.put("sysScheduleVo", sysScheduleVo);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Add SysSchedule
     *
     * @param sysScheduleVo
     * @return
     */
    @Override
    @Transactional
    public String createJob(SysScheduleVo sysScheduleVo) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("code", 500);
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (null == currentUser) {
                rtnMap.put("errorMsg", "Illegal users");
                logger.warn("Illegal users");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            if (null == sysScheduleVo) {
                rtnMap.put("errorMsg", "Parameter is empty");
                logger.warn("Parameter is empty");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            SysSchedule sysSchedule = new SysSchedule();
            BeanUtils.copyProperties(sysScheduleVo, sysSchedule);
            sysSchedule.setCrtDttm(new Date());
            sysSchedule.setCrtUser(currentUser.getUsername());
            sysSchedule.setLastUpdateDttm(new Date());
            sysSchedule.setLastUpdateUser(currentUser.getUsername());
            sysSchedule.setStatus(ScheduleState.INIT);
            sysSchedule.setLastRunResult(ScheduleRunResultType.INIT);
            sysScheduleDomain.saveOrUpdate(sysSchedule);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Created successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Create failed");
            logger.error("Create failed", e);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Run once timed task
     *
     * @param sysScheduleId
     * @return
     */
    @Override
    @Transactional
    public String runOnce(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            rtnMap.put("errorMsg", "id is empty");
            logger.warn("id is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            rtnMap.put("errorMsg", "The task corresponding to the current Id does not exist");
            logger.warn("The task corresponding to the current Id does not exist");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            rtnMap.put("errorMsg", "Task name is empty");
            logger.warn("Task name is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(currentUser.getUsername());
        try {
            QuartzUtils.runOnce(scheduler, jobName);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.SUCCEED);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Start successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Start failed");
            logger.error("Start failed", e);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.FAILURE);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Start timed task
     *
     * @param sysScheduleId
     * @return
     */
    @Override
    @Transactional
    public String startJob(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            rtnMap.put("errorMsg", "id is empty");
            logger.warn("id is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            rtnMap.put("errorMsg", "The task for which the current Id does not exist");
            logger.warn("The task for which the current Id does not exist");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(currentUser.getUsername());
        try {
            QuartzUtils.createScheduleJob(scheduler, sysScheduleById);
            sysScheduleById.setStatus(ScheduleState.RUNNING);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.SUCCEED);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Started successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Started failed");
            logger.error("Started failed", e);
            sysScheduleById.setStatus(ScheduleState.STOP);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.FAILURE);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Stop timed task
     *
     * @param sysScheduleId
     * @return
     */
    @Override
    @Transactional
    public String stopJob(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            rtnMap.put("errorMsg", "id is empty");
            logger.warn("id is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            rtnMap.put("errorMsg", "The task for which the current Id does not exist");
            logger.warn("The task for which the current Id does not exist");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            rtnMap.put("errorMsg", "Task name is empty");
            logger.warn("Task name is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(currentUser.getUsername());
        try {
            QuartzUtils.deleteScheduleJob(scheduler, jobName);
            sysScheduleById.setStatus(ScheduleState.STOP);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Stop successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Stop failed");
            logger.error("Stop failed", e);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Pause timed task
     *
     * @param sysScheduleId
     * @return
     */
    @Override
    @Transactional
    public String pauseJob(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            rtnMap.put("errorMsg", "id is empty");
            logger.warn("id is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            rtnMap.put("errorMsg", "The task for which the current Id does not exist");
            logger.warn("The task for which the current Id does not exist");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            rtnMap.put("errorMsg", "Task name is empty");
            logger.warn("Task name is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(currentUser.getUsername());
        try {
            QuartzUtils.pauseScheduleJob(scheduler, jobName);
            sysScheduleById.setStatus(ScheduleState.SUSPEND);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Suspended successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Suspended failed");
            logger.error("Suspended failed", e);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Resume timed task
     *
     * @param sysScheduleId
     * @return
     */
    @Override
    @Transactional
    public String resume(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            rtnMap.put("errorMsg", "id is empty");
            logger.warn("id is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            rtnMap.put("errorMsg", "The task for which the current Id does not exist");
            logger.warn("The task for which the current Id does not exist");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            rtnMap.put("errorMsg", "Task name is empty");
            logger.warn("Task name is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(currentUser.getUsername());
        try {
            QuartzUtils.resumeScheduleJob(scheduler, "test1");
            sysScheduleById.setStatus(ScheduleState.RUNNING);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Started successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Started failed");
            logger.error("Started failed", e);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Update timed task
     *
     * @param sysScheduleVo
     * @return
     */
    @Override
    @Transactional
    public String update(SysScheduleVo sysScheduleVo) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (null == sysScheduleVo) {
            rtnMap.put("errorMsg", "Parameter is empty");
            logger.warn("Parameter is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String id = sysScheduleVo.getId();
        if (StringUtils.isBlank(id)) {
            rtnMap.put("errorMsg", "id is empty");
            logger.warn("id is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(id);
        if (null == sysScheduleById) {
            rtnMap.put("errorMsg", "The task for which the current Id does not exist");
            logger.warn("The task for which the current Id does not exist");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(currentUser.getUsername());
        sysScheduleById.setJobName(sysScheduleVo.getJobName());
        sysScheduleById.setJobClass(sysScheduleVo.getJobClass());
        sysScheduleById.setCronExpression(sysScheduleVo.getCronExpression());
        try {
            QuartzUtils.updateScheduleJob(scheduler, sysScheduleById);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Started successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Started failed");
            logger.error("Started failed", e);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Delete timed task
     *
     * @param sysScheduleId
     * @return
     */
    @Override
    @Transactional
    public String deleteTask(String sysScheduleId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            rtnMap.put("errorMsg", "id is empty");
            logger.warn("id is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            rtnMap.put("errorMsg", "The task for which the current Id does not exist");
            logger.warn("The task for which the current Id does not exist");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            rtnMap.put("errorMsg", "Task name is empty");
            logger.warn("Task name is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(currentUser.getUsername());
        try {
            QuartzUtils.resumeScheduleJob(scheduler, jobName);
            sysScheduleById.setStatus(ScheduleState.RUNNING);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Started successfully");
            return JsonUtils.toJsonNoException(rtnMap);
        } catch (Exception e) {
            rtnMap.put("errorMsg", "Started failed");
            logger.error("Started failed", e);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }
}
