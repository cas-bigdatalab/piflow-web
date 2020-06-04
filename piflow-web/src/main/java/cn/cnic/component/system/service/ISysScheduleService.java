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
    public String getScheduleListPage(Integer offset, Integer limit, String param);

    /**
     * Get schedule by id
     *
     * @param scheduleId
     * @return
     */
    public String getScheduleById(boolean isAdmin, String scheduleId);

    /**
     * Add SysSchedule
     *
     * @param sysScheduleVo
     * @return
     */
    public String createJob(String username, SysScheduleVo sysScheduleVo);

    /**
     * Run once timed task
     *
     * @param username
     * @param sysScheduleId
     * @return
     */
    public String runOnce(String username, String sysScheduleId);

    /**
     * Start timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String startJob(String username, String sysScheduleId);

    /**
     * Stop timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String stopJob(String username, String sysScheduleId);

    /**
     * Pause timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String pauseJob(String username, String sysScheduleId);

    /**
     * Resume timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String resume(String username, String sysScheduleId);

    /**
     * Update timed task
     *
     * @param sysScheduleVo
     * @return
     */
    public String update(String username, SysScheduleVo sysScheduleVo);

    /**
     * Delete timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String deleteTask(String username, String sysScheduleId);

}
