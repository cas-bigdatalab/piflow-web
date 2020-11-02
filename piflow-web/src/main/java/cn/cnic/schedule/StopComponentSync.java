package cn.cnic.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.cnic.component.stopsComponent.service.IStopGroupService;


public class StopComponentSync extends QuartzJobBean {

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        stopGroupServiceImpl.updateGroupAndStopsListByServer("systemSync");
    }
}