package com.nature.transaction.process;

import com.nature.base.util.LoggerUtil;
import com.nature.component.process.model.ProcessStop;
import com.nature.mapper.process.ProcessStopMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class ProcessStopTransaction {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessStopMapper processStopMapper;

    public ProcessStop getProcessStopByPageId(String processID, String pageID) {
        ProcessStop processStop = null;
        if (!StringUtils.isAnyEmpty(processID, pageID)) {
            processStop = processStopMapper.getProcessStopByPageIdAndPageId(processID, pageID);
        }
        return processStop;
    }

    public List<ProcessStop> getProcessStopByPageIds(String processID, String[] pageIDs) {
        List<ProcessStop> processStopList = null;
        if (StringUtils.isNotBlank(processID) && null != pageIDs && pageIDs.length > 0) {
            processStopList = processStopMapper.getProcessStopByPageIdAndPageIds(processID, pageIDs);
        }
        return processStopList;
    }

    public int updateProcessStop(ProcessStop processStop) {
        int updateProcessStop = 0;
        if (null != processStop) {
            updateProcessStop = processStopMapper.updateProcessStop(processStop);
        }
        return updateProcessStop;
    }

    /**
     * Query processStop according to processId and processStop name
     * @param processID
     * @param name
     * @return
     */
    public ProcessStop getProcessStopByNameAndPid(String processID, String name) {
        return processStopMapper.getProcessStopByNameAndPid(processID, name);
    }
}
