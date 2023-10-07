package cn.cnic.schedule;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SpringContextUtil;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.domain.ProcessGroupDomain;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.schedule.domain.ScheduleDomain;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.vo.ScheduleVo;
import cn.cnic.third.service.ISchedule;
import cn.cnic.third.vo.schedule.ThirdScheduleEntryVo;
import cn.cnic.third.vo.schedule.ThirdScheduleVo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class RunningGroupScheduleSync extends QuartzJobBean {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ScheduleDomain scheduleDomain;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("groupScheduleSync start : " + formatter.format(new Date()));
        List<ScheduleVo> scheduleRunningList = scheduleDomain.getScheduleIdListByStateRunning(true, "sync");
        if (CollectionUtils.isNotEmpty(scheduleRunningList)) {
            ISchedule scheduleImpl = (ISchedule) SpringContextUtil.getBean("scheduleImpl");
            for (ScheduleVo scheduleVo : scheduleRunningList) {
                if (null == scheduleVo) {
                    continue;
                }
                ThirdScheduleVo thirdScheduleVo = scheduleImpl.scheduleInfo(scheduleVo.getScheduleId());
                Schedule scheduleById = scheduleDomain.getScheduleById(true, "sync", scheduleVo.getId());
                if ("STOPED".equals(thirdScheduleVo.getState())) {
                    scheduleById.setStatus(ScheduleState.STOP);
                    scheduleDomain.update(scheduleById);
                }
                List<ThirdScheduleEntryVo> entryList = thirdScheduleVo.getEntryList();
                if (CollectionUtils.isEmpty(entryList)) {
                    return;
                }
                ServicesExecutor.getServicesExecutorServiceService().execute(new ScheduleRunnable(scheduleVo.getScheduleProcessTemplateId(), entryList));
            }

        }
        logger.info("groupScheduleSync end : " + formatter.format(new Date()));
    }

    class ScheduleRunnable implements Runnable{
        private String scheduleProcessTemplateId;
        private List<ThirdScheduleEntryVo> entryList;
        public ScheduleRunnable(String scheduleProcessTemplateId, List<ThirdScheduleEntryVo> entryList){
            this.scheduleProcessTemplateId = scheduleProcessTemplateId;
            this.entryList = entryList;
        }
        @Override
        public void run() {
            try {
                ProcessDomain processDomain = (ProcessDomain) SpringContextUtil.getBean("processDomain");
                ProcessGroupDomain processGroupDomain = (ProcessGroupDomain) SpringContextUtil.getBean("processGroupDomain");
                for (ThirdScheduleEntryVo thirdScheduleEntryVo : entryList) {
                    if (null == thirdScheduleEntryVo) {
                        continue;
                    }
                    String scheduleEntryType = thirdScheduleEntryVo.getScheduleEntryType();
                    if ("Flow".equals(scheduleEntryType)) {
                        String processIdByAppId = processDomain.getProcessIdByAppId("sync", true, thirdScheduleEntryVo.getScheduleEntryId());
                        if (StringUtils.isNotBlank(processIdByAppId)) {
                            continue;
                        }
                        Process processById = processDomain.getProcessById("sync", true, scheduleProcessTemplateId);
                        if (processById == null) {
                            logger.warn("sync failed");
                            continue;
                        }
                        // copy and Create
                        Process processCopy = ProcessUtils.copyProcess(processById, "sync", RunModeType.RUN, true);
                        if (null == processCopy) {
                            logger.warn("sync failed");
                            continue;
                        }
                        try {
                            processCopy.setAppId(thirdScheduleEntryVo.getScheduleEntryId());
                            int addProcess = processDomain.addProcess(processCopy);
                            if (addProcess <= 0) {
                                logger.warn("sync failed");
                            }
                        } catch (Exception e) {
                            logger.error("error:", e);
                        }
                        continue;
                    }
                    List<String> processGroupIdByAppId = processGroupDomain.getProcessGroupIdByAppId(thirdScheduleEntryVo.getScheduleEntryId());
                    if (null != processGroupIdByAppId && processGroupIdByAppId.size() > 0) {
                        continue;
                    }
                    ProcessGroup processGroupById = processGroupDomain.getProcessGroupById("sync", true, scheduleProcessTemplateId);
                    if (null == processGroupById) {
                        continue;
                    }
                    // copy and Create
                    ProcessGroup copyProcessGroup = ProcessGroupUtils.copyProcessGroup(processGroupById, "sync", RunModeType.RUN, true);
                    if (null == copyProcessGroup) {
                        continue;
                    }
                    try {
                        copyProcessGroup.setAppId(thirdScheduleEntryVo.getScheduleEntryId());
                        int addProcessGroup = processGroupDomain.addProcessGroup(copyProcessGroup);
                        if (addProcessGroup <= 0) {
                            logger.warn("sync failed");
                        }
                    } catch (Exception e) {
                        logger.error("error:", e);
                    }

                }
            } catch (Exception e) {
                logger.error("update process group data error", e);
            }
        }
    }

}