package cn.cnic.schedule;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SpringContextUtil;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.third.service.IFlow;

import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;


@Component
public class RunningProcessSync extends QuartzJobBean {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final ProcessDomain processDomain;

    @Autowired
    public RunningProcessSync(ProcessDomain processDomain) {
        this.processDomain = processDomain;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("processSync start : " + formatter.format(new Date()));
        List<String> runningProcess = processDomain.getRunningProcessAppId();
        if (CollectionUtils.isNotEmpty(runningProcess)) {


            for (String appId : runningProcess) {
                Future<?> future = ServicesExecutor.TASK_FUTURE.get(appId);
                if (null != future){
                    if (false == future.isDone()){
                        continue;
                    }
                    ServicesExecutor.TASK_FUTURE.remove(appId);
                }
                Future<?> submit = ServicesExecutor.getServicesExecutorServiceService().submit(new ProcessRunnable(appId));
                ServicesExecutor.TASK_FUTURE.put(appId, submit);
            }
        }
        logger.info("processSync end : " + formatter.format(new Date()));
    }

    @Getter
    class ProcessRunnable implements Runnable{
        private String appId;
        public ProcessRunnable(String appId){
            this.appId = appId;
        }
        @Override
        public void run() {
            try {
                IFlow getFlowInfoImpl = (IFlow) SpringContextUtil.getBean("flowImpl");
                getFlowInfoImpl.getProcessInfoAndSave(appId);
            } catch (Exception e) {
                logger.error("update process data error", e);
            }

        }
    }
}