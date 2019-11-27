package com.nature.component.system.service;

import com.nature.component.system.vo.SysScheduleVo;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public String createJob(SysScheduleVo sysScheduleVo);

    public String runOnce(String sysScheduleId);
    public String startJob(String sysScheduleId);
    public String pauseJob(String sysScheduleId);
    public String resume(String sysScheduleId);
    public String update(SysScheduleVo sysScheduleVo);


}
