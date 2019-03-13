package com.nature.component.process.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.StopState;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopProperty;
import com.nature.component.process.service.IProcessService;
import com.nature.component.process.utils.ProcessStopUtils;
import com.nature.component.process.vo.ProcessPathVo;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.component.process.vo.ProcessVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.inf.IStartFlow;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import com.nature.transaction.process.ProcessStopTransaction;
import com.nature.transaction.process.ProcessTransaction;
import com.nature.transaction.workFlow.FlowTransaction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        if (StringUtils.isNotBlank(id)) {
            Process processById = processTransaction.getProcessById(id);
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
        if (StringUtils.isNotBlank(id)) {
            Process processById = processTransaction.getProcessById(id);
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
        Process processById = processTransaction.getProcessById(id);
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
                            processById.setProcessId(thirdFlowInfoVo.getPid());
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
     * 根据appID在第三方接口查询progress并保存
     *
     * @param appIDs
     * @return
     */

    @Override
    public List<ProcessVo> getProgressByThirdAndSave(String[] appIDs) {
        List<ProcessVo> processVoList = null;
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
                            double progressNums = Double.parseDouble(flowProgress.getProgress());
                            double progressNumsDb = 0.00;
                            String percentage = process.getProgress();
                            if (StringUtils.isNotBlank(percentage)) {
                                progressNumsDb = Float.parseFloat(percentage);
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
        return processVoList;
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
            Process processById = processTransaction.getProcessById(processVo.getId());
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

    @Override
    public Process startFlowAndUpdateProcess(Flow flow, UserVo currentUser) {
        Process process = null;
        if (null != flow && null != currentUser) {
            process = this.flowToProcessAndSave(flow.getId());
            if (null != process) {
                String checkpoint = "";
                List<Stops> stopsList = flow.getStopsList();
                for (Stops stops : stopsList) {
                    stops.getCheckpoint();
                    if (null != stops.getCheckpoint() && stops.getCheckpoint()) {
                        if (StringUtils.isNotBlank(checkpoint)) {
                            checkpoint = (checkpoint + ",");
                        }
                        checkpoint = (checkpoint + stops.getName());
                    }
                }
                StatefulRtnBase startProcess = startFlowImpl.startProcess(process, checkpoint, currentUser);
                if (null != startProcess && startProcess.isReqRtnStatus()) {
                    logger.info("调用接口并保存成功");
                } else {
                    processTransaction.updateProcessEnableFlag(process.getId(), currentUser);
                    logger.info("调用接口并保存失败");
                    process = null;
                }
            }
        }
        return process;
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
                Process process = processTransaction.getProcessById(processId);
                if (null != process) {
                    processCopy = new Process();
                    processCopy.setId(Utils.getUUID32());
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
                                processPathCopy.setId(Utils.getUUID32());
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
                                processStopCopy.setId(Utils.getUUID32());
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
                                            processStopPropertyCopy.setId(Utils.getUUID32());
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
        String username = (null != user) ? user.getUsername() : "-1";
        Process process = null;
        //flowId判空
        if (StringUtils.isNotBlank(flowId)) {
            // 查询flowId
            Flow flowById = flowTransaction.getFlowById(flowId);
            // 判空查询到的flow
            if (null != flowById) {
                process = new Process();
                // copy flow信息到process
                BeanUtils.copyProperties(flowById, process);
                // set基本信息
                process.setId(Utils.getUUID32());
                process.setCrtDttm(new Date());
                process.setCrtUser(username);
                process.setLastUpdateDttm(new Date());
                process.setLastUpdateUser(username);
                process.setEnableFlag(true);
                // 取出flow的画板信息
                MxGraphModel mxGraphModel = flowById.getMxGraphModel();
                // flow画板信息转为ViewXml
                String viewXml = SvgUtils.mxGraphModelToViewXml(mxGraphModel);
                // set viewXml
                process.setViewXml(viewXml);
                // set flowId
                process.setFlowId(flowId);
                // 取出flow的stops
                List<Stops> stopsList = flowById.getStopsList();
                // 判空stopsList
                if (null != stopsList && stopsList.size() > 0) {
                    // proce的stop的list
                    List<ProcessStop> processStopList = new ArrayList<ProcessStop>();
                    // 循环stopsList
                    for (Stops stops : stopsList) {
                        // 判空stops
                        if (null != stops) {
                            ProcessStop processStop = new ProcessStop();
                            // copy stops的信息到processStop中
                            BeanUtils.copyProperties(stops, processStop);
                            // set基本信息
                            processStop.setId(Utils.getUUID32());
                            processStop.setCrtDttm(new Date());
                            processStop.setCrtUser(username);
                            processStop.setLastUpdateDttm(new Date());
                            processStop.setLastUpdateUser(username);
                            processStop.setEnableFlag(true);
                            // 关联外键
                            processStop.setProcess(process);
                            // 取出stops的属性
                            List<Property> properties = stops.getProperties();
                            // stops的属性判空
                            if (null != properties && properties.size() > 0) {
                                List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();
                                // 循环stops的属性
                                for (Property property : properties) {
                                    // 判空
                                    if (null != property) {
                                        ProcessStopProperty processStopProperty = new ProcessStopProperty();
                                        // copy property的信息到processStopProperty中
                                        BeanUtils.copyProperties(property, processStopProperty);
                                        // set 基本信息
                                        processStopProperty.setId(Utils.getUUID32());
                                        processStopProperty.setCrtDttm(new Date());
                                        processStopProperty.setCrtUser(username);
                                        processStopProperty.setLastUpdateDttm(new Date());
                                        processStopProperty.setLastUpdateUser(username);
                                        processStopProperty.setEnableFlag(true);
                                        // 关联外键
                                        processStopProperty.setProcessStop(processStop);
                                        processStopPropertyList.add(processStopProperty);
                                    }
                                }
                                processStop.setProcessStopPropertyList(processStopPropertyList);
                            }
                            processStopList.add(processStop);
                        }
                    }
                    process.setProcessStopList(processStopList);
                }
                // 取flow的paths信息
                List<Paths> pathsList = flowById.getPathsList();
                // 判空paths信息
                if (null != pathsList && pathsList.size() > 0) {
                    List<ProcessPath> processPathList = new ArrayList<ProcessPath>();
                    // 循环paths信息
                    for (Paths paths : pathsList) {
                        // 判空paths
                        if (null != paths) {
                            ProcessPath processPath = new ProcessPath();
                            // copy paths的信息到processPath中
                            BeanUtils.copyProperties(paths, processPath);
                            // set基本信息
                            processPath.setId(Utils.getUUID32());
                            processPath.setCrtDttm(new Date());
                            processPath.setCrtUser(username);
                            processPath.setLastUpdateDttm(new Date());
                            processPath.setLastUpdateUser(username);
                            processPath.setEnableFlag(true);
                            // 关联外键
                            processPath.setProcess(process);
                            processPathList.add(processPath);
                        }
                    }
                    process.setProcessPathList(processPathList);
                }
                int addProcess = processTransaction.addProcess(process);
                if (addProcess <= 0) {
                    process = null;
                    logger.warn("保存失败，转换失败");
                }
            } else {
                logger.warn("查询不到flowId为‘" + flowId + "的flow’，转换失败");
            }
        } else {
            logger.warn("参数flowId为空，转换失败");
        }
        return process;
    }

    private ProcessVo processPoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setCrtDttm(process.getCrtDttm());
            List<ProcessStopVo> processStopVoList = ProcessStopUtils.processStopListPoToVo(process.getProcessStopList());
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
     * 逻辑删除
     *
     * @param processId
     * @return
     */
    @Override
    public StatefulRtnBase updateProcessEnableFlag(String processId, UserVo currentUser) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        if (StringUtils.isNotBlank(processId) && null != currentUser) {
            Process processById = processTransaction.getProcessById(processId);
            if (null != processById) {
                processTransaction.updateProcessEnableFlag(processId, currentUser);
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("没有查询到id为" + processId + "的process");
                logger.warn("没有查询到id为" + processId + "的process");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("参数为空或丢失");
            logger.warn("参数为空或丢失");
        }
        return statefulRtnBase;
    }

    /**
     * 根据flowId查询正在运行的进程List(processList)
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
}