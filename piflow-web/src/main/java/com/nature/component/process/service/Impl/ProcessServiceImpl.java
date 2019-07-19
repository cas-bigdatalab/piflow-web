package com.nature.component.process.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.StopState;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopProperty;
import com.nature.component.process.service.IProcessService;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.component.process.vo.ProcessPathVo;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.component.process.vo.ProcessVo;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.inf.IStartFlow;
import com.nature.third.inf.IStopFlow;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import com.nature.transaction.flow.FlowTransaction;
import com.nature.transaction.process.ProcessStopTransaction;
import com.nature.transaction.process.ProcessTransaction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ProcessServiceImpl implements IProcessService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessTransaction processTransaction;

    @Resource
    ProcessStopTransaction processStopTransaction;

    @Resource
    private FlowTransaction flowTransaction;

    @Resource
    private IStartFlow startFlowImpl;

    @Resource
    private IStopFlow stopFlow;

    @Resource
    private IGetFlowInfo getFlowInfoImpl;

    @Resource
    IGetFlowProgress getFlowProgressImpl;

    /**
     * 查询processVoList(查询包含其子表)
     *
     * @return
     */
    @Override
    public List<ProcessVo> getProcessAllVoList() {
        List<ProcessVo> processVoList = null;
        List<Process> processList = processTransaction.getProcessList();
        if (null != processList && processList.size() > 0) {
            for (Process process : processList) {
                if (null != process) {
                    ProcessVo processVo = this.processPoToVo(process);
                    processVo.setCrtDttm(process.getCrtDttm());
                    processVoList.add(processVo);
                }
            }
        }
        return processVoList;
    }

    /**
     * 查询processVoList(只查询process表)
     *
     * @return
     */
    @Override
    public List<ProcessVo> getProcessVoList() {
        List<ProcessVo> processVoList = null;
        List<Process> processList = processTransaction.getProcessList();
        if (null != processList && processList.size() > 0) {
            processVoList = new ArrayList<ProcessVo>();
            for (Process process : processList) {
                if (null != process) {
                    ProcessVo processVo = new ProcessVo();
                    BeanUtils.copyProperties(process, processVo);
                    processVo.setCrtDttm(process.getCrtDttm());
                    processVoList.add(processVo);
                }
            }
        }
        return processVoList;
    }

    /**
     * 查询processVo根据id(查询包含其子表)
     *
     * @param id
     * @return
     */
    @Override
    public ProcessVo getProcessAllVoById(String id) {
        ProcessVo processVo = null;
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (StringUtils.isNotBlank(id)) {
            Process processById = processTransaction.getProcessById(currentUser, id);
            processVo = this.processPoToVo(processById);
        }
        return processVo;
    }

    /**
     * 查询processVo根据id(只查询process表)
     *
     * @param id
     * @return
     */
    @Override
    public ProcessVo getProcessVoById(String id) {
        ProcessVo processVo = null;
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (StringUtils.isNotBlank(id)) {
            Process processById = processTransaction.getProcessById(currentUser, id);
            if (null != processById) {
                processVo = new ProcessVo();
                BeanUtils.copyProperties(processById, processVo);
                processVo.setCrtDttm(processById.getCrtDttm());
            }
        }
        return processVo;
    }

    /**
     * 查询process根据id
     *
     * @param id
     * @return
     */
    @Override
    public Process getProcessById(String id) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        Process processById = processTransaction.getProcessById(currentUser, id);
        return processById;
    }

    /**
     * 根据Appid查询process
     *
     * @param appId
     * @return
     */
    @Override
    public ProcessVo getProcessVoByAppId(String appId) {
        ProcessVo processVo = null;
        if (StringUtils.isNotBlank(appId)) {
            Process processById = processTransaction.getProcessByAppId(appId);
            if (null != processById) {
                processVo = this.processPoToVo(processById);
            }
        }
        return processVo;
    }

    /**
     * 根据数组Appid查询process
     *
     * @param appIDs
     * @return
     */
    public List<ProcessVo> getProcessVoByAppIds(String appIDs) {
        return null;
    }

    /**
     * 根据appID在第三方接口查询appInfo并保存
     *
     * @param appID
     * @return
     */
    @Override
    public ProcessVo getAppInfoByThirdAndSave(String appID) {
        ProcessVo processVo = new ProcessVo();
        Process processById = processTransaction.getProcessByAppId(appID);
        if (null != processById) {
            // 如果状态为STARTED，去调接口,否则说明已经是启动完成,直接返回
            ProcessState state = processById.getState();
            if (ProcessState.STARTED == state || null == processById.getStartTime()) {
                ThirdFlowInfoVo thirdFlowInfoVo = getFlowInfoImpl.getFlowInfo(appID);
                if (null != thirdFlowInfoVo) {
                    processById.getProcessStopList();
                    //判断接口返回的进度是否为空
                    if (StringUtils.isNotBlank(thirdFlowInfoVo.getProgress())) {
                        double progressNums = Double.parseDouble(thirdFlowInfoVo.getProgress());
                        Double progressNumsDb = null;
                        String percentage = processById.getProgress();
                        if (StringUtils.isNotBlank(percentage)) {
                            progressNumsDb = Double.parseDouble(percentage);
                        }
                        boolean isUpdateProcess = false;
                        // 判断状态，如果状态是STARTED，判断返回进度是否大于数据库进度，大于则保存
                        // 如果状态不是STARTED直接保存数据库
                        if ("STARTED".equals(thirdFlowInfoVo.getState())) {
                            // 如果数据库进度为空则进行保存
                            if (null == progressNumsDb) {
                                isUpdateProcess = true;
                            } else if (progressNums > progressNumsDb) {
                                //如果返回进度大于数据库进度则进行保存
                                isUpdateProcess = true;
                            }
                        } else {
                            isUpdateProcess = true;
                        }
                        if (isUpdateProcess) {
                            // 修改flow信息
                            processById.setLastUpdateUser("update");
                            processById.setLastUpdateDttm(new Date());
                            processById.setProgress(progressNums + "");
                            processById.setState(ProcessState.selectGender(thirdFlowInfoVo.getState()));
                            //processById.setProcessId(thirdFlowInfoVo.getPid());
                            processById.setProcessId(thirdFlowInfoVo.getId());
                            processById.setName(thirdFlowInfoVo.getName());
                            processById.setStartTime(DateUtils.strCstToDate(thirdFlowInfoVo.getStartTime()));
                            processById.setEndTime(DateUtils.strCstToDate(thirdFlowInfoVo.getEndTime()));
                            processTransaction.updateProcess(processById);
                            // 修改stops信息
                            List<ThirdFlowInfoStopsVo> stops = thirdFlowInfoVo.getStops();
                            if (null != stops && stops.size() > 0) {
                                List<ProcessStop> processStopListNew = new ArrayList<ProcessStop>();
                                processVo.setId(processById.getId());
                                for (ThirdFlowInfoStopsVo thirdFlowInfoStopsVo : stops) {
                                    if (null != thirdFlowInfoStopsVo) {
                                        ThirdFlowInfoStopVo thirdFlowInfoStopVo = thirdFlowInfoStopsVo.getStop();
                                        if (null != thirdFlowInfoStopVo) {
                                            ProcessStop processStopByNameAndPid = processStopTransaction.getProcessStopByNameAndPid(processById.getId(), thirdFlowInfoStopVo.getName());
                                            ProcessStopVo processStopVo = new ProcessStopVo();
                                            processStopByNameAndPid.setName(thirdFlowInfoStopVo.getName());
                                            processStopByNameAndPid.setState(StopState.selectGender(thirdFlowInfoStopVo.getState()));
                                            processStopByNameAndPid.setStartTime(DateUtils.strCstToDate(thirdFlowInfoStopVo.getStartTime()));
                                            processStopByNameAndPid.setEndTime(DateUtils.strCstToDate(thirdFlowInfoStopVo.getEndTime()));
                                            int updateProcessStop = processStopTransaction.updateProcessStop(processStopByNameAndPid);
                                            if (updateProcessStop > 0) {
                                                processStopListNew.add(processStopByNameAndPid);
                                            }
                                        }
                                    }
                                }
                                processById.setProcessStopList(processStopListNew);
                            }
                            processById = processTransaction.getProcessByAppId(appID);
                        }
                    }
                }
            }
            processVo = this.processPoToVo(processById);
        }

        return processVo;
    }

    /**
     * Query appInfo according to appID
     *
     * @param appID
     * @return
     */
    public String getAppInfoByAppId(String appID) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        if (StringUtils.isNotBlank(appID)) {
            // 查询appinfo
            //ProcessVo processVoThird = this.getAppInfoByThirdAndSave(appID);
            Process processById = processTransaction.getProcessByAppId(appID);
            ProcessVo processVo = ProcessUtils.processPoToVo(processById);
            if (null != processVo) {
                rtnMap.put("code", "1");
                rtnMap.put("progress", (null != processVo.getProgress() ? processVo.getProgress() : "0.00"));
                rtnMap.put("state", (null != processVo.getState() ? processVo.getState().name() : "NO_STATE"));
                rtnMap.put("processVo", processVo);
            }
        } else {
            rtnMap.put("errMsg", "appID is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 根据appID在第三方接口查询progress并保存
     *
     * @param appIDs
     * @return
     */

    @Override
    public String getProgressByThirdAndSave(String[] appIDs) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        List<ProcessVo> processVoList = null;
        if (null != appIDs && appIDs.length > 0) {
            List<Process> processListByAppIDs = processTransaction.getProcessListByAppIDs(appIDs);
            if (null != processListByAppIDs && processListByAppIDs.size() > 0) {
                processVoList = new ArrayList<ProcessVo>();
                for (Process process : processListByAppIDs) {
                    if (null != process) {
                        ProcessVo processVo = null;
                        // 如果状态为STARTED，去调接口,否则说明已经是启动完成,直接返回
                        ProcessState state = process.getState();
                        if (ProcessState.STARTED == state) {
                            ThirdProgressVo flowProgress = getFlowProgressImpl.getFlowProgress(process.getAppId());
                            if (null != flowProgress) {
                                double progressNumsDb = 0.00;
                                String percentage = process.getProgress();
                                if (StringUtils.isNotBlank(percentage)) {
                                    progressNumsDb = Float.parseFloat(percentage);
                                }
                                double progressNums = progressNumsDb;
                                if (!"NaN".equals(flowProgress.getProgress())) {
                                    progressNums = Double.parseDouble(flowProgress.getProgress());
                                    BigDecimal formatBD = new BigDecimal(progressNums);
                                    progressNums = formatBD.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                }
                                boolean isUpdateProcess = false;
                                // 判断状态，如果状态是STARTED，判断返回进度是否大于数据库进度，大于则保存
                                // 如果状态不是STARTED直接保存数据库
                                if ("STARTED".equals(flowProgress.getState())) {
                                    //如果返回进度大于数据库进度则进行保存
                                    if (progressNums > progressNumsDb) {
                                        isUpdateProcess = true;
                                    }
                                } else {
                                    isUpdateProcess = true;
                                }
                                if (isUpdateProcess) {
                                    // 修改flow信息
                                    process.setLastUpdateUser("update");
                                    process.setLastUpdateDttm(new Date());
                                    process.setProgress(progressNums + "");
                                    process.setState(ProcessState.selectGender(flowProgress.getState()));
                                    process.setName(flowProgress.getName());
                                    processTransaction.updateProcess(process);
                                }
                            }
                            processVo = this.processPoToVo(process);
                        } else if (null == process.getStartTime()) {
                            processVo = this.getAppInfoByThirdAndSave(process.getAppId());
                        }
                        if (null != processVo) {
                            processVoList.add(processVo);
                        }
                    }
                }
            }
        }
        if (null != processVoList && processVoList.size() > 0) {
            rtnMap.put("code", "1");
            for (ProcessVo processVo : processVoList) {
                if (null != processVo) {
                    rtnMap.put(processVo.getAppId(), processVo);
                }
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Query  process according to appID
     *
     * @param appIDs
     * @return
     */
    public String getProgressByAppIds(String[] appIDs) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        if (null != appIDs && appIDs.length > 0) {
            List<Process> processListByAppIDs = processTransaction.getProcessListByAppIDs(appIDs);
            if (CollectionUtils.isNotEmpty(processListByAppIDs)) {
                rtnMap.put("code", "1");
                for (Process process : processListByAppIDs) {
                    if (null != process) {
                        ProcessVo processVo = this.processPoToVo(process);
                        if (null != process) {
                            rtnMap.put(processVo.getAppId(), processVo);
                        }
                    }
                }
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 修改process(只更新process表，子表不更新)
     *
     * @param processVo
     * @return
     */
    @Override
    public int updateProcess(ProcessVo processVo, UserVo currentUser) {
        if (null != processVo && null != currentUser) {
            Process processById = processTransaction.getProcessById(currentUser, processVo.getId());
            if (null != processById) {
                BeanUtils.copyProperties("processVo", "processById");
                processById.setLastUpdateUser(currentUser.getUsername());
                processById.setLastUpdateDttm(new Date());
                processById.setState(processVo.getState());
                processById.setProgress(processVo.getProgress());
                processById.setStartTime(processVo.getStartTime());
                processById.setEndTime(processVo.getEndTime());
                processById.setProcessId(processVo.getProcessId());
                processById.setName(processVo.getName());
                return processTransaction.updateProcess(processById);
            }
        }
        return 0;
    }

    /**
     * 拷贝 process并新建
     *
     * @param processId
     * @return
     */
    @Override
    public Process processCopyProcessAndAdd(String processId, UserVo currentUser) {
        Process processCopy = null;
        if (null != currentUser) {
            String username = currentUser.getUsername();
            if (!StringUtils.isAnyEmpty(processId, username)) {
                Process process = processTransaction.getProcessById(currentUser, processId);
                if (null != process) {
                    processCopy = new Process();
                    processCopy.setId(SqlUtils.getUUID32());
                    processCopy.setCrtUser(username);
                    processCopy.setCrtDttm(new Date());
                    processCopy.setLastUpdateUser(username);
                    processCopy.setLastUpdateDttm(new Date());
                    processCopy.setEnableFlag(true);
                    processCopy.setState(ProcessState.STARTED);
                    processCopy.setName(process.getName());
                    processCopy.setDriverMemory(process.getDriverMemory());
                    processCopy.setExecutorNumber(process.getExecutorNumber());
                    processCopy.setExecutorMemory(process.getExecutorMemory());
                    processCopy.setExecutorCores(process.getExecutorCores());
                    processCopy.setDescription(process.getDescription());
                    processCopy.setViewXml(process.getViewXml());
                    processCopy.setFlowId(process.getFlowId());
                    processCopy.setParentProcessId(StringUtils.isNotBlank(process.getParentProcessId()) ? process.getParentProcessId() : process.getProcessId());
                    List<ProcessPath> processPathList = process.getProcessPathList();
                    if (null != processPathList && processPathList.size() > 0) {
                        List<ProcessPath> processPathListCopy = new ArrayList<ProcessPath>();
                        for (ProcessPath processPath : processPathList) {
                            if (null != processPath) {
                                ProcessPath processPathCopy = new ProcessPath();
                                processPathCopy.setId(SqlUtils.getUUID32());
                                processPathCopy.setCrtDttm(new Date());
                                processPathCopy.setCrtUser(username);
                                processPathCopy.setLastUpdateDttm(new Date());
                                processPathCopy.setLastUpdateUser(username);
                                processPathCopy.setEnableFlag(true);
                                processPathCopy.setFrom(processPath.getFrom());
                                processPathCopy.setTo(processPath.getTo());
                                processPathCopy.setInport(processPath.getInport());
                                processPathCopy.setOutport(processPath.getOutport());
                                processPathCopy.setPageId(processPath.getPageId());
                                processPathCopy.setProcess(processCopy);
                                processPathListCopy.add(processPathCopy);
                            }
                        }
                        processCopy.setProcessPathList(processPathListCopy);
                    }
                    List<ProcessStop> processStopList = process.getProcessStopList();
                    if (null != processStopList && processStopList.size() > 0) {
                        List<ProcessStop> processStopListCopy = new ArrayList<ProcessStop>();
                        for (ProcessStop processStop : processStopList) {
                            if (null != processStop) {
                                ProcessStop processStopCopy = new ProcessStop();
                                processStopCopy.setId(SqlUtils.getUUID32());
                                processStopCopy.setCrtDttm(new Date());
                                processStopCopy.setCrtUser(username);
                                processStopCopy.setLastUpdateDttm(new Date());
                                processStopCopy.setLastUpdateUser(username);
                                processStopCopy.setEnableFlag(true);
                                processStopCopy.setBundel(processStop.getBundel());
                                processStopCopy.setName(processStop.getName());
                                processStopCopy.setDescription(processStop.getDescription());
                                processStopCopy.setGroups(processStop.getGroups());
                                processStopCopy.setInports(processStop.getInports());
                                processStopCopy.setInPortType(processStop.getInPortType());
                                processStopCopy.setOutports(processStop.getOutports());
                                processStopCopy.setOutPortType(processStop.getOutPortType());
                                processStopCopy.setOwner(processStop.getOwner());
                                processStopCopy.setPageId(processStop.getPageId());
                                processStopCopy.setProcess(processCopy);
                                List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                                if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                                    List<ProcessStopProperty> processStopPropertyListCopy = new ArrayList<ProcessStopProperty>();
                                    for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                                        if (null != processStopProperty) {
                                            ProcessStopProperty processStopPropertyCopy = new ProcessStopProperty();
                                            processStopPropertyCopy.setId(SqlUtils.getUUID32());
                                            processStopPropertyCopy.setCrtDttm(new Date());
                                            processStopPropertyCopy.setCrtUser(username);
                                            processStopPropertyCopy.setLastUpdateDttm(new Date());
                                            processStopPropertyCopy.setLastUpdateUser(username);
                                            processStopPropertyCopy.setEnableFlag(true);
                                            processStopPropertyCopy.setCustomValue(processStopProperty.getCustomValue());
                                            processStopPropertyCopy.setName(processStopProperty.getName());
                                            processStopPropertyCopy.setAllowableValues(processStopProperty.getAllowableValues());
                                            processStopPropertyCopy.setDescription(processStopProperty.getDescription());
                                            processStopPropertyCopy.setDisplayName(processStopProperty.getDisplayName());
                                            processStopPropertyCopy.setRequired(processStopProperty.getRequired());
                                            processStopPropertyCopy.setSensitive(processStopPropertyCopy.getSensitive());
                                            processStopPropertyCopy.setProcessStop(processStopCopy);
                                            processStopPropertyListCopy.add(processStopPropertyCopy);
                                        }
                                    }
                                    processStopCopy.setProcessStopPropertyList(processStopPropertyListCopy);
                                }
                                processStopListCopy.add(processStopCopy);
                            }
                        }
                        processCopy.setProcessStopList(processStopListCopy);
                    }
                    int addProcess = processTransaction.addProcess(processCopy);
                    if (addProcess <= 0) {
                        processCopy = null;
                    }
                }
            }
        }
        return processCopy;
    }

    /**
     * 根据flowId生成Process并保存
     *
     * @param flowId
     * @return
     */
    @Override
    public Process flowToProcessAndSave(String flowId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        Process process = null;
        //flowId判空
        if (StringUtils.isNotBlank(flowId)) {
            // 查询flowId
            Flow flowById = flowTransaction.getFlowById(flowId);
            // 判空查询到的flow
            if (null != flowById) {
                process = ProcessUtils.flowToProcess(flowById, user);
                if (null != process) {
                    int addProcess = processTransaction.addProcess(process);
                    if (addProcess <= 0) {
                        process = null;
                        logger.warn("Save failed, transform failed");
                    }
                } else {
                    logger.warn("Conversion failed");
                }
            } else {
                logger.warn("Unable to query flow Id for'" + flowId + "'flow, the conversion failed");
            }
        } else {
            logger.warn("The parameter'flowId'is empty and the conversion fails");
        }
        return process;
    }

    private ProcessVo processPoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setCrtDttm(process.getCrtDttm());
            List<ProcessStopVo> processStopVoList = ProcessUtils.processStopListPoToVo(process.getProcessStopList());
            processVo.setProcessStopVoList(processStopVoList);
            List<ProcessPath> processPathList = process.getProcessPathList();
            if (null != processPathList && processPathList.size() > 0) {
                List<ProcessPathVo> processPathVoList = new ArrayList<ProcessPathVo>();
                for (ProcessPath processPath : processPathList) {
                    if (null != processPath) {
                        ProcessPathVo processPathVo = new ProcessPathVo();
                        BeanUtils.copyProperties(processPath, processPathVo);
                        processPathVoList.add(processPathVo);
                    }
                }
                processVo.setProcessPathVoList(processPathVoList);
            }
        }
        return processVo;
    }

    /**
     * Logical deletion
     *
     * @param processId
     * @return
     */
    @Override
    public StatefulRtnBase updateProcessEnableFlag(String processId, UserVo currentUser) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        if (StringUtils.isNotBlank(processId) && null != currentUser) {
            Process processById = processTransaction.getProcessById(currentUser, processId);
            if (null != processById) {
                processTransaction.updateProcessEnableFlag(processId, currentUser);
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No process with ID of'" + processId + "'was queried");
                logger.warn("No process with ID of'" + processId + "'was queried");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("The parameter is empty or missing");
            logger.warn("The parameter is empty or missing");
        }
        return statefulRtnBase;
    }

    /**
     * Query the running process List (process List) according to flowId
     *
     * @param flowId
     * @return
     */
    @Override
    public List<ProcessVo> getRunningProcessVoList(String flowId) {
        List<ProcessVo> processVoList = processTransaction.getRunningProcessList(flowId);
        if (CollectionUtils.isEmpty(processVoList)) {
            return null;
        } else {
            return processVoList;
        }
    }

    /**
     * Query processVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    @Override
    public String getProcessVoListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
            Page page = PageHelper.startPage(offset, limit);
            processTransaction.getProcessListByParam(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Start processes
     *
     * @param processId
     * @param checkpoint
     * @param currentUser
     * @return
     */
    @Override
    public String startProcess(String processId, String checkpoint, UserVo currentUser) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        if (StringUtils.isNotBlank(processId) && null != currentUser) {
            // Query Process by 'ProcessId' and copy new
            Process process = this.processCopyProcessAndAdd(processId, currentUser);
            if (null != process) {
                StatefulRtnBase statefulRtnBase = startFlowImpl.startProcess(process, checkpoint, currentUser);
                if (null != statefulRtnBase && statefulRtnBase.isReqRtnStatus()) {
                    // Call the 'AppInfo' interface once the startup is successful
                    this.getAppInfoByThirdAndSave(process.getAppId());
                    rtnMap.put("code", "1");
                    rtnMap.put("processId", process.getId());
                    rtnMap.put("errMsg", "Successful startup");
                } else {
                    this.updateProcessEnableFlag(process.getId(), currentUser);
                    rtnMap.put("errMsg", "Calling interface failed, startup failed");
                    logger.warn("Calling interface failed, startup failed");
                }
            } else {
                rtnMap.put("errMsg", "No process Id'" + processId + "'");
                logger.warn("No process Id'" + processId + "'");
            }
        } else {
            rtnMap.put("errMsg", "processId is null");
            logger.warn("processId is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Stop running processes
     *
     * @param processId
     * @return
     */
    @Override
    public String stopProcess(String processId) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        rtnMap.put("code", "0");
        if (StringUtils.isNotBlank(processId)) {
            // Query Process by 'ProcessId'
            Process process = processTransaction.getProcessById(currentUser, processId);
            // Determine whether it is empty, and determine whether the save is successful.
            if (null != process) {
                String appId = process.getAppId();
                if (null != appId) {
                    if (ProcessState.STARTED == process.getState()) {
                        String stopFlow = this.stopFlow.stopFlow(processId);
                        if (StringUtils.isNotBlank(stopFlow) && !stopFlow.contains("Exception")) {
                            rtnMap.put("code", "1");
                            rtnMap.put("errMsg", "Stop successful, return status is " + stopFlow);
                        } else {
                            logger.warn("Interface return value is null.");
                            rtnMap.put("errMsg", "Interface return value is null.");
                        }
                    } else {
                        logger.warn("The status of the process is " + process.getState() + " and cannot be stopped.");
                        rtnMap.put("errMsg", "The status of the process is " + process.getState() + " and cannot be stopped.");
                    }
                } else {
                    logger.warn("The 'appId' of the 'process' is empty.");
                    rtnMap.put("errMsg", "The 'appId' of the 'process' is empty.");
                }
            } else {
                logger.warn("No process ID is '" + processId + "' process");
                rtnMap.put("errMsg", " No process ID is '" + processId + "' process");
            }
        } else {
            logger.warn("processId is null");
            rtnMap.put("errMsg", "processId is null");
        }

        return JsonUtils.toJsonNoException(rtnMap);
    }
}