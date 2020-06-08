package cn.cnic.controller.modify.admin;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.system.service.ISysScheduleService;
import cn.cnic.component.system.vo.SysScheduleVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/sysSchedule")
public class AdminQuartzCtrl {

    @Resource
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
    public String createTask(HttpServletRequest request, SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.createJob(username, sysScheduleVo);
    }

    @RequestMapping("/runOnceTask")
    @ResponseBody
    public String runOnceTask(HttpServletRequest request, String sysScheduleId) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.runOnce(username, sysScheduleId);
    }

    @RequestMapping("/startTask")
    @ResponseBody
    public String startTask(HttpServletRequest request, String sysScheduleId) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.startJob(username, sysScheduleId);
    }

    @RequestMapping("/stopTask")
    @ResponseBody
    public String stopTask(HttpServletRequest request, String sysScheduleId) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.stopJob(username, sysScheduleId);
    }

    @RequestMapping("/pauseTask")
    @ResponseBody
    public String pauseTask(HttpServletRequest request, String sysScheduleId) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.pauseJob(username, sysScheduleId);
    }

    @RequestMapping("/resumeTask")
    @ResponseBody
    public String resumeTask(HttpServletRequest request, String sysScheduleId) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.resume(username, sysScheduleId);
    }

    @RequestMapping("/updateTask")
    @ResponseBody
    public String updateTask(HttpServletRequest request, SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.update(username, sysScheduleVo);
    }

    @RequestMapping("/deleteTask")
    @ResponseBody
    public String deleteTask(HttpServletRequest request, String sysScheduleId) {
        String username = SessionUserUtil.getUsername(request);
        return sysScheduleServiceImpl.deleteTask(username, sysScheduleId);
    }
}