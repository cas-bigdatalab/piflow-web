package com.nature.component.process.service.Impl;

import com.nature.base.util.LoggerUtil;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.service.IProcessStopService;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.mapper.process.ProcessStopMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProcessStopServiceImpl implements IProcessStopService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessStopMapper processStopMapper;

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Override
    public ProcessStopVo getProcessStopVoByPageId(String processId, String pageId) {
        if (StringUtils.isAnyEmpty(processId, pageId)) {
            return null;
        }
        ProcessStop processStopByPageId = processStopMapper.getProcessStopByPageIdAndPageId(processId, pageId);
        if (null == processStopByPageId) {
            return null;
        }
        return ProcessUtils.processStopPoToVo(processStopByPageId);
    }
}