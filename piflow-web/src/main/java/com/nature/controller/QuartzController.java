package com.nature.controller;

import com.nature.base.util.QuartzUtils;
import com.nature.component.system.model.SysSchedule;
import com.nature.component.system.service.ISysScheduleService;
import com.nature.component.system.vo.SysScheduleVo;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sysSchedule/")
public class QuartzController {

    @Autowired
    private ISysScheduleService sysScheduleServiceImpl;

    @RequestMapping("/getScheduleListPage")
    @ResponseBody
    public String getScheduleListPage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
        return sysScheduleServiceImpl.getScheduleListPage(start / length + 1, length, extra_search);
    }

    @RequestMapping("/createTask")
    @ResponseBody
    public String createTask(SysScheduleVo sysScheduleVo) {
        return sysScheduleServiceImpl.createJob(sysScheduleVo);
    }

    @RequestMapping("/startTask")
    @ResponseBody
    public String startTask(String sysScheduleId) {
        return sysScheduleServiceImpl.startJob(sysScheduleId);
    }

    @RequestMapping("/runOnceTask")
    @ResponseBody
    public String runOnceTask(String sysScheduleId) {
        return sysScheduleServiceImpl.runOnce(sysScheduleId);
    }

    @RequestMapping("/pauseTask")
    @ResponseBody
    public String pauseTask(String sysScheduleId) {
        return sysScheduleServiceImpl.pauseJob(sysScheduleId);
    }

    @RequestMapping("/resumeTask")
    @ResponseBody
    public String resumeTask(String sysScheduleId) {
        return sysScheduleServiceImpl.resume(sysScheduleId);
    }

    @RequestMapping("/updateTask")
    @ResponseBody
    public String updateTask(SysScheduleVo sysScheduleVo) {
        return sysScheduleServiceImpl.update(sysScheduleVo);
    }
}