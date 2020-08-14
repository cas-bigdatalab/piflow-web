package cn.cnic.component.process.transaction;

import java.util.List;

import javax.annotation.Resource;

import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.transaction.MxGraphModelTransaction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.component.process.mapper.ProcessPathMapper;
import cn.cnic.component.process.mapper.ProcessStopMapper;
import cn.cnic.component.process.mapper.ProcessStopPropertyMapper;

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

    @Resource
    private MxGraphModelTransaction mxGraphModelTransaction;

    /**
     * Add process of things
     *
     * @param process process
     * @return affected rows
     */
    public int addProcess(Process process) throws Exception {
        if (null == process) {
            return 0;
        }
        int addProcessCounts = processMapper.addProcess(process);
        if (addProcessCounts <= 0) {
            throw new Exception("save failed");
        }
        // save path
        // Number of save Paths
        int addProcessPathCounts = 0;
        List<ProcessPath> processPathList = process.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            addProcessPathCounts = processPathMapper.addProcessPathList(processPathList);
        }
        // Save Stop
        // Number of deposits in Stop
        int addProcessStopCounts = 0;
        int addProcessStopPropertyCounts = 0;
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            addProcessStopCounts = processStopMapper.addProcessStopList(processStopList);
            for (ProcessStop processStop : processStopList) {
                List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                    addProcessStopPropertyCounts += processStopPropertyMapper.addProcessStopProperties(processStopPropertyList);
                }
            }
        }
        int addMxGraphModel = 0;
        MxGraphModel mxGraphModel = process.getMxGraphModel();
        if (null != mxGraphModel) {
            addMxGraphModel = mxGraphModelTransaction.addMxGraphModel(mxGraphModel);
        }
        int influenceCounts = (addProcessPathCounts + addProcessStopCounts + addProcessStopPropertyCounts + addMxGraphModel);

        return influenceCounts;
    }

    /**
     * The process of modifying things (just modify the process table)
     *
     * @param process process
     * @return affected rows
     */
    public int updateProcess(Process process) {
        return processMapper.updateProcess(process);
    }

    /**
     * The process of modifying things (modifying the process table and all sub-tables)
     *
     * @param process process
     * @return affected rows
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
     * @param processId processId
     * @return affected rows
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

    /**
     * logically delete
     *
     * @param processId processId
     * @return affected rows
     */
    public Process getProcessById(String username, boolean isAdmin, String processId) {
        if (StringUtils.isBlank(processId) || StringUtils.isBlank(username)) {
            logger.warn("process id is null");
            return null;
        }
        int affectedLine = 0;

        Process processById = processMapper.getProcessById(username, isAdmin, processId);
        if (null == processById) {
            logger.warn("data is null");
            return null;
        }
        return processById;
    }

}
