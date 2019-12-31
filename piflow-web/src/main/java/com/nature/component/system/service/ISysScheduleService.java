package com.nature.component.system.service;

import com.nature.component.system.vo.SysScheduleVo;
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
    public String getScheduleById(String scheduleId);

    /**
     * Add SysSchedule
     *
     * @param sysScheduleVo
     * @return
     */
    public String createJob(SysScheduleVo sysScheduleVo);

    /**
     * Run once timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String runOnce(String sysScheduleId);

    /**
     * Start timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String startJob(String sysScheduleId);

    /**
     * Stop timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String stopJob(String sysScheduleId);

    /**
     * Pause timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String pauseJob(String sysScheduleId);

    /**
     * Resume timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String resume(String sysScheduleId);

    /**
     * Update timed task
     *
     * @param sysScheduleVo
     * @return
     */
    public String update(SysScheduleVo sysScheduleVo);

    /**
     * Delete timed task
     *
     * @param sysScheduleId
     * @return
     */
    public String deleteTask(String sysScheduleId);

}
