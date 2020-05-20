package com.nature.component.process.service;

import com.nature.base.vo.UserVo;
import com.nature.component.process.vo.DebugDataRequest;
import com.nature.component.process.vo.DebugDataResponse;
import com.nature.component.process.vo.ProcessGroupVo;


public interface IProcessAndProcessGroupService {

    /**
     * Query ProcessAndProcessGroupVoList (parameter space-time non-paging)
     *
     * @param offset Number of pages
     * @param limit  Number each page
     * @param param  Search content
     * @return json
     */
    public String getProcessAndProcessGroupListPage(Integer offset, Integer limit, String param);

}
