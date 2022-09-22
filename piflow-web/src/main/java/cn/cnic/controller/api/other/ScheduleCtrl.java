package cn.cnic.controller.api.other;

import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.schedule.service.IScheduleService;
import cn.cnic.component.schedule.vo.ScheduleVo;
import io.swagger.annotations.Api;


@Api(value = "schedule api",tags = "schedule api")
@RestController
@RequestMapping("/schedule")
public class ScheduleCtrl {

    private final IScheduleService scheduleServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public ScheduleCtrl(IScheduleService scheduleServiceImpl, ILogHelperService logHelperServiceImpl) {
        this.scheduleServiceImpl = scheduleServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    /**
     * Query and enter the scheduleVo list
     *
     * @param page  page number
     * @param limit page size
     * @param param search param
     * @return json
     */
    @RequestMapping(value = "/getScheduleVoListPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getScheduleVoListPage", notes="get ScheduleVo list")
    public String getScheduleVoListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.getScheduleVoListPage(isAdmin, username, page, limit, param);
    }

    /**
     * create schedule
     *
     * @param scheduleVo
     * @return
     */
    @RequestMapping(value = "/addSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="addSchedule", notes="add Schedule")
    public String addSchedule(ScheduleVo scheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("addSchedule " + scheduleVo.getScheduleRunTemplateName(),username);
        return scheduleServiceImpl.addSchedule(username, scheduleVo);
    }

    /**
     * get Schedule by id
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/getScheduleById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getScheduleById", notes="get Schedule by id")
    public String getScheduleById(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.getScheduleVoById(isAdmin, username, scheduleId);
    }

    /**
     * update schedule
     *
     * @param scheduleVo
     * @return
     */
    @RequestMapping(value = "/updateSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateSchedule", notes="update Schedule")
    public String updateSchedule(ScheduleVo scheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("updateSchedule " + scheduleVo.getScheduleRunTemplateName(),username);
        return scheduleServiceImpl.updateSchedule(isAdmin, username, scheduleVo);
    }

    /**
     * del schedule
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/delSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="delSchedule", notes="delete Schedule")
    public String delSchedule(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("updateSchedule " + scheduleId,username);
        return scheduleServiceImpl.delSchedule(isAdmin, username, scheduleId);
    }


    /**
     * update schedule
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/startSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="startSchedule", notes="start Schedule")
    public String startSchedule(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.startSchedule(isAdmin, username, scheduleId);
    }

    /**
     * update schedule
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/stopSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="stopSchedule", notes="stop Schedule")
    public String stopSchedule(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.stopSchedule(isAdmin, username, scheduleId);
    }

}
