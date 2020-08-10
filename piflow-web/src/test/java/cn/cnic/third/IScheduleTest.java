package cn.cnic.third;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.utils.ScheduleUtils;
import cn.cnic.third.service.ISchedule;
import cn.cnic.third.vo.schedule.ThirdScheduleVo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.Map;

public class IScheduleTest extends ApplicationTests {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    ISchedule scheduleImpl;

    @Test
    public void scheduleStartTest() {
        Schedule schedule = ScheduleUtils.newScheduleNoId("admin");
        schedule.setType("FLOW");
        schedule.setCronExpression("0 0/5 * * * ?");
        schedule.setScheduleRunTemplateId("0641076d5ae840c09d2be5b71fw00001");
        Map<String, Object> scheduleId = scheduleImpl.scheduleStart("admin", schedule);
        logger.info(scheduleId.get("scheduleId").toString());
    }

    @Test
    public void scheduleStopTest() {
        String s = scheduleImpl.scheduleStop("schedule_b1a7c252-a22e-4f54-868e-fced68cf2d43");
        if (StringUtils.isNotBlank(s) && s.contains("ok!")) {
            logger.info(s);
        } else {
            logger.info("failed");
        }
    }

    @Test
    public void scheduleInfoTest() {
        ThirdScheduleVo s = scheduleImpl.scheduleInfo("schedule_b1a7c252-a22e-4f54-868e-fced68cf2d43");
        logger.info(s.toString());


    }
}
