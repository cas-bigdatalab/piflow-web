package com.nature.component.process.service;

import com.nature.component.process.vo.ProcessPathVo;
import org.springframework.data.annotation.Transient;

public interface IProcessPathService {

    /**
     * 根据processId和pageId查询processPath
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Transient
    public ProcessPathVo getProcessPathVoByPageId(String processId, String pageId);
}
