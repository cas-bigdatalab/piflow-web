package cn.cnic.schedule;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SpringContextUtil;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.third.service.IFlow;
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
public class RunningProcessSync extends QuartzJobBean {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessMapper processMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("processSync start : " + formatter.format(new Date()));
        List<String> runningProcess = processMapper.getRunningProcessAppId();
        if (CollectionUtils.isNotEmpty(runningProcess)) {
            Runnable runnable = new Thread(new Thread() {
                @Override
                public void run() {
                    for (String appId : runningProcess) {
                        try {
                            IFlow getFlowInfoImpl = (IFlow) SpringContextUtil.getBean("flowImpl");
                            getFlowInfoImpl.getProcessInfoAndSave(appId);
                        } catch (Exception e) {
                            logger.error("errorMsg:", e);
                        }
                    }
                }
            });
            ServicesExecutor.getServicesExecutorServiceService().execute(runnable);
        }
        logger.info("processSync end : " + formatter.format(new Date()));
    }
}