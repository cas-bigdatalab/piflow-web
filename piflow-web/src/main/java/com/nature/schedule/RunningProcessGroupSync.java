package com.nature.schedule;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SpringContextUtil;
import com.nature.common.executor.ServicesExecutor;
import com.nature.mapper.process.ProcessGroupMapper;
import com.nature.third.service.IGroup;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class RunningProcessGroupSync extends QuartzJobBean {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessGroupMapper processGroupMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("processGroupSync start : " + formatter.format(new Date()));
        List<String> runningProcessGroup = processGroupMapper.getRunningProcessGroup();
        if (CollectionUtils.isNotEmpty(runningProcessGroup)) {
            Runnable runnable = new Thread(new Thread() {
                @Override
                public void run() {
                    IGroup groupImpl = (IGroup) SpringContextUtil.getBean("groupImpl");
                    groupImpl.updateFlowGroupsByInterface(runningProcessGroup);
                }
            });
            ServicesExecutor.getServicesExecutorServiceService().execute(runnable);
        }
        logger.info("processGroupSync end : " + formatter.format(new Date()));
    }
}