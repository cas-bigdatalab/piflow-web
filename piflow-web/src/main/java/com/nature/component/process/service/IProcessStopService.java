package com.nature.component.process.service;

import com.nature.component.process.vo.ProcessStopVo;
import org.springframework.transaction.annotation.Transactional;

public interface IProcessStopService {

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Transactional
    public ProcessStopVo getProcessStopVoByPageId(String processId, String pageId);
}
