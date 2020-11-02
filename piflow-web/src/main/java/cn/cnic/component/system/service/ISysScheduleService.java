package cn.cnic.component.system.service;

import cn.cnic.component.system.vo.SysScheduleVo;
import org.springframework.stereotype.Service;


@Service
public interface ISysScheduleService {

    /**
     * Paging query schedule
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    public String getScheduleListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * Get schedule by id
     *
     * @param username
     * @param isAdmin
     * @param scheduleId
     * @return
     */
    public String getScheduleById(String username, boolean isAdmin, String scheduleId);

    /**
     * Add SysSchedule
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleVo
     * @return
     */
    public String createJob(String username, boolean isAdmin, SysScheduleVo sysScheduleVo);

    /**
     * Run once timed task
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleId
     * @return
     */
    public String runOnce(String username, boolean isAdmin, String sysScheduleId);

    /**
     * Start timed task
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleId
     * @return
     */
    public String startJob(String username, boolean isAdmin, String sysScheduleId);

    /**
     * Stop timed task
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleId
     * @return
     */
    public String stopJob(String username, boolean isAdmin, String sysScheduleId);

    /**
     * Pause timed task
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleId
     * @return
     */
    public String pauseJob(String username, boolean isAdmin, String sysScheduleId);

    /**
     * Resume timed task
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleId
     * @return
     */
    public String resume(String username, boolean isAdmin, String sysScheduleId);

    /**
     * Update timed task
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleVo
     * @return
     */
    public String update(String username, boolean isAdmin, SysScheduleVo sysScheduleVo);

    /**
     * Delete timed task
     *
     * @param username
     * @param isAdmin
     * @param sysScheduleId
     * @return
     */
    public String deleteTask(String username, boolean isAdmin, String sysScheduleId);

}
