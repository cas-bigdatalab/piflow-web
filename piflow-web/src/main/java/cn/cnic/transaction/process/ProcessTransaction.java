package cn.cnic.transaction.process;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.process.model.Process;
import cn.cnic.component.process.model.ProcessPath;
import cn.cnic.component.process.model.ProcessStop;
import cn.cnic.component.process.model.ProcessStopProperty;
import cn.cnic.mapper.process.ProcessMapper;
import cn.cnic.mapper.process.ProcessPathMapper;
import cn.cnic.mapper.process.ProcessStopMapper;
import cn.cnic.mapper.process.ProcessStopPropertyMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class ProcessTransaction {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessPathMapper processPathMapper;

    @Resource
    private ProcessStopMapper processStopMapper;

    @Resource
    private ProcessStopPropertyMapper processStopPropertyMapper;

    /**
     * The process of modifying things (just modify the process table)
     *
     * @param process
     * @return
     */
    public int updateProcess(Process process) {
        int updateProcess = processMapper.updateProcess(process);
        return updateProcess;
    }

    /**
     * The process of modifying things (modifying the process table and all sub-tables)
     *
     * @param process
     * @return
     */
    public int updateProcessAll(Process process) {
        int influenceCounts = 0;
        int updateProcess = processMapper.updateProcess(process);
        if (updateProcess > 0) {
            influenceCounts += updateProcess;
            List<ProcessPath> processPathList = process.getProcessPathList();
            if (null != processPathList && processPathList.size() > 0) {
                for (ProcessPath processPath : processPathList) {
                    if (null != processPath) {
                        int updateProcessPath = processPathMapper.updateProcessPath(processPath);
                        influenceCounts += updateProcessPath;
                    }
                }
            }
            List<ProcessStop> processStopList = process.getProcessStopList();
            if (null != processStopList && processStopList.size() > 0) {
                for (ProcessStop processStop : processStopList) {
                    if (null != processStop) {
                        int updateProcessStop = processStopMapper.updateProcessStop(processStop);
                        influenceCounts += updateProcessStop;
                        List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                        if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                            for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                                if (null != processStopProperty) {
                                    int updateProcessStopProperty = processStopPropertyMapper.updateProcessStopProperty(processStopProperty);
                                    influenceCounts += updateProcessStopProperty;
                                }
                            }
                        }
                    }
                }
            }
        }
        return influenceCounts;
    }

    /**
     * logically delete
     *
     * @param processId
     * @return
     */
    public boolean updateProcessEnableFlag(String username, boolean isAdmin, String processId) {
        int affectedLine = 0;
        if (StringUtils.isBlank(processId) || StringUtils.isBlank(username)) {
            logger.info("Number of rows affected：" + affectedLine);
            return false;
        }
        Process processById = processMapper.getProcessById(username, isAdmin, processId);
        if (null == processById) {
            return false;
        }
        List<ProcessPath> processPathList = processById.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            int updateEnableFlagByProcessId = processPathMapper.updateEnableFlagByProcessId(processId, username);
            affectedLine += updateEnableFlagByProcessId;
        }
        List<ProcessStop> processStopList = processById.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            for (ProcessStop processStop : processStopList) {
                if (null != processStop) {
                    List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                    if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                        int updateEnableFlagByProcessStopId = processStopPropertyMapper.updateEnableFlagByProcessStopId(processStop.getId(), username);
                        affectedLine += updateEnableFlagByProcessStopId;
                    }
                }
            }
            int updateEnableFlagByProcessId = processStopMapper.updateEnableFlagByProcessId(processId, username);
            affectedLine += updateEnableFlagByProcessId;
        }
        int updateEnableFlag = processMapper.updateEnableFlag(processId, username);
        if (updateEnableFlag <= 0) {
            return false;
        }
        logger.info("Number of rows affected：" + affectedLine);
        return true;
    }

}
