package com.nature.transaction.process;

import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.StopState;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.component.process.vo.ProcessVo;
import com.nature.mapper.process.ProcessStopMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class ProcessStopTransaction {

    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessStopMapper processStopMapper;

    public ProcessStop getProcessStopByPageId(String processID, String pageID) {
        ProcessStop processStop = null;
        if (!StringUtils.isAnyEmpty(processID, pageID)) {
            processStop = processStopMapper.getProcessStopByPageIdAndPid(processID, pageID);
        }
        return processStop;
    }

    public ProcessStop updateProcessStopByProcessId(ProcessStopVo processStopVo) {
        ProcessStop processStop = null;
        if (null != processStopVo) {
            ProcessVo processVo = processStopVo.getProcessVo();
            String name = processStopVo.getName();
            if (StringUtils.isNotBlank(name) && null != processVo) {
                String processVoID = processVo.getId();
                if (StringUtils.isNotBlank(processVoID)) {
                    processStop = processStopMapper.getProcessStopByNameAndPid(processVoID, name);
                    if (null != processStop) {
                        processStop.setName(processStopVo.getName());
                        processStop.setState(StopState.selectGender(processStopVo.getState()));
                        processStop.setStartTime(processStopVo.getStartTime());
                        processStop.setEndTime(processStopVo.getEndTime());
                        int updateProcessStop = processStopMapper.updateProcessStop(processStop);
                        if (updateProcessStop <= 0) {
                            processStop = null;
                        }
                    }
                }
            }

        }
        return processStop;
    }
}
