package com.nature.schedule;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SpringContextUtil;
import com.nature.common.executor.ServicesExecutor;
import com.nature.common.schedule.ISchedule;
import com.nature.common.schedule.ScheduleConfig;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.transaction.process.ProcessTransaction;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ProcessInfoSync {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessTransaction processTransaction;

    @Resource
    private ScheduleConfig scheduleConfig;

    public void executeAsync() {
        ISchedule iSchedule = new ISchedule() {
            @Override
            public void taskSync() {
                logger.info("processSync start");
                List<String> runningProcess = processTransaction.getRunningProcess();
                if (CollectionUtils.isNotEmpty(runningProcess)) {
                    Runnable runnable = new Thread(new Thread() {
                        @Override
                        public void run() {
                            for (String appId : runningProcess) {
                                IGetFlowInfo getFlowInfoImpl = (IGetFlowInfo) SpringContextUtil.getBean("getFlowInfoImpl");
                                getFlowInfoImpl.getProcessInfoAndSave(appId);
                            }
                        }
                    });
                    ServicesExecutor.getServicesExecutorServiceService().execute(runnable);
                }
                logger.info("processSync end");
            }
        };
        scheduleConfig.setCron(SysParamsCache.SYNC_PROCESS_CRON, iSchedule);
    }
}
