package cn.cnic.component.process.service.Impl;

import cn.cnic.base.util.*;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.Eunm.StopState;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.jpa.domain.FlowDomain;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.vo.MxCellVo;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.domain.ProcessDomainU;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.jpa.domain.ProcessDomain;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.component.process.mapper.ProcessStopMapper;
import cn.cnic.component.process.service.IProcessService;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.DebugDataRequest;
import cn.cnic.component.process.vo.DebugDataResponse;
import cn.cnic.component.process.vo.ProcessGroupVo;
import cn.cnic.component.process.vo.ProcessVo;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoVo;
import cn.cnic.third.vo.flow.ThirdProgressVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
    private ProcessMapper processMapper;

    @Resource
    private ProcessDomainU processDomainU;

    @Resource
    private ProcessStopMapper processStopMapper;

    @Resource
    private FlowDomain flowDomain;

    @Resource
    private IFlow flowImpl;

    @Resource
    private ProcessDomain processDomain;

    /**
     * Query processVoList (the query contains its child table)
     *
     * @return
     */
    @Override
    public List<ProcessVo> getProcessAllVoList() {
        List<ProcessVo> processVoList = null;
        List<Process> processList = processMapper.getProcessList();
        if (null != processList && processList.size() > 0) {
            processVoList = new ArrayList<>();
            for (Process process : processList) {
                if (null != process) {
                    ProcessVo processVo = ProcessUtils.processPoToVo(process);
                    processVo.setCrtDttm(process.getCrtDttm());
                    processVoList.add(processVo);
                }
            }
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
        List<ProcessVo> processVoList = null;
        List<Process> processList = processMapper.getProcessList();
        if (null != processList && processList.size() > 0) {
            processVoList = new ArrayList<>();
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
     * Query processVo based on id (query contains its child table)
     *
     * @param id
     * @return
     */
    @Override
    public ProcessVo getProcessAllVoById(String username, boolean isAdmin, String id) {
        ProcessVo processVo = null;
        if (StringUtils.isNotBlank(id)) {
            Process processById = processMapper.getProcessById(username, isAdmin, id);
            processVo = ProcessUtils.processPoToVo(processById);
            ProcessGroup processGroup = processById.getProcessGroup();
            if (null != processGroup) {
                ProcessGroupVo processGroupVo = new ProcessGroupVo();
                processGroupVo.setId(processGroup.getId());
                processVo.setProcessGroupVo(processGroupVo);
            }
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("user Illegality");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        ProcessVo processVo = null;
        Process processById = processMapper.getProcessById(username, isAdmin, id);
        if (null != processById) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(processById, processVo);
            processVo.setCrtDttm(processById.getCrtDttm());
        }
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
        ProcessVo processVo = null;
        Process processById = processMapper.getProcessById(username, isAdmin, id);
        if (null != processById) {
            processVo = ProcessUtils.processPoToVo(processById);
        }
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
        ProcessVo processVo = null;
        if (StringUtils.isNotBlank(appId)) {
            Process processById = processMapper.getProcessByAppId(appId);
            if (null != processById) {
                processVo = ProcessUtils.processPoToVo(processById);
            }
        }
        return processVo;
    }

    /**
     * Query appInfo on a third-party interface based on appID and save
     *
     * @param appID
     * @return
     */
    @Override
    public ProcessVo getAppInfoByThirdAndSave(String appID) {
        ProcessVo processVo = new ProcessVo();
        Process processById = processMapper.getProcessByAppId(appID);
        if (null != processById) {
            // If the status is STARTED, the interface is removed. Otherwise, it indicates that the startup is complete and returns directly.
            ProcessState state = processById.getState();
            if (ProcessState.STARTED == state || null == processById.getStartTime()) {
                ThirdFlowInfoVo thirdFlowInfoVo = flowImpl.getFlowInfo(appID);
                if (null != thirdFlowInfoVo) {
                    processById.getProcessStopList();
                    //Determine if the progress returned by the interface is empty
                    if (StringUtils.isNotBlank(thirdFlowInfoVo.getProgress())) {
                        double progressNums = Double.parseDouble(thirdFlowInfoVo.getProgress());
                        Double progressNumsDb = null;
                        String percentage = processById.getProgress();
                        if (StringUtils.isNotBlank(percentage)) {
                            progressNumsDb = Double.parseDouble(percentage);
                        }
                        boolean isUpdateProcess = false;
                        // Determine the status, if the status is STARTED, determine whether the return progress is greater than the database progress, if it is greater than the save
                        // Save the database directly if the state is not STARTED
                        if ("STARTED".equals(thirdFlowInfoVo.getState())) {
                            // Save if the database progress is empty
                            if (null == progressNumsDb) {
                                isUpdateProcess = true;
                            } else if (progressNums > progressNumsDb) {
                                //Save if the return progress is greater than the database progress
                                isUpdateProcess = true;
                            }
                        } else {
                            isUpdateProcess = true;
                        }
                        if (isUpdateProcess) {
                            // Modify flow information
                            processById.setLastUpdateUser("update");
                            processById.setLastUpdateDttm(new Date());
                            processById.setProgress(progressNums + "");
                            processById.setState(ProcessState.selectGender(thirdFlowInfoVo.getState()));
                            //processById.setProcessId(thirdFlowInfoVo.getPid());
                            processById.setProcessId(thirdFlowInfoVo.getId());
                            processById.setName(thirdFlowInfoVo.getName());
                            processById.setStartTime(DateUtils.strCstToDate(thirdFlowInfoVo.getStartTime()));
                            processById.setEndTime(DateUtils.strCstToDate(thirdFlowInfoVo.getEndTime()));
                            processDomainU.updateProcess(processById);
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
                                    ProcessStop processStopByNameAndPid = processStopMapper.getProcessStopByNameAndPid(processById.getId(), thirdFlowInfoStopVo.getName());
                                    processStopByNameAndPid.setName(thirdFlowInfoStopVo.getName());
                                    processStopByNameAndPid.setState(StopState.selectGender(thirdFlowInfoStopVo.getState()));
                                    processStopByNameAndPid.setStartTime(DateUtils.strCstToDate(thirdFlowInfoStopVo.getStartTime()));
                                    processStopByNameAndPid.setEndTime(DateUtils.strCstToDate(thirdFlowInfoStopVo.getEndTime()));
                                    int updateProcessStop = processStopMapper.updateProcessStop(processStopByNameAndPid);
                                    if (updateProcessStop > 0) {
                                        processStopListNew.add(processStopByNameAndPid);
                                    }
                                }
                                processById.setProcessStopList(processStopListNew);
                            }
                            processById = processMapper.getProcessByAppId(appID);
                        }
                    }
                }
            }
            processVo = ProcessUtils.processPoToVo(processById);
        }

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
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(appID)) {
            // 查询appinfo
            //ProcessVo processVoThird = this.getAppInfoByThirdAndSave(appID);
            Process processById = processMapper.getProcessByAppId(appID);
            ProcessVo processVo = ProcessUtils.processPoToVo(processById);
            if (null != processVo) {
                rtnMap.put("code", 200);
                rtnMap.put("progress", (null != processVo.getProgress() ? processVo.getProgress() : "0.00"));
                rtnMap.put("state", (null != processVo.getState() ? processVo.getState().name() : "NO_STATE"));
                rtnMap.put("processVo", processVo);
            }
        } else {
            rtnMap.put("errorMsg", "appID is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Query progress and save on third-party interface according to appID
     *
     * @param appIDs
     * @return
     */
    @Override
    public String getProgressByThirdAndSave(String[] appIDs) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        List<ProcessVo> processVoList = null;
        if (null != appIDs && appIDs.length > 0) {
            List<Process> processListByAppIDs = processMapper.getProcessListByAppIDs(appIDs);
            if (null != processListByAppIDs && processListByAppIDs.size() > 0) {
                processVoList = new ArrayList<>();
                for (Process process : processListByAppIDs) {
                    if (null != process) {
                        ProcessVo processVo = null;
                        // If the status is STARTED, the interface is removed. Otherwise, it indicates that the startup is complete and returns directly.
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
                                // Determine the status, if the status is STARTED, determine whether the return progress is greater than the database progress, if it is greater than the save
                                // Save the database directly if the state is not STARTED
                                if ("STARTED".equals(flowProgress.getState())) {
                                    //Save if the return progress is greater than the database progress
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
                                    processDomainU.updateProcess(process);
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
                }
            }
        }
        if (null != processVoList && processVoList.size() > 0) {
            rtnMap.put("code", 200);
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
    @Override
    public String getProgressByAppIds(String[] appIDs) {
        if (null == appIDs || appIDs.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("appId is null");
        }
        List<Process> processListByAppIDs = processMapper.getProcessListByAppIDs(appIDs);
        if (CollectionUtils.isEmpty(processListByAppIDs)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null ");
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        for (Process process : processListByAppIDs) {
            ProcessVo processVo = ProcessUtils.processPoToVo(process);
            if (null == processVo) {
                continue;
            }
            rtnMap.put(processVo.getAppId(), processVo);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Modify the process (only update the process table, the subtable is not updated)
     *
     * @param processVo
     * @return
     */
    @Override
    public int updateProcess(String username, boolean isAdmin, ProcessVo processVo) {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        if (null == processVo) {
            return 0;
        }
        Process processById = processMapper.getProcessById(username, isAdmin, processVo.getId());
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
        return processDomainU.updateProcess(processById);
    }

    /**
     * Generate Process and save according to flowId
     *
     * @param flowId
     * @return
     */
    @Override
    public ProcessVo flowToProcessAndSave(String username, String flowId) {
        //Determine if the flowId is empty
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
        process = processDomain.saveOrUpdate(process);
        if (null != process) {
            ProcessVo processVo = ProcessUtils.processPoToVo(process);
            return processVo;
        } else {
            logger.warn("Save failed, transform failed");
            return null;
        }
    }

    /**
     * Logical deletion
     *
     * @param processId
     * @return
     */
    @Override
    public String delProcess(String username, String processId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(processId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processID is null");
        }
        // Query Process by 'ProcessId'
        Process processById = processDomain.getProcessById(processId);
        if (null == processById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No process with ID of'" + processId + "'was queried");
        }
        if (processById.getState() == ProcessState.STARTED) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Status is STARTED, cannot be deleted");
        }
        processById.setEnableFlag(false);
        processById.setLastUpdateDttm(new Date());
        processById.setLastUpdateUser(username);
        processDomain.saveOrUpdate(processById);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Successfully Deleted");
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
        List<Process> processList = processMapper.getRunningProcessList(flowId);
        if (CollectionUtils.isEmpty(processList)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data");
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
        Map<String, Object> rtnMap = new HashMap<>();
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        Page<Process> page = PageHelper.startPage(offset, limit);
        processMapper.getProcessListByParam(username, isAdmin, param);
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        Page<Process> page = PageHelper.startPage(offset, limit);
        processMapper.getProcessGroupListByParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Start processes
     *
     * @param username
     * @param processId
     * @param checkpoint
     * @return
     */
    @Override
    public String startProcess(String username, String processId, String checkpoint, String runMode) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
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
        Process process = processDomain.getProcessById(processId);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data by process Id:'" + processId + "'");
        }
        Process processCopy = ProcessUtils.copyProcess(process, username, runModeType, false);
        if (null != processCopy) {
            processCopy = processDomain.saveOrUpdate(processCopy);
        }
        Map<String, Object> stringObjectMap = flowImpl.startFlow(processCopy, checkpoint, runModeType);
        processCopy.setLastUpdateUser(username);
        processCopy.setLastUpdateDttm(new Date());
        if (200 == (Integer) stringObjectMap.get("code")) {
            processCopy.setAppId((String) stringObjectMap.get("appId"));
            processCopy.setProcessId((String) stringObjectMap.get("appId"));
            processCopy.setState(ProcessState.STARTED);
            processDomain.saveOrUpdate(processCopy);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processId", processCopy.getId());
        } else {
            processCopy.setEnableFlag(false);
            processDomain.saveOrUpdate(processCopy);
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
    public String stopProcess(String username, boolean isAdmin, String processId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(processId)) {
            // Query Process by 'ProcessId'
            Process process = processMapper.getProcessById(username, isAdmin, processId);
            // Determine whether it is empty, and determine whether the save is successful.
            if (null != process) {
                String appId = process.getAppId();
                if (null != appId) {
                    if (ProcessState.STARTED == process.getState()) {
                        String stopFlow = flowImpl.stopFlow(appId);
                        if (StringUtils.isNotBlank(stopFlow) && !stopFlow.contains("Exception")) {
                            rtnMap.put("code", 200);
                            rtnMap.put("errorMsg", "Stop successful, return status is " + stopFlow);
                        } else {
                            logger.warn("Interface return value is null." + stopFlow);
                            rtnMap.put("errorMsg", "Interface return value is " + stopFlow);
                        }
                    } else {
                        logger.warn("The status of the process is " + process.getState() + " and cannot be stopped.");
                        rtnMap.put("errorMsg", "The status of the process is " + process.getState() + " and cannot be stopped.");
                    }
                } else {
                    logger.warn("The 'appId' of the 'process' is empty.");
                    rtnMap.put("errorMsg", "The 'appId' of the 'process' is empty.");
                }
            } else {
                logger.warn("No process ID is '" + processId + "' process");
                rtnMap.put("errorMsg", " No process ID is '" + processId + "' process");
            }
        } else {
            logger.warn("processId is null");
            rtnMap.put("errorMsg", "processId is null");
        }

        return JsonUtils.toJsonNoException(rtnMap);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        // (all parameters have values, and isanyempty returns false)
        if (StringUtils.isAnyBlank(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        String debugData = flowImpl.getDebugData(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName());
        if (StringUtils.isBlank(debugData)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Interface call failed");
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        // (all parameters have values, and isanyempty returns false)
        if (StringUtils.isAnyBlank(appID, stopName, visualizationType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        String visualizationData = flowImpl.getVisualizationData(appID, stopName, visualizationType);
        if (StringUtils.isBlank(visualizationData)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Interface call failed");
        }
        if (isSoft) {
            visualizationData = visualizationDataSort(visualizationData,visualizationType);
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
            Process processByPageId = processMapper.getProcessByPageId(username, isAdmin, processGroupId, pageId);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
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
        Map<String, Object> rtnMap = new HashMap<>();
        if (StringUtils.isNotBlank(amContainerLogs)) {
            rtnMap.put("code", 200);
            rtnMap.put("stdoutLog", amContainerLogs + "/stdout/?start=0");
            rtnMap.put("stderrLog", amContainerLogs + "/stderr/?start=0");
        } else {
            rtnMap.put("code", 200);
            rtnMap.put("stdoutLog", "Interface call failed");
            rtnMap.put("stderrLog", "Interface call failed");
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(loadId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'load' is null");
        }

        Process process = processMapper.getProcessById(username, isAdmin, loadId);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID : " + loadId);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        // set current user
        UserVo currentUser = new UserVo();
        currentUser.setUsername(username);
        rtnMap.put("currentUser", currentUser);
        //set parentAccessPath
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

        return JsonUtils.toJsonNoException(rtnMap);
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

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", "application_1613819551288_0150");
        map.put("stopName", "LineChart");
        map.put("visualizationType", "LINECHART");
        String url = "http://10.0.90.155:8002/flow/visualizationData";
        HttpUtils.doGet(url, map, 5 * 1000);
    }

}
