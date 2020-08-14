package cn.cnic.third.service;

import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.third.vo.schedule.ThirdScheduleVo;

import java.util.Map;

public interface ISchedule {

    public Map<String, Object> scheduleStart(Schedule schedule, Process process, ProcessGroup processGroup);

    public String scheduleStop(String scheduleId);

    public ThirdScheduleVo scheduleInfo(String scheduleId);
}
