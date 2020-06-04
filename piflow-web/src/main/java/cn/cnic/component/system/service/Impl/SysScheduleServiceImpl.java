package cn.cnic.component.system.service.Impl;

import cn.cnic.base.util.*;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.ScheduleRunResultType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.component.system.model.SysSchedule;
import cn.cnic.component.system.service.ISysScheduleService;
import cn.cnic.component.system.vo.SysScheduleVo;
import cn.cnic.domain.system.SysScheduleDomain;
import cn.cnic.mapper.system.SysScheduleMapper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
            Page<SysSchedule> sysScheduleListPage = sysScheduleDomain.getSysScheduleListPage(offset - 1, limit, param);
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
            rtnMap.put("msg", "");
            rtnMap.put("count", sysScheduleListPage.getTotalElements());
            rtnMap.put("data", sysScheduleListPage.getContent());//Data collection
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Get schedule by id
     *
     * @param scheduleId
     * @return
     */
    @Override
    public String getScheduleById(boolean isAdmin, String scheduleId) {
        SysScheduleVo sysScheduleVo = sysScheduleMapper.getSysScheduleById(isAdmin, scheduleId);
        if (null == sysScheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("sysScheduleVo", sysScheduleVo);
    }

    /**
     * Add SysSchedule
     *
     * @param sysScheduleVo
     * @return
     */
    @Override
    @Transactional
    public String createJob(String username, SysScheduleVo sysScheduleVo) {
        try {
            if (StringUtils.isBlank(username)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
            }
            if (null == sysScheduleVo) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter is empty");
            }
            SysSchedule sysSchedule = new SysSchedule();
            BeanUtils.copyProperties(sysScheduleVo, sysSchedule);
            sysSchedule.setCrtDttm(new Date());
            sysSchedule.setCrtUser(username);
            sysSchedule.setLastUpdateDttm(new Date());
            sysSchedule.setLastUpdateUser(username);
            sysSchedule.setStatus(ScheduleState.INIT);
            sysSchedule.setLastRunResult(ScheduleRunResultType.INIT);
            sysScheduleDomain.saveOrUpdate(sysSchedule);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Created successfully");
        } catch (Exception e) {
            logger.error("Create failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Create failed");
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
    public String runOnce(String username, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task corresponding to the current Id does not exist");
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Task name is empty");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        try {
            QuartzUtils.runOnce(scheduler, jobName);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.SUCCEED);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Start successfully");
        } catch (Exception e) {
            logger.error("Start failed", e);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.FAILURE);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Start failed");
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
    public String startJob(String username, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        try {
            QuartzUtils.createScheduleJob(scheduler, sysScheduleById);
            sysScheduleById.setStatus(ScheduleState.RUNNING);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.SUCCEED);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("Started failed", e);
            sysScheduleById.setStatus(ScheduleState.STOP);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.FAILURE);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Started failed");
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
    public String stopJob(String username, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Task name is empty");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        try {
            QuartzUtils.deleteScheduleJob(scheduler, jobName);
            sysScheduleById.setStatus(ScheduleState.STOP);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Stop successfully");
        } catch (Exception e) {
            logger.error("Stop failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Stop failed");
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
    public String pauseJob(String username, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Task name is empty");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        try {
            QuartzUtils.pauseScheduleJob(scheduler, jobName);
            sysScheduleById.setStatus(ScheduleState.SUSPEND);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Suspended successfully");
        } catch (Exception e) {
            logger.error("Suspended failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Suspended failed");
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
    public String resume(String username, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Task name is empty");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        try {
            QuartzUtils.resumeScheduleJob(scheduler, "test1");
            sysScheduleById.setStatus(ScheduleState.RUNNING);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("Started failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Started failed");
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
    public String update(String username, SysScheduleVo sysScheduleVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == sysScheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter is empty");
        }
        String id = sysScheduleVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(id);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        sysScheduleById.setJobName(sysScheduleVo.getJobName());
        sysScheduleById.setJobClass(sysScheduleVo.getJobClass());
        sysScheduleById.setCronExpression(sysScheduleVo.getCronExpression());
        try {
            QuartzUtils.updateScheduleJob(scheduler, sysScheduleById);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("Started failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Started failed");
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
    public String deleteTask(String username, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Task name is empty");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        try {
            QuartzUtils.resumeScheduleJob(scheduler, jobName);
            sysScheduleById.setStatus(ScheduleState.RUNNING);
            sysScheduleDomain.saveOrUpdate(sysScheduleById);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("Started failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Started failed");
        }
    }
}
