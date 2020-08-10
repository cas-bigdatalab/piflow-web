package cn.cnic.third;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.utils.ScheduleUtils;
import cn.cnic.third.service.ISchedule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;

import javax.annotation.Resource;

@Slf4j
public class IScheduleTest extends ApplicationTests {

    @Resource
    ISchedule scheduleImpl;

    @Test
    public void scheduleStartTest() {
        Schedule schedule = ScheduleUtils.newScheduleNoId("admin");
        schedule.setType("FLOW");
        schedule.setCronExpression("0 0/5 * * * ?");
        schedule.setScheduleRunTemplateId("0641076d5ae840c09d2be5b71fw00001");
        String scheduleId = scheduleImpl.scheduleStart("admin", schedule);
        log.info(scheduleId);
        log.info(scheduleId);
        log.info(scheduleId);
    }

    @Test
    public void scheduleStopTest() {
        String s = scheduleImpl.scheduleStop("schedule_badbf32a-674d-42a9-8e13-e91ae1539e01");
        log.info(s);
        log.info(s);
        log.info(s);
    }

    @Test
    public void scheduleInfoTest() {
        String s = scheduleImpl.scheduleInfo("schedule_9339f584-4ec5-4e12-a51d-4214182ff63a");
        log.info(s);
        log.info(s);
        log.info(s);

    }
}
