package cn.cnic.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.ISysScheduleService;
import cn.cnic.component.system.vo.SysScheduleVo;

import cn.cnic.component.user.service.LogHelper;


@Controller
@RequestMapping("/sysSchedule")
public class QuartzCtrl {

    @Autowired
    private ISysScheduleService sysScheduleServiceImpl;

    @Autowired
    private LogHelper logHelper;

    @RequestMapping("/getScheduleListPage")
    @ResponseBody
    public String getScheduleListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.getScheduleListPage(username, isAdmin, page, limit, param);
    }

    @RequestMapping(value = "/getScheduleById", method = RequestMethod.GET)
    @ResponseBody
    public String getScheduleById(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.getScheduleById(username, isAdmin, scheduleId);
    }

    @RequestMapping(value = "/createTask", method = RequestMethod.GET)
    @ResponseBody
    public String createTask(SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("create task",username);
        return sysScheduleServiceImpl.createJob(username, isAdmin, sysScheduleVo);
    }

    @RequestMapping(value = "/runOnceTask", method = RequestMethod.POST)
    @ResponseBody
    public String runOnceTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("run task " + sysScheduleId,username);
        return sysScheduleServiceImpl.runOnce(username, isAdmin, sysScheduleId);
    }

    @RequestMapping(value = "/startTask", method = RequestMethod.POST)
    @ResponseBody
    public String startTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("start task " + sysScheduleId,username);
        return sysScheduleServiceImpl.startJob(username, isAdmin, sysScheduleId);
    }

    @RequestMapping(value = "/stopTask", method = RequestMethod.POST)
    @ResponseBody
    public String stopTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("stop task " + sysScheduleId,username);
        return sysScheduleServiceImpl.stopJob(username, isAdmin, sysScheduleId);
    }

    @RequestMapping(value = "/pauseTask", method = RequestMethod.POST)
    @ResponseBody
    public String pauseTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("pause task " + sysScheduleId,username);
        return sysScheduleServiceImpl.pauseJob(username, isAdmin, sysScheduleId);
    }

    @RequestMapping(value = "/resumeTask", method = RequestMethod.POST)
    @ResponseBody
    public String resumeTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.resume(username, isAdmin, sysScheduleId);
    }

    @RequestMapping(value = "/updateTask", method = RequestMethod.GET)
    @ResponseBody
    public String updateTask(SysScheduleVo sysScheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.update(username, isAdmin, sysScheduleVo);
    }

    @RequestMapping(value = "/deleteTask", method = RequestMethod.GET)
    @ResponseBody
    public String deleteTask(String sysScheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysScheduleServiceImpl.deleteTask(username, isAdmin, sysScheduleId);
    }
}