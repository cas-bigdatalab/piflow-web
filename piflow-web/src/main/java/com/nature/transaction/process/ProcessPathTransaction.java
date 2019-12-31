package com.nature.transaction.process;

import com.nature.base.util.LoggerUtil;
import com.nature.component.process.model.ProcessPath;
import com.nature.mapper.process.ProcessPathMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class ProcessPathTransaction {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessPathMapper processPathMapper;

    public ProcessPath getProcessPathByPageId(String processID, String pageID) {
        ProcessPath processPath = null;
        if (!StringUtils.isAnyEmpty(processID, pageID)) {
            processPath = processPathMapper.getProcessPathByPageIdAndPid(processID, pageID);
        }
        return processPath;
    }
}
