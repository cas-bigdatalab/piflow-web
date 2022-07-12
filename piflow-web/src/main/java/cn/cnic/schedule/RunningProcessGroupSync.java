package cn.cnic.schedule;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SpringContextUtil;
import cn.cnic.base.utils.ThreadPoolExecutorUtils;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.mapper.ProcessGroupMapper;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.service.IGroup;

import lombok.SneakyThrows;
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
            if (null == SysParamsCache.MONITOR_THREAD_POOL_EXECUTOR) {
                SysParamsCache.MONITOR_THREAD_POOL_EXECUTOR = ThreadPoolExecutorUtils.createThreadPoolExecutor(1, 5, 0L);;
            }
            for (String groupId : runningProcessGroup) {
                SysParamsCache.MONITOR_THREAD_POOL_EXECUTOR.execute(() -> {
                    try {
                        IGroup groupImpl = (IGroup) SpringContextUtil.getBean("groupImpl");
                        groupImpl.updateFlowGroupByInterface(groupId);
                    } catch (Exception e) {
                        logger.error("update process group data error", e);
                    }
                });
            }
        }
        logger.info("processGroupSync end : " + formatter.format(new Date()));
    }
}