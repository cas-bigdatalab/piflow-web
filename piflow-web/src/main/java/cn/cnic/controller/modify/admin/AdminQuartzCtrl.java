package cn.cnic.controller.modify.admin;

import cn.cnic.controller.modify.utils.UserUtils;
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
    public String getScheduleListPage(HttpServletRequest request, Integer page, Integer limit, String param) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.getScheduleListPage(username, true, page, limit, param);
    }

    @RequestMapping("/getScheduleById")
    @ResponseBody
    public String getScheduleById(HttpServletRequest request, String scheduleId) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.getScheduleById(username,true, scheduleId);
    }

    @RequestMapping("/createTask")
    @ResponseBody
    public String createTask(HttpServletRequest request, SysScheduleVo sysScheduleVo) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.createJob(username,true, sysScheduleVo);
    }

    @RequestMapping("/runOnceTask")
    @ResponseBody
    public String runOnceTask(HttpServletRequest request, String sysScheduleId) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.runOnce(username,true, sysScheduleId);
    }

    @RequestMapping("/startTask")
    @ResponseBody
    public String startTask(HttpServletRequest request, String sysScheduleId) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.startJob(username,true, sysScheduleId);
    }

    @RequestMapping("/stopTask")
    @ResponseBody
    public String stopTask(HttpServletRequest request, String sysScheduleId) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.stopJob(username, true,sysScheduleId);
    }

    @RequestMapping("/pauseTask")
    @ResponseBody
    public String pauseTask(HttpServletRequest request, String sysScheduleId) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.pauseJob(username, true,sysScheduleId);
    }

    @RequestMapping("/resumeTask")
    @ResponseBody
    public String resumeTask(HttpServletRequest request, String sysScheduleId) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.resume(username,true, sysScheduleId);
    }

    @RequestMapping("/updateTask")
    @ResponseBody
    public String updateTask(HttpServletRequest request, SysScheduleVo sysScheduleVo) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.update(username,true, sysScheduleVo);
    }

    @RequestMapping("/deleteTask")
    @ResponseBody
    public String deleteTask(HttpServletRequest request, String sysScheduleId) {
        String username = UserUtils.getUsername(request);
        return sysScheduleServiceImpl.deleteTask(username, true,sysScheduleId);
    }
}