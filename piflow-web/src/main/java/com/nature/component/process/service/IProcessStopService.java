package com.nature.component.process.service;

import com.nature.component.process.vo.ProcessStopVo;
import org.springframework.data.annotation.Transient;

public interface IProcessStopService {

    /**
     * 根据processId和pageId查询processStop
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Transient
    public ProcessStopVo getProcessStopVoByPageId(String processId, String pageId);
}
