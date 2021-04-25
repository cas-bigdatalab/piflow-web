package cn.cnic.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.system.service.ISysScheduleService;
import cn.cnic.component.system.vo.SysScheduleVo;

@Controller
@RequestMapping("/sysSchedule")
public class QuartzCtrl {

    @Autowired
    private ISysScheduleService sysScheduleServiceImpl;

    @RequestMapping("/getScheduleListPage")
    @ResponseBody
    public String getScheduleListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.getScheduleListPage(username, isAdmin, page, limit, param);
    }

    @RequestMapping("/getScheduleById")
    @ResponseBody
    public String getScheduleById(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.getScheduleById(username, isAdmin, scheduleId);
    }

    @RequestMapping("/createTask")
    @ResponseBody
    public String createTask(SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.createJob(username, isAdmin, sysScheduleVo);
    }

    @RequestMapping("/runOnceTask")
    @ResponseBody
    public String runOnceTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.runOnce(username, isAdmin, sysScheduleId);
    }

    @RequestMapping("/startTask")
    @ResponseBody
    public String startTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.startJob(username, isAdmin, sysScheduleId);
    }

    @RequestMapping("/stopTask")
    @ResponseBody
    public String stopTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.stopJob(username, isAdmin, sysScheduleId);
    }

    @RequestMapping("/pauseTask")
    @ResponseBody
    public String pauseTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.pauseJob(username, isAdmin, sysScheduleId);
    }

    @RequestMapping("/resumeTask")
    @ResponseBody
    public String resumeTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.resume(username, isAdmin, sysScheduleId);
    }

    @RequestMapping("/updateTask")
    @ResponseBody
    public String updateTask(SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.update(username, isAdmin, sysScheduleVo);
    }

    @RequestMapping("/deleteTask")
    @ResponseBody
    public String deleteTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.deleteTask(username, isAdmin, sysScheduleId);
    }
}