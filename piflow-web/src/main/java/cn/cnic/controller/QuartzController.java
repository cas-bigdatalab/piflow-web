package cn.cnic.controller;

import cn.cnic.base.util.SessionUserUtil;
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
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.getScheduleById(isAdmin, scheduleId);
    }

    @RequestMapping("/createTask")
    @ResponseBody
    public String createTask(SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.createJob(username, sysScheduleVo);
    }

    @RequestMapping("/runOnceTask")
    @ResponseBody
    public String runOnceTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.runOnce(username, sysScheduleId);
    }

    @RequestMapping("/startTask")
    @ResponseBody
    public String startTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.startJob(username, sysScheduleId);
    }

    @RequestMapping("/stopTask")
    @ResponseBody
    public String stopTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.stopJob(username, sysScheduleId);
    }

    @RequestMapping("/pauseTask")
    @ResponseBody
    public String pauseTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.pauseJob(username, sysScheduleId);
    }

    @RequestMapping("/resumeTask")
    @ResponseBody
    public String resumeTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.resume(username, sysScheduleId);
    }

    @RequestMapping("/updateTask")
    @ResponseBody
    public String updateTask(SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.update(username, sysScheduleVo);
    }

    @RequestMapping("/deleteTask")
    @ResponseBody
    public String deleteTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        return sysScheduleServiceImpl.deleteTask(username, sysScheduleId);
    }
}