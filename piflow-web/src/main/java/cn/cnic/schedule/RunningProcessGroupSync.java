package cn.cnic.schedule;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SpringContextUtil;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.mapper.ProcessGroupMapper;
import cn.cnic.third.service.IGroup;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class RunningProcessGroupSync extends QuartzJobBean {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessGroupMapper processGroupMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("processGroupSync start : " + formatter.format(new Date()));
        List<String> runningProcessGroup = processGroupMapper.getRunningProcessGroupAppId();
        if (CollectionUtils.isNotEmpty(runningProcessGroup)) {
            Runnable runnable = new Thread(new Thread() {
                @Override
                public void run() {
                    try {
                        IGroup groupImpl = (IGroup) SpringContextUtil.getBean("groupImpl");
                        groupImpl.updateFlowGroupsByInterface(runningProcessGroup);
                    } catch (Exception e) {
                        logger.error("errorMsg:", e);
                    }
                }
            });
            ServicesExecutor.getServicesExecutorServiceService().execute(runnable);
        }
        logger.info("processGroupSync end : " + formatter.format(new Date()));
    }
}