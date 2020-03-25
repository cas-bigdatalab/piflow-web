package com.nature.component.process.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessParentType;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.process.model.*;
import com.nature.component.process.service.IProcessGroupService;
import com.nature.component.process.utils.ProcessGroupUtils;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.component.process.vo.*;
import com.nature.domain.process.*;
import com.nature.mapper.process.ProcessGroupMapper;
import com.nature.mapper.process.ProcessMapper;
import com.nature.third.service.IFlow;
import com.nature.third.service.IGroup;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProcessGroupServiceImpl implements IProcessGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessGroupDomain processGroupDomain;

    

    @Autowired
    private ProcessDomain processDomain;

    @Resource
    private ProcessGroupMapper processGroupMapper;

    @Resource
    private ProcessMapper processMapper;

    @Autowired
    private IGroup groupImpl;

    @Autowired
    private IFlow flowImpl;
//
//    @Autowired
//    private ProcessStopDomain processStopDomain;
//
//    @Autowired
//    private ProcessStopPropertyDomain processStopPropertyDomain;
//
//    @Autowired
//    private ProcessPathDomain processPathDomain;
//    
//    @Autowired
//    private ProcessGroupPathDomain processGroupPathDomain;

    /**
     * Query processGroup based on id
     *
     * @param id
     * @return
     */
    @Override
    public ProcessGroupVo getProcessGroupById(String id) {
        ProcessGroupVo processGroupVo = null;
        ProcessGroup processGroupById = processGroupMapper.getProcessGroupById(id);
        processGroupVo = ProcessGroupUtils.processGroupPoToVo(processGroupById);
        return processGroupVo;
    }

    /**
     * Query processVo based on id (query contains its child table)
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ProcessGroupVo getProcessAllVoById(String id) {
        ProcessGroupVo processGroupVo = null;
        if (StringUtils.isNotBlank(id)) {
            ProcessGroup processGroupById = processGroupDomain.getProcessGroupById(id);
            processGroupVo = ProcessGroupUtils.processGroupPoToVo(processGroupById);
            ProcessGroup processGroup_parents = processGroupById.getProcessGroup();
            if (null != processGroup_parents) {
                ProcessGroupVo processGroupVo_parents = new ProcessGroupVo();
                BeanUtils.copyProperties(processGroup_parents, processGroupVo_parents);
                processGroupVo.setProcessGroupVo(processGroupVo_parents);
            }
        }
        return processGroupVo;
    }

    /**
     * Query processGroupVo based on id (only query process table)
     *
     * @param id
     * @return
     */
    @Override
    public ProcessGroupVo getProcessGroupVoById(String id) {
        ProcessGroupVo processGroupVo = null;
        if (StringUtils.isNotBlank(id)) {
            ProcessGroup processGroupById = processGroupMapper.getProcessGroupById(id);
            if (null != processGroupById) {
                processGroupVo = new ProcessGroupVo();
                BeanUtils.copyProperties(processGroupById, processGroupVo);
                processGroupVo.setCrtDttm(processGroupById.getCrtDttm());
            }
        }
        return processGroupVo;
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
            // find appinfo
            ProcessGroup processGroupByAppId = processGroupDomain.getProcessGroupByAppId(appID);
            ProcessGroupVo processGroupVo = ProcessGroupUtils.processGroupPoToVo(processGroupByAppId);
            if (null != processGroupVo) {
                rtnMap.put("code", 200);
                rtnMap.put("progress", (null != processGroupVo.getProgress() ? processGroupVo.getProgress() : "0.00"));
                rtnMap.put("state", (null != processGroupVo.getState() ? processGroupVo.getState().name() : "NO_STATE"));
                rtnMap.put("processGroupVo", processGroupVo);
            }
        } else {
            rtnMap.put("errorMsg", "appID is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Query  appInfo according to appID
     *
     * @param appIDs
     * @return
     */
    @Override
    public String getAppInfoByAppIds(String[] appIDs) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        if (null != appIDs && appIDs.length > 0) {
            List<ProcessGroup> processGroupListByAppIDs = processGroupMapper.getProcessGroupListByAppIDs(appIDs);
            if (CollectionUtils.isNotEmpty(processGroupListByAppIDs)) {
                rtnMap.put("code", 200);
                for (ProcessGroup processGroup : processGroupListByAppIDs) {
                    if (null != processGroup) {
                        ProcessGroupVo processGroupVo = ProcessGroupUtils.processGroupPoToVo(processGroup);
                        if (null != processGroupVo) {
                            rtnMap.put(processGroupVo.getAppId(), processGroupVo);
                        }
                    }
                }
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Start processesGroup
     *
     * @param processGroupId
     * @param checkpoint
     * @param currentUser
     * @return
     */
    @Override
    @Transactional
    public String startProcessGroup(String processGroupId, String checkpoint, String runMode, UserVo currentUser) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        if (StringUtils.isNotBlank(processGroupId) && null != currentUser) {
            // Query Process by 'processGroupId'
            ProcessGroup processGroupById = processGroupDomain.getProcessGroupById(processGroupId);
            // copy and Create
            ProcessGroup processGroupCopy = ProcessGroupUtils.copyProcessGroup(processGroupById, currentUser, runModeType);
            // ProcessGroup processGroupCopy = this.copyProcessGroupAndNewCreate(processGroupById, currentUser, runModeType);
            processGroupCopy = processGroupDomain.saveOrUpdate(processGroupCopy);

            if (null != processGroupCopy) {
                Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(processGroupCopy, runModeType);
                if (200 == (Integer) stringObjectMap.get("code")) {
                    processGroupCopy.setAppId((String) stringObjectMap.get("appId"));
                    processGroupCopy.setProcessId((String) stringObjectMap.get("appId"));
                    processGroupCopy.setState(ProcessState.STARTED);
                    processGroupCopy.setLastUpdateUser(currentUser.getUsername());
                    processGroupCopy.setLastUpdateDttm(new Date());
                    processGroupCopy.setProcessParentType(ProcessParentType.GROUP);
                    processGroupDomain.saveOrUpdate(processGroupCopy);
                    rtnMap.put("code", 200);
                    rtnMap.put("processGroupId", processGroupCopy.getId());
                    rtnMap.put("errorMsg", "Successful startup");
                    logger.info("save process success,update success");
                } else {
                    processGroupDomain.updateEnableFlagById(processGroupCopy.getId(), false);
                    rtnMap.put("errorMsg", "Calling interface failed, startup failed");
                    logger.warn("Calling interface failed, startup failed");
                }
            } else {
                rtnMap.put("errorMsg", "No process group Id'" + processGroupId + "'");
                logger.warn("No process group Id'" + processGroupId + "'");
            }
        } else {
            rtnMap.put("errorMsg", "processGroupId is null");
            logger.warn("processGroupId is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Query processGroupVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    @Override
    public String getProcessGroupVoListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
            Page<ProcessGroup> page = PageHelper.startPage(offset, limit);
            processGroupMapper.getProcessGroupListByParam(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Stop running processGroup
     *
     * @param processGroupId
     * @return
     */
    @Override
    public String stopProcessGroup(String processGroupId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(processGroupId)) {
            // Query Process by 'processGroupId'
            ProcessGroup processGroup = processGroupMapper.getProcessGroupById(processGroupId);
            // Determine whether it is empty, and determine whether the save is successful.
            if (null != processGroup) {
                String appId = processGroup.getAppId();
                if (null != appId) {
                    if (ProcessState.STARTED == processGroup.getState()) {
                        String stopFlow = groupImpl.stopFlowGroup(appId);
                        if (StringUtils.isNotBlank(stopFlow) && !stopFlow.contains("Exception")) {
                            rtnMap.put("code", 200);
                            rtnMap.put("errorMsg", "Stop successful, return status is " + stopFlow);
                        } else {
                            logger.warn("Interface return value is null." + stopFlow);
                            rtnMap.put("errorMsg", "Interface return value is " + stopFlow);
                        }
                    } else {
                        logger.warn("The status of the process is " + processGroup.getState() + " and cannot be stopped.");
                        rtnMap.put("errorMsg", "The status of the process is " + processGroup.getState() + " and cannot be stopped.");
                    }
                } else {
                    logger.warn("The 'appId' of the 'process' is empty.");
                    rtnMap.put("errorMsg", "The 'appId' of the 'process' is empty.");
                }
            } else {
                logger.warn("No process ID is '" + processGroupId + "' process");
                rtnMap.put("errorMsg", " No process ID is '" + processGroupId + "' process");
            }
        } else {
            logger.warn("processGroupId is null");
            rtnMap.put("errorMsg", "processGroupId is null");
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
    public DebugDataResponse getDebugData(DebugDataRequest debugDataRequest) {
        DebugDataResponse debugDataResponse = null;
        if (null != debugDataRequest) {
            // （isNoneEmpty returns false whenever there is a value）
            if (StringUtils.isNoneEmpty(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName())) {
                String debugData = flowImpl.getDebugData(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName());
                if (StringUtils.isNotBlank(debugData)) {
                    JSONObject obj = JSONObject.fromObject(debugData);
                    String schema = (String) obj.get("schema");
                    String debugDataPath = (String) obj.get("debugDataPath");
                    if (StringUtils.isNotBlank(schema) && StringUtils.isNotBlank(debugDataPath)) {
                        String[] schemaSplit = schema.split(",");
                        debugDataResponse = HdfsUtils.readPath(debugDataPath, debugDataRequest.getStartFileName(), debugDataRequest.getStartLine(), 10);
                        if (null != debugDataResponse) {
                            debugDataResponse.setSchema(Arrays.asList(schemaSplit));
                        }
                    }
                } else {
                    logger.warn("Interface call failed");
                }
            } else {
                logger.warn("param is null");
            }
        } else {
            logger.warn("param is null");
        }
        return debugDataResponse;
    }

    /**
     * delProcessGroup
     *
     * @param processGroupID
     * @return
     */
    @Override
    public String delProcessGroup(String processGroupID) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(processGroupID)) {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            // Query Process by 'processGroupID'
            ProcessGroup processGroupById = processGroupMapper.getProcessGroupById(processGroupID);
            if (null != processGroupById) {
                if (processGroupById.getState() != ProcessState.STARTED) {
                    int updateEnableFlagById = processGroupMapper.updateEnableFlagById(processGroupID, currentUser.getUsername());
                    // Determine whether the deletion is successful
                    if (updateEnableFlagById > 0) {
                        rtnMap.put("code", 200);
                        rtnMap.put("errorMsg", "Successfully Deleted");
                    } else {
                        logger.warn("Failed to delete");
                        rtnMap.put("errorMsg", "Failed to delete");
                    }
                } else {
                    logger.warn("Status is STARTED, cannot be deleted");
                    rtnMap.put("errorMsg", "Status is STARTED, cannot be deleted");
                }
            } else {
                logger.warn("No process ID is '" + processGroupID + "' process");
                rtnMap.put("errorMsg", "No process ID is '" + processGroupID + "' process");
            }
        } else {
            logger.warn("processGroupID is null");
            rtnMap.put("errorMsg", "processGroupID is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * getGroupLogData
     *
     * @param processGroupAppID
     * @return
     */
    @Override
    public String getGroupLogData(String processGroupAppID) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(processGroupAppID)) {
            // Query groupLogData by 'processGroupAppID'
            String groupLogData = groupImpl.getFlowGroupInfoStr(processGroupAppID);
            if (StringUtils.isNotBlank(groupLogData)) {
                rtnMap.put("code", 200);
                rtnMap.put("data", groupLogData);
            } else {
                logger.warn("No process ID is '" + processGroupAppID + "' process");
                rtnMap.put("errorMsg", "Interface return data is empty");
            }
        } else {
            logger.warn("processGroupID is null");
            rtnMap.put("errorMsg", "processGroupID is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * getStartGroupJson
     *
     * @param processGroupId
     * @return
     */
    @Override
    @Transactional
    public String getStartGroupJson(String processGroupId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isBlank(processGroupId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processGroupID is null");
        }
        // Query groupLogData by 'processGroupId'
        ProcessGroup processGroup = processGroupDomain.getProcessGroupById(processGroupId);
        if (null == processGroup) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No process ID is '" + processGroupId + "' process");
        }
        String formatJson = ProcessUtils.processGroupToJson(processGroup, processGroup.getRunModeType());
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", formatJson);
    }

    /**
     * getProcessIdByPageId
     *
     * @param fId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessIdByPageId(String fId, String pageId) {
        return processDomain.getProcessIdByPageId(fId, pageId);
    }

    /**
     * getProcessIdByPageId
     *
     * @param fId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessGroupIdByPageId(String fId, String pageId) {
        return processGroupDomain.getProcessIdGroupByPageId(fId, pageId);
    }

/*
    private ProcessGroup copyProcessGroupAndNewCreate(ProcessGroup processGroup, UserVo currentUser, RunModeType runModeType) {
        ProcessGroup processGroupCopy = null;
        if (null != currentUser) {
            String username = currentUser.getUsername();
            if (null != processGroup) {
                processGroupCopy = new ProcessGroup();
                processGroupCopy.setCrtUser(username);
                processGroupCopy.setCrtDttm(new Date());
                processGroupCopy.setLastUpdateUser(username);
                processGroupCopy.setLastUpdateDttm(new Date());
                processGroupCopy.setEnableFlag(true);
                processGroupCopy.setState(ProcessState.STARTED);
                processGroupCopy.setRunModeType(null != runModeType ? runModeType : RunModeType.RUN);
                processGroupCopy.setName(processGroup.getName());
                processGroupCopy.setDescription(processGroup.getDescription());
                processGroupCopy.setViewXml(processGroup.getViewXml());
                processGroupCopy.setFlowId(processGroup.getFlowId());
                processGroupCopy.setParentProcessId(StringUtils.isNotBlank(processGroup.getParentProcessId()) ? processGroup.getParentProcessId() : processGroup.getProcessId());
                processGroupCopy.setProcessParentType(ProcessParentType.GROUP);
                processGroupCopy = processGroupDomain.saveOrUpdate(processGroupCopy);
                List<ProcessGroupPath> processGroupPathList = processGroup.getProcessGroupPathList();
                if (null != processGroupPathList && processGroupPathList.size() > 0) {
                    List<ProcessGroupPath> processGroupPathListCopy = new ArrayList<>();
                    for (ProcessGroupPath processGroupPath : processGroupPathList) {
                        if (null != processGroupPath) {
                            ProcessGroupPath processGroupPathCopy = new ProcessGroupPath();
                            processGroupPathCopy.setCrtDttm(new Date());
                            processGroupPathCopy.setCrtUser(username);
                            processGroupPathCopy.setLastUpdateDttm(new Date());
                            processGroupPathCopy.setLastUpdateUser(username);
                            processGroupPathCopy.setEnableFlag(true);
                            processGroupPathCopy.setFrom(processGroupPath.getFrom());
                            processGroupPathCopy.setTo(processGroupPath.getTo());
                            processGroupPathCopy.setInport(processGroupPath.getInport());
                            processGroupPathCopy.setOutport(processGroupPath.getOutport());
                            processGroupPathCopy.setPageId(processGroupPath.getPageId());
                            processGroupPathCopy.setProcessGroup(processGroupCopy);
                            processGroupPathListCopy.add(processGroupPathCopy);
                        }
                    }
                    processGroupPathListCopy = processGroupPathDomain.saveOrUpdate(processGroupPathListCopy);
                    processGroupCopy.setProcessGroupPathList(processGroupPathListCopy);
                }
                List<Process> processList = processGroup.getProcessList();
                List<Process> processListCopy = this.copyProcessListAndNewCreate(processList, currentUser, runModeType, processGroupCopy);
                processGroupCopy.setProcessList(processListCopy);
            }
        }
        return processGroupCopy;
    }
*/

/*
    private List<Process> copyProcessListAndNewCreate(List<Process> processList, UserVo currentUser, RunModeType runModeType, ProcessGroup processGroup) {
        List<Process> processListCopy = null;
        if (null != processList && processList.size() > 0) {
            processListCopy = new ArrayList<>();
            for (Process process : processList) {
                Process processCopy = this.copyProcessAndNewCreate(process, currentUser, runModeType, processGroup);
                processListCopy.add(processCopy);
            }
        }
        return processListCopy;
    }
*/

/*
    private Process copyProcessAndNewCreate(Process process, UserVo currentUser, RunModeType runModeType, ProcessGroup processGroup) {
        Process processCopy = null;
        if (null != currentUser) {
            String username = currentUser.getUsername();
            if (StringUtils.isNotBlank(username) && null != process) {
                processCopy = new Process();
                processCopy.setId(SqlUtils.getUUID32());
                processCopy.setCrtUser(username);
                processCopy.setCrtDttm(new Date());
                processCopy.setLastUpdateUser(username);
                processCopy.setLastUpdateDttm(new Date());
                processCopy.setEnableFlag(true);
                processCopy.setState(ProcessState.INIT);
                processCopy.setRunModeType(null != runModeType ? runModeType : RunModeType.RUN);
                processCopy.setName(process.getName());
                processCopy.setDriverMemory(process.getDriverMemory());
                processCopy.setExecutorNumber(process.getExecutorNumber());
                processCopy.setExecutorMemory(process.getExecutorMemory());
                processCopy.setExecutorCores(process.getExecutorCores());
                processCopy.setDescription(process.getDescription());
                processCopy.setViewXml(process.getViewXml());
                processCopy.setFlowId(process.getFlowId());
                processCopy.setParentProcessId(StringUtils.isNotBlank(process.getParentProcessId()) ? process.getParentProcessId() : process.getProcessId());
                processCopy.setPageId(process.getPageId());
                processCopy.setProcessGroup(processGroup);
                processCopy.setProcessParentType(ProcessParentType.GROUP);
                processCopy = processDomain.saveOrUpdate(processCopy);
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
                    processPathListCopy = processPathDomain.saveOrUpdate(processPathListCopy);
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
                            processStopCopy = processStopDomain.saveOrUpdate(processStopCopy);
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
                                processStopPropertyListCopy = processStopPropertyDomain.saveOrUpdate(processStopPropertyListCopy);
                                processStopCopy.setProcessStopPropertyList(processStopPropertyListCopy);
                            }
                            processStopListCopy.add(processStopCopy);
                        }
                    }
                    processCopy.setProcessStopList(processStopListCopy);
                }
            }
        }
        return processCopy;
    }
*/

    /**
     * getProcessGroupVoByPageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @Override
    @Transactional
    public ProcessGroupVo getProcessGroupVoByPageId(String processGroupId, String pageId) {
        ProcessGroupVo processGroupVo = null;
        ProcessGroup processGroup = processGroupDomain.getProcessGroupByPageId(processGroupId, pageId);
        if (null != processGroup) {
            processGroupVo = new ProcessGroupVo();
            BeanUtils.copyProperties(processGroup, processGroupVo);
            // List<ProcessGroup> processGroupList = processGroup.getProcessGroupList();
            // List<Process> processList = processGroup.getProcessList();
            // if (null != processGroupList) {
            //     processGroupVo.setFlowGroupQuantity(processGroupList.size());
            // }
            // if (null != processList) {
            //     processGroupVo.setFlowQuantity(processList.size());
            // }
        }
        return processGroupVo;
    }

}