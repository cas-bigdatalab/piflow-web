package cn.cnic.controller;

import cn.cnic.component.system.service.ISysScheduleService;
import cn.cnic.component.system.vo.SysScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sysSchedule")
public class QuartzController {

    @Autowired
    private ISysScheduleService sysScheduleServiceImpl;

    @RequestMapping("/getScheduleListPage")
    @ResponseBody
    public String getScheduleListPage(Integer page, Integer limit, String param) {
        return sysScheduleServiceImpl.getScheduleListPage(page, limit, param);
    }

    @RequestMapping("/getScheduleById")
    @ResponseBody
    public String getScheduleById(HttpServletRequest request, String scheduleId) {
        return sysScheduleServiceImpl.getScheduleById(scheduleId);
    }

    @RequestMapping("/createTask")
    @ResponseBody
    public String createTask(SysScheduleVo sysScheduleVo) {
        return sysScheduleServiceImpl.createJob(sysScheduleVo);
    }

    @RequestMapping("/runOnceTask")
    @ResponseBody
    public String runOnceTask(String sysScheduleId) {
        return sysScheduleServiceImpl.runOnce(sysScheduleId);
    }

    @RequestMapping("/startTask")
    @ResponseBody
    public String startTask(String sysScheduleId) {
        return sysScheduleServiceImpl.startJob(sysScheduleId);
    }

    @RequestMapping("/stopTask")
    @ResponseBody
    public String stopTask(String sysScheduleId) {
        return sysScheduleServiceImpl.stopJob(sysScheduleId);
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

    @RequestMapping("/deleteTask")
    @ResponseBody
    public String deleteTask(String sysScheduleId) {
        return sysScheduleServiceImpl.deleteTask(sysScheduleId);
    }
}