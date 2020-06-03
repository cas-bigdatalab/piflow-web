package cn.cnic.schedule;

import cn.cnic.base.vo.UserVo;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class StopComponentSync extends QuartzJobBean {

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        stopGroupServiceImpl.updateGroupAndStopsListByServer("systemSync");
    }
}