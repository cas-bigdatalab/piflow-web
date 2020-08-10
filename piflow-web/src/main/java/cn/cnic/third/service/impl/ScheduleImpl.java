package cn.cnic.third.service.impl;

import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.third.service.ISchedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleImpl implements ISchedule {
    @Override
    public String scheduleStart(Schedule schedule) {
        return null;
    }

    @Override
    public String scheduleStop(String scheduleId) {
        return null;
    }

    @Override
    public String scheduleInfo(String scheduleId) {
        return null;
    }
}
