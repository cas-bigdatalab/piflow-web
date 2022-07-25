package cn.cnic.schedule;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SpringContextUtil;
import cn.cnic.base.utils.ThreadPoolExecutorUtils;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.mapper.ProcessGroupMapper;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.service.IGroup;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


@Component
public class RunningProcessGroupSync extends QuartzJobBean {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessGroupMapper processGroupMapper;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        logger.info("processGroupSync start : " + formatter.format(new Date()));
        List<String> runningProcessGroup = processGroupMapper.getRunningProcessGroupAppId();
        if (CollectionUtils.isNotEmpty(runningProcessGroup)) {
            for (String groupId : runningProcessGroup) {
                Future<?> future = ServicesExecutor.TASK_FUTURE.get(groupId);
                if (null != future){
                    if (false == future.isDone()){
                        continue;
                    }
                    ServicesExecutor.TASK_FUTURE.remove(groupId);
                }
                Future<?> submit = ServicesExecutor.getServicesExecutorServiceService().submit(new ProcessGroupCallable(groupId));
                ServicesExecutor.TASK_FUTURE.put(groupId, submit);
            }

        }
        logger.info("processGroupSync end : " + formatter.format(new Date()));
    }

    class ProcessGroupCallable implements Callable<String> {
        private String groupId;

        public ProcessGroupCallable(String groupId) {
            this.groupId = groupId;
        }

        @Override
        public String call() throws Exception {
            IGroup groupImpl = (IGroup) SpringContextUtil.getBean("groupImpl");
            groupImpl.updateFlowGroupByInterface(groupId);
            return null;
        }
    }

}