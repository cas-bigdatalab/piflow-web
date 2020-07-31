package cn.cnic;

import cn.cnic.base.util.CheckPathUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.QuartzUtils;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.system.entity.SysSchedule;
import cn.cnic.component.system.mapper.SysScheduleMapper;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Order(value = 1)
public class StartLoader implements ApplicationRunner {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysScheduleMapper sysScheduleMapper;

    @Resource
    private Scheduler scheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkStoragePath();
        startStatusRunning();
    }

    private void checkStoragePath() {
        String storagePathHead = System.getProperty("user.dir");
        logger.warn(storagePathHead);
        CheckPathUtils.isChartPathExist(storagePathHead + "/storage/image/");
        CheckPathUtils.isChartPathExist(storagePathHead + "/storage/video/");
        CheckPathUtils.isChartPathExist(storagePathHead + "/storage/xml/");
        SysParamsCache.setImagesPath(storagePathHead + "/storage/image/");
        SysParamsCache.setVideosPath(storagePathHead + "/storage/video/");
        SysParamsCache.setXmlPath(storagePathHead + "/storage/xml/");
    }

    private void startStatusRunning() {
        List<SysSchedule> sysScheduleByStatusList = sysScheduleMapper.getSysScheduleListByStatus(true, ScheduleState.RUNNING);
        if (null != sysScheduleByStatusList && sysScheduleByStatusList.size() > 0) {
            for (SysSchedule sysSchedule : sysScheduleByStatusList) {
                QuartzUtils.createScheduleJob(scheduler, sysSchedule);
            }
        }
    }


    public static void main(String[] args) {
        String s0 = "[]";
        String s1 = "[asfd,asdf,asdf]";
        String[] split = s1.split(",");
        System.out.println(split.length);
        String index0 = s0.substring(1, s0.length() - 1);
        System.out.println("--------  " + index0 + "  --------");
        String index1 = s1.substring(1, s1.length() - 1);
        System.out.println("--------  " + index1 + "  --------");
    }
}
