package cn.cnic.component.process.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.domain.DataProductDomain;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.flow.domain.FlowPublishDomain;
import cn.cnic.component.flow.entity.*;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.flow.vo.StopPublishingPropertyVo;
import cn.cnic.component.flow.vo.StopPublishingVo;
import cn.cnic.component.mxGraph.utils.MxGraphUtils;
import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.process.vo.*;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.entity.File;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.vo.UserVo;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.vo.MxCellVo;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.service.IProcessService;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoVo;
import cn.cnic.third.vo.flow.ThirdProgressVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service
public class ProcessServiceImpl implements IProcessService {

    private Logger logger = LoggerUtil.getLogger();

    private final ProcessDomain processDomain;
    private final StopsComponentDomain stopsComponentDomain;
    private final FlowDomain flowDomain;
    private final IFlow flowImpl;
    private final DataProductDomain dataProductDomain;
    private final FlowPublishDomain flowPublishDomain;
    private final FileDomain fileDomain;

    @Autowired
    public ProcessServiceImpl(ProcessDomain processDomain,
                              StopsComponentDomain stopsComponentDomain,
                              FlowDomain flowDomain,
                              IFlow flowImpl, DataProductDomain dataProductDomain, FlowPublishDomain flowPublishDomain, FileDomain fileDomain) {
        this.processDomain = processDomain;
        this.stopsComponentDomain = stopsComponentDomain;
        this.flowDomain = flowDomain;
        this.flowImpl = flowImpl;
        this.dataProductDomain = dataProductDomain;
        this.flowPublishDomain = flowPublishDomain;
        this.fileDomain = fileDomain;
    }


    /**
     * Query processVoList (the query contains its child table)
     *
     * @return
     */
    @Override
    public List<ProcessVo> getProcessAllVoList() {
        List<Process> processList = processDomain.getProcessList();
        if (null == processList || processList.size() <= 0) {
            return null;
        }
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Process process : processList) {
            if (null == process) {
                continue;
            }
            ProcessVo processVo = ProcessUtils.processPoToVo(process);
            processVo.setCrtDttm(process.getCrtDttm());
            processVoList.add(processVo);
        }
        return processVoList;
    }

    /**
     * Query processVoList (only query process table)
     *
     * @return
     */
    @Override
    public List<ProcessVo> getProcessVoList() {
        List<Process> processList = processDomain.getProcessList();
        if (null == processList || processList.size() <= 0) {
            return null;
        }
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Process process : processList) {
            if (null == process) {
                continue;
            }
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setCrtDttm(process.getCrtDttm());
            processVoList.add(processVo);
        }
        return processVoList;
    }

    /**
     * Query processVo based on id (query contains its child table)
     *
     * @param id
     * @return
     */
    @Override
    public ProcessVo getProcessAllVoById(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        Process processById = processDomain.getProcessById(username, isAdmin, id);
        ProcessVo processVo = ProcessUtils.processPoToVo(processById);
        ProcessGroup processGroup = processById.getProcessGroup();
        if (null != processGroup) {
            ProcessGroupVo processGroupVo = new ProcessGroupVo();
            processGroupVo.setId(processGroup.getId());
            processVo.setProcessGroupVo(processGroupVo);
        }
        return processVo;
    }

    /**
     * Query processVo based on id (only query process table)
     *
     * @param id
     * @return
     */
    @Override
    public String getProcessVoById(String username, boolean isAdmin, String id) {
        // Determine if current user obtained are empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        Process processById = processDomain.getProcessById(username, isAdmin, id);
        if (null == processById) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processVo", null);
        }
        ProcessVo processVo = new ProcessVo();
        BeanUtils.copyProperties(processById, processVo);
        processVo.setCrtDttm(processById.getCrtDttm());
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processVo", processVo);
    }

    /**
     * Query process based on id
     *
     * @param id
     * @return
     */
    @Override
    public ProcessVo getProcessById(String username, boolean isAdmin, String id) {
        Process processById = processDomain.getProcessById(username, isAdmin, id);
        if (null == processById) {
            return null;
        }
        ProcessVo processVo = ProcessUtils.processPoToVo(processById);
        return processVo;
    }

    /**
     * Query process according to Appid
     *
     * @param appId
     * @return
     */
    @Override
    public ProcessVo getProcessVoByAppId(String appId) {
        if (StringUtils.isBlank(appId)) {
            return null;
        }
        Process processById = processDomain.getProcessByAppId(appId);
        if (null == processById) {
            return null;
        }
        ProcessVo processVo = ProcessUtils.processPoToVo(processById);
        return processVo;
    }

    /**
     * Query appInfo on a third-party interface based on appID and save
     *
     * @param appID
     * @return
     * @throws Exception
     */
    @Override
    public ProcessVo getAppInfoByThirdAndSave(String appID) throws Exception {
        Process processById = processDomain.getProcessByAppId(appID);
        if (null == processById) {
            return new ProcessVo();
        }
        ProcessVo processVo = new ProcessVo();
        // If the status is STARTED, the interface is removed. Otherwise,
        // it indicates that the startup is complete and returns directly.
        ProcessState state = processById.getState();
        if (ProcessState.STARTED != state && null != processById.getStartTime()) {
            processVo = ProcessUtils.processPoToVo(processById);
            return processVo;
        }
        ThirdFlowInfoVo thirdFlowInfoVo = flowImpl.getFlowInfo(appID);
        if (null == thirdFlowInfoVo) {
            processVo = ProcessUtils.processPoToVo(processById);
            return processVo;
        }
        processById.getProcessStopList();
        // Determine if the progress returned by the interface is empty
        if (StringUtils.isBlank(thirdFlowInfoVo.getProgress())) {
            processVo = ProcessUtils.processPoToVo(processById);
            return processVo;
        }
        double progressNums = Double.parseDouble(thirdFlowInfoVo.getProgress());
        Double progressNumsDb = null;
        String percentage = processById.getProgress();
        if (StringUtils.isNotBlank(percentage)) {
            progressNumsDb = Double.parseDouble(percentage);
        }
        boolean isUpdateProcess = false;
        // Determine the status, if the status is STARTED,
        // determine whether the return progress is greater than the database progress,
        // if it is greater than the save
        // Save the database directly if the state is not STARTED
        if ("STARTED".equals(thirdFlowInfoVo.getState())) {
            // Save if the database progress is empty
            if (null == progressNumsDb) {
                isUpdateProcess = true;
            } else if (progressNums > progressNumsDb) {
                // Save if the return progress is greater than the database progress
                isUpdateProcess = true;
            }
        } else {
            isUpdateProcess = true;
        }
        if (!isUpdateProcess) {
            processVo = ProcessUtils.processPoToVo(processById);
        }
        // Modify flow information
        processById.setLastUpdateUser("update");
        processById.setLastUpdateDttm(new Date());
        processById.setProgress(progressNums + "");
        processById.setState(ProcessState.selectGender(thirdFlowInfoVo.getState()));
        // processById.setProcessId(thirdFlowInfoVo.getPid());
        processById.setProcessId(thirdFlowInfoVo.getId());
        processById.setName(thirdFlowInfoVo.getName());
        processById.setStartTime(DateUtils.strCstToDate(thirdFlowInfoVo.getStartTime()));
        processById.setEndTime(DateUtils.strCstToDate(thirdFlowInfoVo.getEndTime()));
        processDomain.updateProcess(processById);
        // Modify the stops information
        List<ThirdFlowInfoStopsVo> stops = thirdFlowInfoVo.getStops();
        if (null != stops && stops.size() > 0) {
            List<ProcessStop> processStopListNew = new ArrayList<>();
            processVo.setId(processById.getId());
            for (ThirdFlowInfoStopsVo thirdFlowInfoStopsVo : stops) {
                if (null == thirdFlowInfoStopsVo) {
                    continue;
                }
                ThirdFlowInfoStopVo thirdFlowInfoStopVo = thirdFlowInfoStopsVo.getStop();
                if (null == thirdFlowInfoStopVo) {
                    continue;
                }
                ProcessStop processStopByNameAndPid = processDomain.getProcessStopByNameAndPid(processById.getId(), thirdFlowInfoStopVo.getName());
                processStopByNameAndPid.setName(thirdFlowInfoStopVo.getName());
                processStopByNameAndPid.setState(StopState.selectGender(thirdFlowInfoStopVo.getState()));
                processStopByNameAndPid.setStartTime(DateUtils.strCstToDate(thirdFlowInfoStopVo.getStartTime()));
                processStopByNameAndPid.setEndTime(DateUtils.strCstToDate(thirdFlowInfoStopVo.getEndTime()));
                int updateProcessStop = processDomain.updateProcessStop(processStopByNameAndPid);
                if (updateProcessStop > 0) {
                    processStopListNew.add(processStopByNameAndPid);
                }
            }
            processById.setProcessStopList(processStopListNew);
        }
        processById = processDomain.getProcessByAppId(appID);
        processVo = ProcessUtils.processPoToVo(processById);
        return processVo;
    }

    /**
     * Query appInfo according to appID
     *
     * @param appID
     * @return
     */
    @Override
    public String getAppInfoByAppId(String appID) {
        if (StringUtils.isBlank(appID)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("appID"));
        }
        // 查询appinfo
        // ProcessVo processVoThird = this.getAppInfoByThirdAndSave(appID);
        Process processById = processDomain.getProcessByAppId(appID);
        if (processById == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        ProcessVo processVo = ProcessUtils.processPoToVo(processById);
        if (null == processVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG());
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("processVo", processVo);
        rtnMap.put("progress", (null != processVo.getProgress() ? processVo.getProgress() : "0.00"));
        rtnMap.put("state", (null != processVo.getState() ? processVo.getState().name() : "NO_STATE"));
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * Query progress and save on third-party interface according to appID
     *
     * @param appIDs
     * @return
     * @throws Exception
     */
    @Override
    public String getProgressByThirdAndSave(String[] appIDs) throws Exception {
        if (null == appIDs || appIDs.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        List<Process> processListByAppIDs = processDomain.getProcessListByAppIDs(appIDs);
        if (null == processListByAppIDs || processListByAppIDs.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Process process : processListByAppIDs) {
            if (null == process) {
                continue;
            }
            ProcessVo processVo = null;
            // If the status is STARTED, the interface is removed. Otherwise,
            // it indicates that the startup is complete and returns directly.
            ProcessState state = process.getState();
            if (ProcessState.STARTED == state) {
                ThirdProgressVo flowProgress = flowImpl.getFlowProgress(process.getAppId());
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
                    // Determine the status, if the status is STARTED,
                    // determine whether the return progress is greater than the database progress,
                    // if it is greater than the save
                    // Save the database directly if the state is not STARTED
                    if ("STARTED".equals(flowProgress.getState())) {
                        // Save if the return progress is greater than the database progress
                        if (progressNums > progressNumsDb) {
                            isUpdateProcess = true;
                        }
                    } else {
                        isUpdateProcess = true;
                    }
                    if (isUpdateProcess) {
                        // Modify flow information
                        process.setLastUpdateUser("update");
                        process.setLastUpdateDttm(new Date());
                        process.setProgress(progressNums + "");
                        process.setState(ProcessState.selectGender(flowProgress.getState()));
                        process.setName(flowProgress.getName());
                        processDomain.updateProcess(process);
                    }
                }
                processVo = ProcessUtils.processPoToVo(process);
            } else if (null == process.getStartTime()) {
                processVo = this.getAppInfoByThirdAndSave(process.getAppId());
            }
            if (null != processVo) {
                processVoList.add(processVo);
            }
        }
        if (null == processVoList || processVoList.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        for (ProcessVo processVo : processVoList) {
            if (null != processVo) {
                rtnMap.put(processVo.getAppId(), processVo);
            }
        }
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * Query process according to appID
     *
     * @param appIDs
     * @return
     */
    @Override
    public String getProgressByAppIds(String[] appIDs) {
        if (null == appIDs || appIDs.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        List<Process> processListByAppIDs = processDomain.getProcessListByAppIDs(appIDs);
        if (CollectionUtils.isEmpty(processListByAppIDs)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        for (Process process : processListByAppIDs) {
            ProcessVo processVo = ProcessUtils.processPoToVo(process);
            if (null == processVo) {
                continue;
            }
            rtnMap.put(processVo.getAppId(), processVo);
        }
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * Modify the process (only update the process table, the subtable is not updated)
     *
     * @param processVo
     * @return
     * @throws Exception
     */
    @Override
    public int updateProcess(String username, boolean isAdmin, ProcessVo processVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        if (null == processVo) {
            return 0;
        }
        Process processById = processDomain.getProcessById(username, isAdmin, processVo.getId());
        if (null == processById) {
            return 0;
        }
        BeanUtils.copyProperties("processVo", "processById");
        processById.setLastUpdateUser(username);
        processById.setLastUpdateDttm(new Date());
        processById.setState(processVo.getState());
        processById.setProgress(processVo.getProgress());
        processById.setStartTime(processVo.getStartTime());
        processById.setEndTime(processVo.getEndTime());
        processById.setProcessId(processVo.getProcessId());
        processById.setName(processVo.getName());
        return processDomain.updateProcess(processById);
    }

    /**
     * Generate Process from flowId and save it
     *
     * @param isAdmin
     * @param username
     * @param flowId
     * @return
     */
    @Override
    public ProcessVo flowToProcessAndSave(boolean isAdmin, String username, String flowId) throws Exception {
        // Determine if the flowId is empty
        if (StringUtils.isBlank(flowId)) {
            logger.warn("The parameter'flowId'is empty and the conversion fails");
            return null;
        }
        // Query flow according to Id
        Flow flowById = flowDomain.getFlowById(flowId);
        // Determine if the queryed flow is empty
        if (null == flowById) {
            logger.warn("Unable to query flow Id for'" + flowId + "'flow, the conversion failed");
            return null;
        }
        Process process = ProcessUtils.flowToProcess(flowById, username, false);
        if (null == process) {
            logger.warn("Conversion failed");
            return null;
        }
        process.setId(UUIDUtils.getUUID32());
        int addProcess = processDomain.addProcess(process);
        if (addProcess <= 0) {
            logger.warn("Save failed, transform failed");
            return null;
        }
        String processId = process.getId();
        process = processDomain.getProcessById(username, isAdmin, processId);
        ProcessVo processVo = ProcessUtils.processPoToVo(process);
        return processVo;
    }

    /**
     * Logical deletion
     *
     * @param processId
     * @return
     */
    @Override
    public String delProcess(boolean isAdmin, String username, String processId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(processId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processID is null");
        }
        ProcessState processStateById = processDomain.getProcessStateById(processId);
        if (processStateById == ProcessState.STARTED) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Status is STARTED, cannot be deleted");
        }
        boolean updateProcessEnableFlag = processDomain.updateProcessEnableFlag(username, isAdmin, processId);
        if (updateProcessEnableFlag) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Successfully Deleted");
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Failed Deleted");
    }

    /**
     * Query the running process List (process List) according to flowId
     *
     * @param flowId
     * @return
     */
    @Override
    public String getRunningProcessVoList(String flowId) {
        if (StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowId is null");
        }
        List<Process> processList = processDomain.getRunningProcessList(flowId);
        if (CollectionUtils.isEmpty(processList)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        List<ProcessVo> processVoList = new ArrayList<ProcessVo>();
        for (Process process : processList) {
            ProcessVo processVo = ProcessUtils.processOnePoToVo(process);
            if (null != processVo) {
                processVoList.add(processVo);
            }
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("runningProcessVoList", processVoList);
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
    public String getProcessVoListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Page<Process> page = PageHelper.startPage(offset, limit);
        processDomain.getProcessListByParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
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
    public String getProcessGroupVoListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<Process> page = PageHelper.startPage(offset, limit);
        processDomain.getProcessGroupListByParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    /**
     * Start processes
     *
     * @param isAdmin
     * @param username
     * @param processId
     * @param checkpoint
     * @param runMode
     * @return
     * @throws Exception
     */
    @Override
    public String startProcess(boolean isAdmin, String username, String processId, String checkpoint, String runMode) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        if (StringUtils.isBlank(processId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processId is null");
        }
        if (StringUtils.isBlank(processId)) {
            return null;
        }
        Process process = processDomain.getProcessById(username, isAdmin, processId);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data by process Id:'" + processId + "'");
        }
        Process processCopy = ProcessUtils.copyProcess(process, username, runModeType, false);
        if (null == processCopy) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("start failed, copy failed");
        }
        processCopy.setId(UUIDUtils.getUUID32());
        processDomain.addProcess(processCopy);
        String processCopyId = processCopy.getId();
        Map<String, Object> stringObjectMap = flowImpl.startFlow(processCopy, checkpoint, runModeType);
        if (200 == (Integer) stringObjectMap.get("code")) {
            processCopy = processDomain.getProcessById(username, isAdmin, processCopyId);
            processCopy.setLastUpdateUser(username);
            processCopy.setLastUpdateDttm(new Date());
            processCopy.setAppId((String) stringObjectMap.get("appId"));
            processCopy.setProcessId((String) stringObjectMap.get("appId"));
            processCopy.setState(ProcessState.STARTED);
            processDomain.saveOrUpdate(processCopy);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processId", processCopyId);
        } else {
            processDomain.updateProcessEnableFlag(username, isAdmin, processCopy.getId());
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Calling interface failed, startup failed");
        }
    }

    /**
     * Stop running processes
     *
     * @param processId
     * @return
     */
    @Override
    public String stopProcess(String username, boolean isAdmin, String processId) throws Exception {
        if (StringUtils.isBlank(processId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("processId"));
        }
        // Query Process by 'ProcessId'
        Process process = processDomain.getProcessById(username, isAdmin, processId);
        // Determine whether it is empty, and determine whether the save is successful.
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(processId));
        }
        String appId = process.getAppId();
        if (StringUtils.isBlank(appId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The 'appId' of the 'process' is empty.");
        }
        if (ProcessState.STARTED != process.getState()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The status of the process is " + process.getState() + " and cannot be stopped.");
        }
        String stopFlow = flowImpl.stopFlow(appId);
        if (StringUtils.isBlank(stopFlow) || stopFlow.contains("Exception")) {
            logger.warn("Interface return value is null." + stopFlow);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Interface return value is " + stopFlow);
        }
        //更新process状态
        process.setState(ProcessState.KILLED);
        process.setLastUpdateDttm(new Date());
        process.setLastUpdateUser(username);
        processDomain.updateProcess(process);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Stop successful, return status is " + stopFlow);
    }

    /**
     * get debug data
     *
     * @param debugDataRequest
     * @return
     */
    @Override
    public String getDebugData(DebugDataRequest debugDataRequest) {
        if (null == debugDataRequest) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        // (all parameters have values, and isanyempty returns false)
        if (StringUtils.isAnyBlank(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String debugData = flowImpl.getDebugData(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName());
        if (StringUtils.isBlank(debugData)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        JSONObject obj = JSONObject.fromObject(debugData);
        String schema = (String) obj.get("schema");
        String debugDataPath = (String) obj.get("debugDataPath");

        if (StringUtils.isBlank(schema)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("schema is null");
        }
        if (StringUtils.isBlank(debugDataPath)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("debugDataPath is null");
        }
        DebugDataResponse debugDataResponse = HdfsUtils.readPath(debugDataPath, debugDataRequest.getStartFileName(), debugDataRequest.getStartLine(), 10);
        if (null == debugDataResponse) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Hdfs read failed");
        }
        String[] schemaSplit = schema.split(",");
        debugDataResponse.setSchema(Arrays.asList(schemaSplit));
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("debugData", debugDataResponse);

    }

    /**
     * get visualization data
     *
     * @param appID
     * @param stopName
     * @param visualizationType
     * @return
     */
    @Override
    public String getVisualizationData(String appID, String stopName, String visualizationType, boolean isSoft) {
        if (null == appID || null == stopName || null == visualizationType) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        // (all parameters have values, and isanyempty returns false)
        if (StringUtils.isAnyBlank(appID, stopName, visualizationType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String visualizationData = flowImpl.getVisualizationData(appID, stopName, visualizationType);
        if (StringUtils.isBlank(visualizationData)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        if (isSoft) {
            visualizationData = visualizationDataSort(visualizationData, visualizationType);
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("visualizationData", visualizationData);
    }

    /**
     * Query process based on processId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @Override
    public ProcessVo getProcessVoByPageId(String username, boolean isAdmin, String processGroupId, String pageId) {
        ProcessVo processVo = null;
        if (StringUtils.isNotBlank(processGroupId) && StringUtils.isNotBlank(pageId)) {
            Process processByPageId = processDomain.getProcessByPageId(username, isAdmin, processGroupId, pageId);
            processVo = ProcessUtils.processPoToVo(processByPageId);
        }
        return processVo;
    }

    /**
     * getCheckpoints
     *
     * @param parentProcessId
     * @return
     */
    @Override
    public String getCheckpoints(String parentProcessId, String pID) {
        if (StringUtils.isAllBlank(parentProcessId, pID)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String checkpoints = null;
        if (StringUtils.isNotBlank(parentProcessId) && !"null".equals(parentProcessId)) {
            checkpoints = flowImpl.getCheckpoints(parentProcessId);
        } else if (StringUtils.isNotBlank(pID)) {
            checkpoints = flowImpl.getCheckpoints(pID);
        }
        if (StringUtils.isNotBlank(checkpoints)) {
            String[] checkpointsSplit = checkpoints.split(",");
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("checkpointsSplit", checkpointsSplit);
        } else {
            logger.warn("No checkpoints found");
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("No checkpoints found");
        }
    }

    /**
     * getLogUrl
     *
     * @param appId
     * @return
     */
    @Override
    public String getLogUrl(String appId) {
        if (StringUtils.isBlank(appId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("appId is null");
        }
        String amContainerLogs = flowImpl.getFlowLog(appId);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        if (StringUtils.isNotBlank(amContainerLogs)) {
            rtnMap.put("stdoutLog", amContainerLogs + "/stdout/?start=0");
            rtnMap.put("stderrLog", amContainerLogs + "/stderr/?start=0");
        } else {
            rtnMap.put("code", 200);
            rtnMap.put("stdoutLog", MessageConfig.INTERFACE_CALL_ERROR_MSG());
            rtnMap.put("stderrLog", MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * drawingBoard Data
     *
     * @param username
     * @param isAdmin
     * @param loadId
     * @param parentAccessPath
     * @return
     */
    @Override
    public String drawingBoardData(String username, boolean isAdmin, String loadId, String parentAccessPath) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(loadId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'load' is null");
        }

        Process process = processDomain.getProcessById(username, isAdmin, loadId);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID : " + loadId);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        // set current user
        UserVo currentUser = new UserVo();
        currentUser.setUsername(username);
        rtnMap.put("currentUser", currentUser);
        // set parentAccessPath
        rtnMap.put("parentAccessPath", parentAccessPath);
        rtnMap.put("load", loadId);
        rtnMap.put("processType", "TASK");
        rtnMap.put("processId", process.getId());

        List<Map<String, String>> nodePageIdAndStates = new ArrayList<>();
        // processStopList
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            Map<String, String> stopNode;
            for (ProcessStop processStop_i : processStopList) {
                if (null == processStop_i) {
                    continue;
                }
                stopNode = new HashMap<>();
                stopNode.put("pageId", processStop_i.getPageId());
                stopNode.put("state", null != processStop_i.getState() ? processStop_i.getState().name() : "");
                nodePageIdAndStates.add(stopNode);
            }
        }
        rtnMap.put("nodeArr", nodePageIdAndStates);
        ProcessVo processVo = ProcessUtils.processPoToVo(process);
        rtnMap.put("percentage", (StringUtils.isNotBlank(processVo.getProgress()) ? processVo.getProgress() : 0.00) + "%");
        rtnMap.put("appId", processVo.getAppId());
        rtnMap.put("parentProcessId", processVo.getParentProcessId());
        rtnMap.put("pID", processVo.getProcessId());

        ProcessGroupVo parentsProcessGroupVo = processVo.getProcessGroupVo();
        if (null != parentsProcessGroupVo) {
            rtnMap.put("processGroupId", parentsProcessGroupVo.getId());
        }
        ProcessState processState = processVo.getState();
        if (null == processState) {
            processState = ProcessState.INIT;
        }
        rtnMap.put("processState", processState.name());
        MxGraphModelVo mxGraphModelVo = processVo.getMxGraphModelVo();
        if (null != mxGraphModelVo) {
            List<MxCellVo> rootVo = mxGraphModelVo.getRootVo();
            if (null != rootVo && rootVo.size() > 0) {
                List<MxCellVo> iconTranslate = new ArrayList<>();
                MxCellVo iconMxCellVo;
                for (MxCellVo mxCellVo : rootVo) {
                    if (null == mxCellVo) {
                        continue;
                    }
                    String style = mxCellVo.getStyle();
                    if (StringUtils.isBlank(style) || style.indexOf("image;") != 0) {
                        continue;
                    }
                    if (null == mxCellVo.getMxGeometryVo()) {
                        continue;
                    }
                    iconMxCellVo = MxCellUtils.initIconMxCellVo(mxCellVo);
                    if (null == iconMxCellVo) {
                        continue;
                    }
                    iconTranslate.add(iconMxCellVo);
                }
                rootVo.addAll(iconTranslate);
            }
            mxGraphModelVo.setRootVo(rootVo);
        }

        String loadXml = MxGraphUtils.mxGraphModelVoToMxGraphXml(mxGraphModelVo);
        rtnMap.put("xmlDate", loadXml);

        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessStopVoByPageId(String processId, String pageId) {
        if (StringUtils.isAnyEmpty(processId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        ProcessStop processStopByPageId = processDomain.getProcessStopByPageIdAndPageId(processId, pageId);
        if (null == processStopByPageId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("process stop data is null");
        }
        ProcessStopVo processStopVo = ProcessUtils.processStopPoToVo(processStopByPageId);
        StopsComponent stopsComponentByBundle = stopsComponentDomain.getStopsComponentByBundle(processStopByPageId.getBundel());
        if (null != stopsComponentByBundle) {
            processStopVo.setVisualizationType(stopsComponentByBundle.getVisualizationType());
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processStopVo", processStopVo);
    }

    @Override
    public String getErrorLogInfo(String appId) {
        String amContainerLogs = flowImpl.getFlowLog(appId);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        if (StringUtils.isNotBlank(amContainerLogs)) {
            String stdoutLog = amContainerLogs + "/stdout/?start=0";
            String stderrLog = amContainerLogs + "/stderr/?start=0";
            String stdoutLogHtml = HttpUtils.getHtml(stdoutLog);
            String stderrLogHtml = HttpUtils.getHtml(stderrLog);
            //截取错误信息
            String error1 = extractExceptionLines(stdoutLogHtml);
            String error2 = extractExceptionLines(stderrLogHtml);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("errorInfo", error1 + error2);
            rtnMap.put("data", errorMap);
        } else {
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        return ReturnMapUtils.toJson(rtnMap);
    }

    @Override
    public String getProcessPageByPublishingId(ProcessVo requestVo) {
        String username = SessionUserUtil.getCurrentUsername();
        if (null == requestVo.getPage() || null == requestVo.getLimit()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Page<Process> page = PageHelper.startPage(requestVo.getPage(), requestVo.getLimit(),"crt_dttm desc");
        processDomain.getProcessListByPublishingIdAndUserName(Long.parseLong(requestVo.getPublishingId()), requestVo.getKeyword(), username);
        //对result进行加工
        List<Process> result = page.getResult();
        result.forEach(process -> {
            //封装FlowPublishingVo
            boolean isNumeric = Pattern.matches("\\d+", process.getFlowId());
            if (isNumeric) {
                FlowPublishing flowPublishing = flowPublishDomain.getFullInfoById(process.getFlowId());
                if (ObjectUtils.isNotEmpty(flowPublishing)) {
                    FlowPublishingVo flowPublishingVo = new FlowPublishingVo();
                    BeanUtils.copyProperties(flowPublishing, flowPublishingVo);
                    flowPublishingVo.setId(flowPublishing.getId().toString());

                    List<StopPublishingVo> stops = flowPublishing.getProperties().stream()
                            .collect(Collectors.groupingBy(FlowStopsPublishingProperty::getStopId))
                            .entrySet()
                            .stream()
                            .map(entry -> {
                                StopPublishingVo vo = new StopPublishingVo();
                                vo.setStopId(entry.getKey());
                                vo.setStopName(entry.getValue().get(0).getStopName());
                                vo.setStopPublishingPropertyVos(entry.getValue().stream()
                                        .map(property -> {
                                            StopPublishingPropertyVo propertyVo = new StopPublishingPropertyVo();
                                            BeanUtils.copyProperties(property, propertyVo);
                                            propertyVo.setId(property.getId().toString());
                                            propertyVo.setPublishingId(property.getPublishingId().toString());
                                            Long fileId = property.getFileId();
                                            if (fileId != null) {
                                                propertyVo.setFileId(fileId.toString());
                                                propertyVo.setFileName(property.getFileName());
                                            }
                                            //封装customValue得用dataProduct中的datasetUrl了
                                            if (StringUtils.isNotBlank(property.getStopBundle())) {
                                                Optional<ProcessStop> first = process.getProcessStopList().stream().filter(processStop -> property.getStopBundle().equals(processStop.getBundel())).findFirst();
                                                if (first.isPresent()) {
                                                    String propertyName = property.getPropertyName();
                                                    if (StringUtils.isNotBlank(propertyName)) {
                                                        Optional<ProcessStopProperty> optionalProcessStopProperty = first.get().getProcessStopPropertyList().stream().filter(processStopProperty -> propertyName.equals(processStopProperty.getName())).findFirst();
                                                        optionalProcessStopProperty.ifPresent(processStopProperty -> propertyVo.setCustomValue(processStopProperty.getCustomValue()));
                                                    }
                                                }
                                            }
                                            return propertyVo;
                                        })
                                        .collect(Collectors.toList()));
                                return vo;
                            })
                            .collect(Collectors.toList());
                    flowPublishingVo.setStops(stops);
                    process.setFlowPublishing(flowPublishingVo);
                }
            }
        });
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamWithChangeResultRtnStr(page, new ArrayList<Object>(result), rtnMap);
    }

    @Override
    public String getProcessHistoryPageOfSelf(ProcessVo requestVo) {
        String username = SessionUserUtil.getCurrentUsername();
        if (null == requestVo.getPage() || null == requestVo.getLimit()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Page<Process> page = PageHelper.startPage(requestVo.getPage(), requestVo.getLimit());
        processDomain.getProcessHistoryPageOfSelf(requestVo.getKeyword(), username);
        //对result进行加工
        List<Process> result = page.getResult();
        result.forEach(process -> {
//            ProcessVo processVo = new ProcessVo();
//            try {
//                org.apache.commons.beanutils.BeanUtils.copyProperties(processVo, process);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
            //封装FlowPublishingVo
            boolean isNumeric = Pattern.matches("\\d+", process.getFlowId());
            if (isNumeric) {
                FlowPublishing flowPublishing = flowPublishDomain.getFullInfoById(process.getFlowId());
                if (ObjectUtils.isNotEmpty(flowPublishing)) {
                    FlowPublishingVo flowPublishingVo = new FlowPublishingVo();
                    BeanUtils.copyProperties(flowPublishing, flowPublishingVo);
                    flowPublishingVo.setId(flowPublishing.getId().toString());

                    List<StopPublishingVo> stops = flowPublishing.getProperties().stream()
                            .collect(Collectors.groupingBy(FlowStopsPublishingProperty::getStopId))
                            .entrySet()
                            .stream()
                            .map(entry -> {
                                StopPublishingVo vo = new StopPublishingVo();
                                vo.setStopId(entry.getKey());
                                vo.setStopName(entry.getValue().get(0).getStopName());
                                vo.setStopPublishingPropertyVos(entry.getValue().stream()
                                        .map(property -> {
                                            StopPublishingPropertyVo propertyVo = new StopPublishingPropertyVo();
                                            BeanUtils.copyProperties(property, propertyVo);
                                            propertyVo.setId(property.getId().toString());
                                            propertyVo.setPublishingId(property.getPublishingId().toString());
                                            Long fileId = property.getFileId();
                                            if (fileId != null) {
                                                propertyVo.setFileId(fileId.toString());
                                                propertyVo.setFileName(property.getFileName());
                                            }
                                            //封装customValue得用dataProduct中的datasetUrl了
                                            if (StringUtils.isNotBlank(property.getStopBundle())) {
                                                Optional<ProcessStop> first = process.getProcessStopList().stream().filter(processStop -> property.getStopBundle().equals(processStop.getBundel())).findFirst();
                                                if (first.isPresent()) {
                                                    String propertyName = property.getPropertyName();
                                                    if (StringUtils.isNotBlank(propertyName)) {
                                                        Optional<ProcessStopProperty> optionalProcessStopProperty = first.get().getProcessStopPropertyList().stream().filter(processStopProperty -> propertyName.equals(processStopProperty.getName())).findFirst();
                                                        optionalProcessStopProperty.ifPresent(processStopProperty -> propertyVo.setCustomValue(processStopProperty.getCustomValue()));
                                                    }
                                                }
                                            }
                                            return propertyVo;
                                        })
                                        .collect(Collectors.toList()));
                                return vo;
                            })
                            .collect(Collectors.toList());
                    flowPublishingVo.setStops(stops);
                    process.setFlowPublishing(flowPublishingVo);
                }
            }
        });
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamWithChangeResultRtnStr(page, new ArrayList<Object>(result), rtnMap);
    }

    @Override
    public String getByProcessId(String processId) throws InvocationTargetException, IllegalAccessException {
        Process process = processDomain.getProcessWithFlowPublishingById(processId);
//        ProcessVo processVo = new ProcessVo();
//        org.apache.commons.beanutils.BeanUtils.copyProperties(processVo, process);
        //封装FlowPublishingVo
        boolean isNumeric = Pattern.matches("\\d+", process.getFlowId());
        if (isNumeric) {
            FlowPublishing flowPublishing = flowPublishDomain.getFullInfoById(process.getFlowId());
            if (ObjectUtils.isNotEmpty(flowPublishing)) {
                FlowPublishingVo flowPublishingVo = new FlowPublishingVo();
                BeanUtils.copyProperties(flowPublishing, flowPublishingVo);
                flowPublishingVo.setId(flowPublishing.getId().toString());
                //获取封面
                File flowPublishingCover = fileDomain.getByAssociateId(flowPublishing.getId().toString(), FileAssociateType.FLOW_PUBLISHING_COVER.getValue());
                if (ObjectUtils.isNotEmpty(flowPublishingCover)) {
                    flowPublishingVo.setCoverFileId(flowPublishingCover.getId().toString());
                    flowPublishingVo.setCoverFileName(flowPublishingCover.getFileName());
                }
                //获取说明书
                File flowPublishingInstruction = fileDomain.getByAssociateId(flowPublishing.getId().toString(), FileAssociateType.FLOW_PUBLISHING_INSTRUCTION.getValue());
                if (ObjectUtils.isNotEmpty(flowPublishingInstruction)) {
                    flowPublishingVo.setInstructionFileId(flowPublishingInstruction.getId().toString());
                    flowPublishingVo.setInstructionFileName(flowPublishingInstruction.getFileName());
                }

                List<StopPublishingVo> stops = flowPublishing.getProperties().stream()
                        .collect(Collectors.groupingBy(FlowStopsPublishingProperty::getStopId))
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            StopPublishingVo vo = new StopPublishingVo();
                            vo.setStopId(entry.getKey());
                            vo.setStopName(entry.getValue().get(0).getStopName());
                            vo.setBak1(entry.getValue().get(0).getBak1());
                            vo.setBak2(entry.getValue().get(0).getBak2());
                            vo.setBak3(entry.getValue().get(0).getBak3());
                            vo.setStopPublishingPropertyVos(entry.getValue().stream()
                                    .map(property -> {
                                        StopPublishingPropertyVo propertyVo = new StopPublishingPropertyVo();
                                        BeanUtils.copyProperties(property, propertyVo);
                                        propertyVo.setId(property.getId().toString());
                                        propertyVo.setPublishingId(property.getPublishingId().toString());
                                        Long fileId = property.getFileId();
                                        if (fileId != null) {
                                            propertyVo.setFileId(fileId.toString());
                                            propertyVo.setFileName(property.getFileName());
                                        }
                                        //封装customValue得用dataProduct中的datasetUrl了
                                        if (StringUtils.isNotBlank(property.getStopBundle())) {
                                            Optional<ProcessStop> first = process.getProcessStopList().stream().filter(processStop -> property.getStopBundle().equals(processStop.getBundel())).findFirst();
                                            if (first.isPresent()) {
                                                String propertyName = property.getPropertyName();
                                                if (StringUtils.isNotBlank(propertyName)) {
                                                    Optional<ProcessStopProperty> optionalProcessStopProperty = first.get().getProcessStopPropertyList().stream().filter(processStopProperty -> propertyName.equals(processStopProperty.getName())).findFirst();
                                                    optionalProcessStopProperty.ifPresent(processStopProperty -> propertyVo.setCustomValue(processStopProperty.getCustomValue()));
                                                }
                                            }
                                        }
                                        return propertyVo;
                                    })
                                    .collect(Collectors.toList()));
                            return vo;
                        })
                        .sorted(Comparator.comparing(vo -> Integer.parseInt(vo.getBak1())))
                        .collect(Collectors.toList());
                flowPublishingVo.setStops(stops);
                process.setFlowPublishing(flowPublishingVo);
            }
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", process);
    }

    @Override
    public String getAppIdByProcessId(String processId) {
        String appId = SysParamsCache.STARTED_PROCESS.get(processId);
        if(StringUtils.isNotBlank(appId)){
            SysParamsCache.STARTED_PROCESS.remove(processId);
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("appId", appId);
    }

    @Override
    public String deleteForPublishing(String processId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        //同时删除数据和流水线，发布了就不可删除，必须下架发布的数据之后才能删除
        ProcessState processStateById = processDomain.getProcessStateById(processId);
        if (processStateById == ProcessState.STARTED || processStateById == ProcessState.INIT) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The process is running, cannot be deleted");
        }
        //校验是否有已发布的数据产品
        List<DataProductVo> dataProductVos = dataProductDomain.getListByProcessId(processId);
        if(CollectionUtils.isNotEmpty(dataProductVos)){
            List<DataProductVo> publishedDataProducts = dataProductVos.stream().filter(x -> x.getState().equals(DataProductState.PUBLISHED.getValue())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(publishedDataProducts)) return ReturnMapUtils.setFailedMsgRtnJsonStr("The associated data products have been published. Please remove them before deleting the process");
        }
        boolean updateProcessEnableFlag = processDomain.updateProcessEnableFlag(username, isAdmin, processId);
        //删除数据产品及相关file记录
        Date now = new Date();
        for (DataProductVo dataProductVo : dataProductVos) {
            DataProduct dataProduct = new DataProduct();
            dataProduct.setId(Long.parseLong(dataProductVo.getId()));
            dataProduct.setState(DataProductState.DELETED.getValue());
            dataProduct.setLastUpdateDttm(now);
            dataProduct.setLastUpdateDttmStr(DateUtils.dateTimeSecToStr(now));
            dataProduct.setEnableFlag(false);
            dataProduct.setEnableFlagNum(0);
            int i = dataProductDomain.delete(dataProduct);
            fileDomain.fakeDeleteByAssociateId(dataProduct.getId().toString(), username);
        }
        if (updateProcessEnableFlag) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Successfully Deleted");
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Failed Deleted");
    }

    private String extractExceptionLines(String input) {
        String[] lines = input.split("\n");
        String regex = "(?i).*\\b(fail|error|exception)\\b.*"; // 使用正则表达式，忽略大小写匹配关键字
        boolean capture = false;
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (line.matches(regex)) {
                capture = true;
                result.append(line).append("\n");
            } else if (capture && line.contains("<")) {
                capture = false;
            } else if (capture) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    @SuppressWarnings("rawtypes")
    private String visualizationDataSort(String visualizationData, String type) {
        if (StringUtils.isBlank(visualizationData)) {
            return null;
        }
        if (!"LINECHART".equals(type) && !"HISTOGRAM".equals(type)) {
            return visualizationData;
        }
        JSONObject obj = JSONObject.fromObject(visualizationData);
        JSONArray xAxis_data = obj.getJSONObject("xAxis").getJSONArray("data");
        JSONArray series_data = obj.getJSONArray("series");
        JSONArray legent = obj.getJSONArray("legent");

        LinkedHashMap<String, Map> xAxisMap = new LinkedHashMap<>();
        List<String> xAxisList = new ArrayList<>();

        for (int i = 0; i < xAxis_data.size(); i++) {
            String o = xAxis_data.getString(i);
            xAxisList.add(o);
            LinkedHashMap<String, String> xAxisValueMap = new LinkedHashMap<>();
            for (int j = 0; j < series_data.size(); j++) {
                String string = series_data.getJSONObject(j).getJSONArray("data").getString(i);
                xAxisValueMap.put(j + "", string);
            }
            xAxisMap.put(o, xAxisValueMap);
        }
        Collections.sort(xAxisList);
        JSONObject sort_data = new JSONObject();

        for (String str : xAxisList) {
            JSONArray new_xAxis_data = null;
            if (0 != sort_data.size()) {
                new_xAxis_data = sort_data.getJSONArray("xAxis_data");
            }
            if (null == new_xAxis_data) {
                new_xAxis_data = new JSONArray();
            }
            new_xAxis_data.add(str);
            sort_data.put("xAxis_data", new_xAxis_data);

            Map str_value = xAxisMap.get(str);
            for (Object key : str_value.keySet()) {
                JSONArray str_value_data = null;
                try {
                    str_value_data = sort_data.getJSONArray(key.toString());
                } catch (Exception e) {
                    str_value_data = new JSONArray();
                }
                if (null == str_value_data) {
                    str_value_data = new JSONArray();
                }
                str_value_data.add(str_value.get(key));
                sort_data.put(key.toString(), str_value_data);
            }

        }
        obj.getJSONObject("xAxis").put("data", sort_data.getJSONArray("xAxis_data"));
        for (int i = 0; i < legent.size(); i++) {
            obj.getJSONArray("series").getJSONObject(i).put("data", sort_data.getJSONArray(i + ""));
        }
        return obj.toString();
    }

}
