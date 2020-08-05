package cn.cnic.controller;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.schedule.mapper.ScheduleMapper;
import cn.cnic.component.schedule.service.IScheduleService;
import cn.cnic.component.schedule.vo.ScheduleVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/schedule")
public class ScheduleCtrl {

    @Resource
    private IScheduleService scheduleServiceImpl;

    /**
     * Query and enter the scheduleVo list
     *
     * @param page page number
     * @param limit page size
     * @param param search param
     * @return json
     */
    @RequestMapping("/getScheduleVoListPage")
    @ResponseBody
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
    @RequestMapping("/addSchedule")
    @ResponseBody
    public String addSchedule(ScheduleVo scheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return scheduleServiceImpl.addSchedule(username, scheduleVo);
    }

}
