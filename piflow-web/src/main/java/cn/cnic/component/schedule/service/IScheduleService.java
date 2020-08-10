package cn.cnic.component.schedule.service;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.PageHelperUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.vo.ScheduleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.Map;

public interface IScheduleService {

    /**
     * Query getScheduleListPage (parameter space-time non-paging)
     *
     * @param isAdmin is admin
     * @param username   username
     * @param offset Number of pages
     * @param limit  Number each page
     * @param param  Search content
     * @return json
     */
    public String getScheduleVoListPage(boolean isAdmin, String username, Integer offset, Integer limit, String param);

    /**
     * Add schedule
     *
     * @param username   username
     * @param scheduleVo scheduleVo
     * @return json
     */
    public String addSchedule(String username, ScheduleVo scheduleVo);

    /**
     * get ScheduleVo by id
     *
     * @param isAdmin is admin
     * @param username username
     * @param id       schedule id
     * @return json
     */
    public String getScheduleVoById(boolean isAdmin, String username, String id);

    /**
     * Update schedule
     *
     * @param isAdmin is admin
     * @param username   username
     * @param scheduleVo scheduleVo
     * @return json
     */
    public String updateSchedule(boolean isAdmin, String username, ScheduleVo scheduleVo);

    /**
     * Delete schedule
     *
     * @param isAdmin is admin
     * @param username username
     * @param id       schedule id
     * @return json
     */
    public String delSchedule(boolean isAdmin, String username, String id);

    /**
     *
     * @param isAdmin is admin
     * @param username username
     * @param id schedule id
     * @return json
     */
    public String startSchedule(boolean isAdmin, String username, String id);

    /**
     *
     * @param isAdmin is admin
     * @param username username
     * @param id schedule id
     * @return json
     */
    public String stopSchedule(boolean isAdmin, String username, String id);

}
