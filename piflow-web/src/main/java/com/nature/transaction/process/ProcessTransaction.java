package com.nature.transaction.process;

import com.nature.base.util.LoggerUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopProperty;
import com.nature.component.process.vo.ProcessVo;
import com.nature.mapper.process.ProcessMapper;
import com.nature.mapper.process.ProcessPathMapper;
import com.nature.mapper.process.ProcessStopMapper;
import com.nature.mapper.process.ProcessStopPropertyMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class ProcessTransaction {

    /**
     * 引入日志，注意都是"org.slf4j"包下
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
     * 事物的添加process
     *
     * @param process
     * @return
     */
    public int addProcess(Process process) {
        int influenceCounts = 0;
        if (null != process) {
            int addProcessCounts = processMapper.addProcess(process);
            if (addProcessCounts > 0) {
                // 存线
                // 存入线的数目
                int addProcessPathCounts = 0;
                List<ProcessPath> processPathList = process.getProcessPathList();
                if (null != processPathList && processPathList.size() > 0) {
                    addProcessPathCounts = processPathMapper.addProcessPathList(processPathList);
                }
                // 存stop
                // 存入stop的数目
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
                influenceCounts = (addProcessPathCounts + addProcessStopCounts + addProcessStopPropertyCounts);
            }
        }
        return influenceCounts;
    }

    /**
     * 事物的修改process(仅仅修改process表)
     *
     * @param process
     * @return
     */
    public int updateProcess(Process process) {
        int updateProcess = processMapper.updateProcess(process);
        return updateProcess;
    }

    /**
     * 事物的修改process(修改process表及所有子表)
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
     * 根据Id查询Process
     *
     * @param id
     * @return
     */
    public Process getProcessById(String id) {
        Process processById = processMapper.getProcessById(id);
        return processById;
    }

    /**
     * 查询ProcessList
     *
     * @return
     */
    public List<Process> getProcessList() {
        List<Process> processList = null;
        processList = processMapper.getProcessList();
        return processList;
    }

    /**
     * 查询ProcessVoList
     *
     * @return
     */
    public List<ProcessVo> getProcessVoList() {
        List<Process> processList = null;
        processList = processMapper.getProcessList();
        List<ProcessVo> processVoList = new ArrayList<ProcessVo>();
        if (null != processList && processList.size() > 0) {
            for (Process process : processList) {
                ProcessVo processVo = processPoToVo(process);
                if (null != processVo) {
                    processVoList.add(processVo);
                }
            }
        }
        return processVoList;
    }

    /**
     * 查询ProcessVoList
     *
     * @return
     */
    public List<Process> getProcessListByParam(String param) {
        return processMapper.getProcessListByParam(param);
    }

    /**
     * 根据进程AppId查询进程
     *
     * @param appID
     * @return
     */
    public Process getProcessByAppId(String appID) {
        return processMapper.getProcessByAppId(appID);
    }

    /**
     * 根据进程AppId查询进程
     *
     * @param appID
     * @return
     */
    public List<Process> getProcessListByAppIDs(String[] appID) {
        return processMapper.getProcessListByAppIDs(appID);
    }

    /**
     * 逻辑删除
     *
     * @param processId
     * @return
     */
    public boolean updateProcessEnableFlag(String processId, UserVo currentUser) {
        int affectedLine = 0;
        if (StringUtils.isNotBlank(processId) && null != currentUser) {
            Process processById = processMapper.getProcessById(processId);
            if (null != processById) {
                List<ProcessPath> processPathList = processById.getProcessPathList();
                String username = currentUser.getUsername();
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
                if (updateEnableFlag > 0) {
                    logger.info("影响行数：" + affectedLine);
                    return true;
                }
            }
        }
        logger.info("影响行数：" + affectedLine);
        return false;
    }

    public List<ProcessVo> getRunningProcessList(String flowId) {
        List<ProcessVo> processVoList = null;
        List<Process> processList = processMapper.getRunningProcessList(flowId);
        if (CollectionUtils.isNotEmpty(processList)) {
            processVoList = new ArrayList<ProcessVo>();
            for (Process process : processList) {
                ProcessVo processVo = processPoToVo(process);
                if (null != processVo) {
                    processVoList.add(processVo);
                }
            }
        }
        return processVoList;
    }

    private ProcessVo processPoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setCrtDttm(process.getCrtDttm());
        }
        return processVo;
    }


}
