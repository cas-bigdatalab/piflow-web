package cn.cnic.schedule;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SpringContextUtil;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.jpa.domain.ProcessGroupDomain;
import cn.cnic.third.service.IGroup;
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
public class GroupScheduleSync extends QuartzJobBean {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessGroupDomain processGroupDomain;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("processGroupSync start : " + formatter.format(new Date()));
        List<String> runningProcessGroup = processGroupDomain.getRunningProcessGroupAppId();
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