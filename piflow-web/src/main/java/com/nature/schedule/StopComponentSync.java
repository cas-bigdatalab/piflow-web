package com.nature.schedule;

import com.nature.base.vo.UserVo;
import com.nature.component.group.service.IStopGroupService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class StopComponentSync extends QuartzJobBean {

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        UserVo userVo = new UserVo();
        userVo.setUsername("systemSync");
        stopGroupServiceImpl.addGroupAndStopsList(userVo);
    }
}