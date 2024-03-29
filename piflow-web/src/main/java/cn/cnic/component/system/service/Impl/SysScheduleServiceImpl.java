package cn.cnic.component.system.service.Impl;

import java.util.Date;
import java.util.Map;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.QuartzUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.Eunm.ScheduleRunResultType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.component.system.domain.SysScheduleDomain;
import cn.cnic.component.system.entity.SysSchedule;
import cn.cnic.component.system.service.ISysScheduleService;
import cn.cnic.component.system.vo.SysScheduleVo;


@Service
public class SysScheduleServiceImpl implements ISysScheduleService {

    private Logger logger = LoggerUtil.getLogger();

    private final SysScheduleDomain sysScheduleDomain;
    private final Scheduler scheduler;

    @Autowired
    public SysScheduleServiceImpl(SysScheduleDomain sysScheduleDomain, Scheduler scheduler) {
        this.sysScheduleDomain = sysScheduleDomain;
        this.scheduler = scheduler;
    }

    /**
     * Paging query schedule
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    @Override
    public String getScheduleListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<SysSchedule> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        sysScheduleDomain.getSysScheduleList(isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    /**
     * Get schedule by id
     *
     * @param scheduleId
     * @return
     */
    @Override
    public String getScheduleById(String username, boolean isAdmin, String scheduleId) {
        SysScheduleVo sysScheduleVo = sysScheduleDomain.getSysScheduleVoById(isAdmin, scheduleId);
        if (null == sysScheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
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
    public String createJob(String username, boolean isAdmin, SysScheduleVo sysScheduleVo) {
        try {
            if (StringUtils.isBlank(username)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
            }
            if (null == sysScheduleVo) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
            }
            SysSchedule sysSchedule = new SysSchedule();
            BeanUtils.copyProperties(sysScheduleVo, sysSchedule);
            sysSchedule.setCrtDttm(new Date());
            sysSchedule.setCrtUser(username);
            sysSchedule.setLastUpdateDttm(new Date());
            sysSchedule.setLastUpdateUser(username);
            sysSchedule.setStatus(ScheduleState.INIT);
            sysSchedule.setLastRunResult(ScheduleRunResultType.INIT);
            int i = sysScheduleDomain.insertSysSchedule(sysSchedule);
            if (i > 0) {
                return ReturnMapUtils.setSucceededMsgRtnJsonStr("Created successfully");
            }
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
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
    public String runOnce(String username, boolean isAdmin, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysScheduleId"));
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(isAdmin, sysScheduleId);
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
            int update = sysScheduleDomain.updateSysSchedule(sysScheduleById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Start successfully");
        } catch (Exception e) {
            logger.error("Start failed", e);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.FAILURE);
            sysScheduleDomain.updateSysSchedule(sysScheduleById);
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
    public String startJob(String username, boolean isAdmin, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysScheduleId"));
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(isAdmin, sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        sysScheduleById.setLastUpdateDttm(new Date());
        sysScheduleById.setLastUpdateUser(username);
        try {
            QuartzUtils.createScheduleJob(scheduler, sysScheduleById);
            sysScheduleById.setStatus(ScheduleState.RUNNING);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.SUCCEED);
            int update = sysScheduleDomain.updateSysSchedule(sysScheduleById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Start successfully");
        } catch (Exception e) {
            logger.error("Started failed", e);
            sysScheduleById.setStatus(ScheduleState.STOP);
            sysScheduleById.setLastRunResult(ScheduleRunResultType.FAILURE);
            sysScheduleDomain.updateSysSchedule(sysScheduleById);
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
    public String stopJob(String username, boolean isAdmin, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysScheduleId"));
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(isAdmin, sysScheduleId);
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
            int update = sysScheduleDomain.updateSysSchedule(sysScheduleById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
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
    public String pauseJob(String username, boolean isAdmin, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysScheduleId"));
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(isAdmin,sysScheduleId);
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
            int update = sysScheduleDomain.updateSysSchedule(sysScheduleById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
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
    public String resume(String username, boolean isAdmin, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysScheduleId"));
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(isAdmin, sysScheduleId);
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
            int update = sysScheduleDomain.updateSysSchedule(sysScheduleById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
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
    public String update(String username, boolean isAdmin, SysScheduleVo sysScheduleVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == sysScheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String id = sysScheduleVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysScheduleId"));
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(isAdmin, id);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Task name is empty");
        }
        try {
            QuartzUtils.deleteScheduleJob(scheduler, jobName);
            sysScheduleById.setLastUpdateDttm(new Date());
            sysScheduleById.setLastUpdateUser(username);
            sysScheduleById.setJobName(sysScheduleVo.getJobName());
            sysScheduleById.setJobClass(sysScheduleVo.getJobClass());
            sysScheduleById.setCronExpression(sysScheduleVo.getCronExpression());
            int update = sysScheduleDomain.updateSysSchedule(sysScheduleById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
            if (ScheduleState.RUNNING == sysScheduleById.getStatus()) {
                QuartzUtils.createScheduleJob(scheduler, sysScheduleById);
            }
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
    public String deleteTask(String username, boolean isAdmin, String sysScheduleId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(sysScheduleId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysScheduleId"));
        }
        SysSchedule sysScheduleById = sysScheduleDomain.getSysScheduleById(isAdmin, sysScheduleId);
        if (null == sysScheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        String jobName = sysScheduleById.getJobName();
        if (StringUtils.isBlank(jobName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Task name is empty");
        }
        try {
            if (ScheduleState.RUNNING == sysScheduleById.getStatus()) {
                QuartzUtils.deleteScheduleJob(scheduler, jobName);
            }
            sysScheduleById.setEnableFlag(false);
            sysScheduleById.setLastUpdateDttm(new Date());
            sysScheduleById.setLastUpdateUser(username);
            int update = sysScheduleDomain.updateSysSchedule(sysScheduleById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("Started failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Started failed");
        }
    }
}
