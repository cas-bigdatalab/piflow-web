package cn.cnic.schedule;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SpringContextUtil;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.transaction.ProcessGroupTransaction;
import cn.cnic.component.process.transaction.ProcessTransaction;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.schedule.mapper.ScheduleMapper;
import cn.cnic.component.schedule.vo.ScheduleVo;
import cn.cnic.third.service.ISchedule;
import cn.cnic.third.vo.schedule.ThirdScheduleEntryVo;
import cn.cnic.third.vo.schedule.ThirdScheduleVo;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class RunningGroupScheduleSync extends QuartzJobBean {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ScheduleMapper scheduleMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("groupScheduleSync start : " + formatter.format(new Date()));
        List<ScheduleVo> scheduleRunningList = scheduleMapper.getScheduleIdListByStateRunning(true, "sync");
        if (CollectionUtils.isNotEmpty(scheduleRunningList)) {
            ISchedule scheduleImpl = (ISchedule) SpringContextUtil.getBean("scheduleImpl");
            ProcessTransaction processTransaction = (ProcessTransaction) SpringContextUtil.getBean("processTransaction");
            ProcessGroupTransaction processGroupTransaction = (ProcessGroupTransaction) SpringContextUtil.getBean("processGroupTransaction");
            for (ScheduleVo scheduleVo : scheduleRunningList) {
                if (null == scheduleVo) {
                    continue;
                }
                ThirdScheduleVo thirdScheduleVo = scheduleImpl.scheduleInfo(scheduleVo.getScheduleId());
                List<ThirdScheduleEntryVo> entryList = thirdScheduleVo.getEntryList();
                if (CollectionUtils.isEmpty(entryList)) {
                    return;
                }
                Runnable runnable = new Thread(new Thread() {
                    @Override
                    public void run() {
                        for (ThirdScheduleEntryVo thirdScheduleEntryVo : entryList) {
                            if (null == thirdScheduleEntryVo) {
                                continue;
                            }
                            String scheduleEntryType = thirdScheduleEntryVo.getScheduleEntryType();
                            if ("Flow".equals(scheduleEntryType)) {
                                Process processById = processTransaction.getProcessById("sync", true, scheduleVo.getScheduleProcessTemplateId());
                                // copy and Create
                                Process processCopy = ProcessUtils.copyProcess(processById, "sync", RunModeType.RUN, true);
                                try {
                                    processCopy.setAppId(thirdScheduleEntryVo.getScheduleEntryId());
                                    int addProcess = processTransaction.addProcess(processCopy);
                                    if (addProcess <= 0) {
                                        logger.warn("sync failed");
                                    }
                                } catch (Exception e) {
                                    logger.error("error:", e);
                                }
                                continue;
                            }
                            ProcessGroup processGroupById = processGroupTransaction.getProcessGroupById("sync", true, scheduleVo.getScheduleProcessTemplateId());
                            // copy and Create
                            ProcessGroup copyProcessGroup = ProcessGroupUtils.copyProcessGroup(processGroupById, "sync", RunModeType.RUN, true);
                            try {
                                int addProcessGroup = processGroupTransaction.addProcessGroup(copyProcessGroup);
                                if (addProcessGroup <= 0) {
                                    logger.warn("sync failed");
                                }
                            } catch (Exception e) {
                                logger.error("error:", e);
                            }

                        }
                    }
                });
                ServicesExecutor.getServicesExecutorServiceService().execute(runnable);
            }

        }
        logger.info("groupScheduleSync end : " + formatter.format(new Date()));
    }

}