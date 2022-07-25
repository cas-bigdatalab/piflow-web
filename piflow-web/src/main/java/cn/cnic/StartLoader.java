package cn.cnic;

import cn.cnic.base.utils.CheckPathUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.QuartzUtils;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.system.entity.SysSchedule;
import cn.cnic.component.system.mapper.SysScheduleMapper;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Order(value = 1)
public class StartLoader implements ApplicationRunner {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final SysScheduleMapper sysScheduleMapper;
    private final Scheduler scheduler;

    @Autowired
    public StartLoader(SysScheduleMapper sysScheduleMapper, Scheduler scheduler) {
        this.sysScheduleMapper = sysScheduleMapper;
        this.scheduler = scheduler;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkStoragePath();
        startStatusRunning();
    }

    private void checkStoragePath() {
        CheckPathUtils.isChartPathExist(SysParamsCache.IMAGES_PATH);
        CheckPathUtils.isChartPathExist(SysParamsCache.VIDEOS_PATH);
        CheckPathUtils.isChartPathExist(SysParamsCache.XML_PATH);
        CheckPathUtils.isChartPathExist(SysParamsCache.CSV_PATH);
    }

    private void startStatusRunning() {
        List<SysSchedule> sysScheduleByStatusList = sysScheduleMapper.getSysScheduleListByStatus(true, ScheduleState.RUNNING);
        if (null != sysScheduleByStatusList && sysScheduleByStatusList.size() > 0) {
            for (SysSchedule sysSchedule : sysScheduleByStatusList) {
                JobDetail scheduleJobByJobName = QuartzUtils.getScheduleJobByJobName(scheduler, sysSchedule.getJobName());
                if (null != scheduleJobByJobName) {
                    continue;
                }
                QuartzUtils.createScheduleJob(scheduler, sysSchedule);
            }
        }
    }

}
