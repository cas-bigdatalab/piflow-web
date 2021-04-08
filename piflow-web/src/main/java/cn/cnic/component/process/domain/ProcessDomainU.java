package cn.cnic.component.process.domain;

import java.util.List;

import javax.annotation.Resource;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomainU;
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
public class ProcessDomainU {

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
    private MxGraphModelDomainU mxGraphModelDomainU;

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
        String id = process.getId();
        if (StringUtils.isBlank(id)) {
            process.setId(UUIDUtils.getUUID32());
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
            for (ProcessPath processPath : processPathList) {
                if (null == processPath) {
                    continue;
                }
                processPath.setProcess(process);
                String processPathId = processPath.getId();
                if (StringUtils.isBlank(processPathId)) {
                    processPath.setId(UUIDUtils.getUUID32());
                }
            }
            addProcessPathCounts = processPathMapper.addProcessPathList(processPathList);
        }
        // Save Stop
        // Number of deposits in Stop
        int addProcessStopCounts = 0;
        int addProcessStopPropertyCounts = 0;
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            for (ProcessStop processStop : processStopList) {
                processStop.setProcess(process);
                String processStopId = processStop.getId();
                if (StringUtils.isBlank(processStopId)) {
                    processStop.setId(UUIDUtils.getUUID32());
                }
                int addProcessStop = processStopMapper.addProcessStop(processStop);
                if (addProcessStop < 0) {
                    throw new Exception("save failed");
                }
                List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                    for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                        if (null == processStopProperty) {
                            continue;
                        }
                        processStopProperty.setProcessStop(processStop);
                        String processStopPropertyId = processStopProperty.getId();
                        if (StringUtils.isBlank(processStopPropertyId)) {
                            processStopProperty.setId(UUIDUtils.getUUID32());
                        }
                        int addProcessStopProperty = processStopPropertyMapper.addProcessStopProperty(processStopProperty);
                        if (addProcessStopProperty < 0) {
                            throw new Exception("save failed");
                        }
                        addProcessStopPropertyCounts += addProcessStopProperty;
                    }
                }
            }
        }
        int addMxGraphModel = 0;
        MxGraphModel mxGraphModel = process.getMxGraphModel();
        if (null != mxGraphModel) {
            mxGraphModel.setProcess(process);
            addMxGraphModel = mxGraphModelDomainU.addMxGraphModel(mxGraphModel);
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
     * @param appId appId
     * @return Process
     */
    public Process getProcessByAppId(String username, boolean isAdmin, String appId) {
        if (StringUtils.isBlank(appId)) {
            logger.warn("appId id is null");
            return null;
        }
        Process processByAppId = processMapper.getProcessByAppId(appId);
        if (null == processByAppId) {
            logger.warn("data is null");
            return null;
        }
        return processByAppId;
    }

    /**
     * logically delete
     *
     * @param appId appId
     * @return Process
     */
    public String getProcessIdByAppId(String username, boolean isAdmin, String appId) {
        if (StringUtils.isBlank(appId)) {
            logger.warn("appId id is null");
            return null;
        }
        String processId = processMapper.getProcessIdByAppId(appId);
        if (null == processId) {
            logger.warn("data is null");
            return null;
        }
        return processId;
    }

    /**
     * logically delete
     *
     * @param id id
     * @return Process
     */
    public Process getProcessById(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            logger.warn("id is null");
            return null;
        }
        Process processById = processMapper.getProcessById(username, isAdmin, id);
        if (null == processById) {
            logger.warn("data is null");
            return null;
        }
        return processById;
    }

}
