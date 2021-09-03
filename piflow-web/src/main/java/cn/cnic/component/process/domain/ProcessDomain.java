package cn.cnic.component.process.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomain;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
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
public class ProcessDomain {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProcessPathMapper processPathMapper;

    @Autowired
    private ProcessStopMapper processStopMapper;

    @Autowired
    private ProcessStopPropertyMapper processStopPropertyMapper;

    @Autowired
    private MxGraphModelDomain mxGraphModelDomain;

    public int saveOrUpdate(Process process) throws Exception {
        if (null == process) {
            throw new Exception("save failed, process is null");
        }
        if (StringUtils.isBlank(process.getId())) {
            return addProcess(process);
        }
        return updateProcess(process);
    }

    /**
     * Add process of things
     *
     * @param process process
     * @return affected rows
     */
    public int addProcess(Process process) throws Exception {
        if (null == process) {
            throw new Exception("save failed");
        }
        String id = process.getId();
        if (StringUtils.isBlank(id)) {
            process.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = processMapper.addProcess(process);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        // save path
        // Number of save Paths
        List<ProcessPath> processPathList = process.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            for (ProcessPath processPath : processPathList) {
                if (null == processPath) {
                    continue;
                }
                processPath.setProcess(process);
                affectedRows += addProcessPath(processPath);
            }
        }
        // Save Stop
        // Number of deposits in Stop
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            for (ProcessStop processStop : processStopList) {
                processStop.setProcess(process);
                affectedRows += addProcessStop(processStop);
            }
        }
        MxGraphModel mxGraphModel = process.getMxGraphModel();
        if (null != mxGraphModel) {
            mxGraphModel.setProcess(process);
            affectedRows += mxGraphModelDomain.addMxGraphModel(mxGraphModel);
        }

        return affectedRows;
    }

    public int addProcessPath(ProcessPath processPath) throws Exception {
        if (null == processPath) {
            throw new Exception("save failed");
        }
        int affectedRows = processPathMapper.addProcessPath(processPath);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int addProcessPathList(List<ProcessPath> processPathList) throws Exception {
        return processPathMapper.addProcessPathList(processPathList);
    }

    public int addProcessStop(ProcessStop processStop) throws Exception {
        if (null == processStop) {
            throw new Exception("save failed, ProcessStop is null");
        }
        String processStopId = processStop.getId();
        if (StringUtils.isBlank(processStopId)) {
            processStop.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = processStopMapper.addProcessStop(processStop);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
        if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
            for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                if (null == processStopProperty) {
                    continue;
                }
                processStopProperty.setProcessStop(processStop);
                affectedRows += addProcessStopProperty(processStopProperty);
            }
        }
        return affectedRows;
    }

    public int addProcessStopList(List<ProcessStop> processStopList) throws Exception {
        return processStopMapper.addProcessStopList(processStopList);
    }

    public int addProcessStopProperty(ProcessStopProperty processStopProperty) throws Exception {
        if (null == processStopProperty) {
            throw new Exception("save failed");
        }
        int affectedRows = processStopPropertyMapper.addProcessStopProperty(processStopProperty);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    /**
     * The process of modifying things (just modify the process table)
     *
     * @param process process
     * @return affected rows
     * @throws Exception
     */
    public int updateProcess(Process process) throws Exception {
        int affectedRows = processMapper.updateProcess(process);
        affectedRows += updateProcessPathList(process.getProcessPathList());
        affectedRows += updateProcessStopList(process.getProcessStopList());
        return affectedRows;
    }

    public int updateProcessPath(ProcessPath processPath) {
        if (null == processPath) {
            return 0;
        }
        return processPathMapper.updateProcessPath(processPath);
    }

    public int updateProcessPathList(List<ProcessPath> processPathList) throws Exception {
        if (null == processPathList || processPathList.size() <= 0) {
            return 0;
        }
        int affectedRows = 0;
        for (ProcessPath processPath : processPathList) {
            if (null == processPath) {
                continue;
            }
            if (StringUtils.isBlank(processPath.getId())) {
                int addProcessPath = addProcessPath(processPath);
                if (addProcessPath <= 0) {
                    throw new Exception("save failed");
                }
                affectedRows += addProcessPath;
            } else {
                affectedRows += updateProcessPath(processPath);
            }
        }
        return affectedRows;
    }

    public int updateProcessStop(ProcessStop processStop) throws Exception {
        if (null == processStop) {
            return 0;
        }
        int affectedRows = 0;
        affectedRows += processStopMapper.updateProcessStop(processStop);
        affectedRows += updateProcessStopPropertyList(processStop.getProcessStopPropertyList());
        return affectedRows;
    }

    public int updateProcessStopList(List<ProcessStop> processStopList) throws Exception {
        if (null == processStopList || processStopList.size() <= 0) {
            return 0;
        }
        int affectedRows = 0;
        for (ProcessStop processStop : processStopList) {
            if (null == processStop) {
                continue;
            }
            if (StringUtils.isBlank(processStop.getId())) {
                int addProcessStop = addProcessStop(processStop);
                if (addProcessStop <= 0) {
                    throw new Exception("save failed");
                }
                affectedRows += addProcessStop;
            } else {
                affectedRows += updateProcessStop(processStop);
            }
        }
        return affectedRows;
    }

    public int updateProcessStopProperty(ProcessStopProperty processStopProperty) {
        if (null == processStopProperty) {
            return 0;
        }
        int affectedRows = processStopPropertyMapper.updateProcessStopProperty(processStopProperty);
        return affectedRows;
    }

    public int updateProcessStopPropertyList(List<ProcessStopProperty> processStopPropertyList) throws Exception {
        if (null == processStopPropertyList || processStopPropertyList.size() <= 0) {
            return 0;
        }
        int affectedRows = 0;
        for (ProcessStopProperty processStopProperty : processStopPropertyList) {
            if (null == processStopProperty) {
                continue;
            }
            if (StringUtils.isBlank(processStopProperty.getId())) {
                int addProcessStopProperty = addProcessStopProperty(processStopProperty);
                if (addProcessStopProperty <= 0) {
                    throw new Exception("save failed");
                }
                affectedRows += addProcessStopProperty;
            } else {
                affectedRows += updateProcessStopProperty(processStopProperty);
            }
        }
        return affectedRows;
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

    /**
     * getProcessNoGroupByAppId
     *
     * @param appid appId
     * @return Process
     */
    public Process getProcessNoGroupByAppId(String appId) {
        if (StringUtils.isBlank(appId)) {
            logger.warn("appId is null");
            return null;
        }

        Process processNoGroupByAppId = processMapper.getProcessNoGroupByAppId(appId);
        if (null == processNoGroupByAppId) {
            logger.warn("data is null");
            return null;
        }
        return processNoGroupByAppId;
    }

    /**
     * getProcessNoGroupByAppId
     *
     * @param appid appId
     * @return Process
     */
    public List<String> getRunningProcessAppId() {
        return processMapper.getRunningProcessAppId();
    }

    public ProcessState getProcessStateById(String id) {
        return processMapper.getProcessStateById(id);

    }

}
