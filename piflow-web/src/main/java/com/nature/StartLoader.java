package com.nature;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.QuartzUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ScheduleState;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.group.service.IStopGroupService;
import com.nature.component.system.model.SysSchedule;
import com.nature.domain.system.SysScheduleDomain;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Order(value = 1)
public class StartLoader implements ApplicationRunner {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private SysScheduleDomain sysScheduleDomain;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadStop();
        startStatusRunning();
    }

    private void loadStop() {
        if (SysParamsCache.IS_LOAD_STOP) {
            logger.warn(new Date() + ":Loading components");
            UserVo userVo = new UserVo();
            userVo.setUsername("system");
            stopGroupServiceImpl.addGroupAndStopsList(userVo);
            logger.warn(new Date() + ":Loading Component Completion");
        }

    }

    private void startStatusRunning() {
        List<SysSchedule> sysScheduleByStatusList = sysScheduleDomain.getSysScheduleByStatus(ScheduleState.RUNNING);
        if (null != sysScheduleByStatusList && sysScheduleByStatusList.size() > 0) {
            for (SysSchedule sysSchedule : sysScheduleByStatusList) {
                QuartzUtils.createScheduleJob(scheduler, sysSchedule);
            }
        }
    }
}
