package cn.cnic.third.service;

import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.third.vo.schedule.ThirdScheduleVo;

import java.util.Map;

public interface ISchedule {

    public Map<String, Object> scheduleStart(String username, Schedule schedule);

    public String scheduleStop(String scheduleId);

    public ThirdScheduleVo scheduleInfo(String scheduleId);
}
