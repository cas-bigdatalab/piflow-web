package cn.cnic.component.process.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.cnic.base.util.*;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.component.process.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.ProcessParentType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.entity.ProcessGroupPath;
import cn.cnic.component.process.service.IProcessGroupService;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.jpa.domain.ProcessDomain;
import cn.cnic.component.process.jpa.domain.ProcessGroupDomain;
import cn.cnic.component.process.jpa.domain.ProcessGroupPathDomain;
import cn.cnic.component.process.mapper.ProcessGroupMapper;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.service.IGroup;
import net.sf.json.JSONObject;

@Service
public class ProcessGroupServiceImpl implements IProcessGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessGroupDomain processGroupDomain;

    @Resource
    private ProcessGroupPathDomain processGroupPathDomain;

    @Resource
    private ProcessDomain processDomain;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessGroupMapper processGroupMapper;

    @Resource
    private IGroup groupImpl;

    @Resource
    private IFlow flowImpl;

    /**
     * Query processVo based on id (query contains its child table)
     *
     * @param id ProcessGroup Id
     * @return ProcessGroupVo (query contains its child table)
     */
    @Override
    public ProcessGroupVo getProcessGroupVoAllById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        ProcessGroup processGroupById = processGroupDomain.getProcessGroupById(id);
        return ProcessGroupUtils.processGroupPoToVo(processGroupById);
    }

    /**
     * Query processGroupVo based on id (only query process table)
     *
     * @param id ProcessGroup Id
     * @return ProcessGroupVo (Only themselves do not include subtables)
     */
    @Override
    public String getProcessGroupVoById(String id) {
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        ProcessGroup processGroupById = processGroupDomain.getProcessGroupById(id);
        if (null == processGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data is null");
        }
        ProcessGroupVo processGroupVo = new ProcessGroupVo();
        BeanUtils.copyProperties(processGroupById, processGroupVo);
        processGroupVo.setCrtDttm(processGroupById.getCrtDttm());
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processGroupVo", processGroupVo);
    }

    /**
     * Query appInfo according to appID
     *
     * @param appID appId
     * @return ProcessGroupVo
     */
    @Override
    public String getAppInfoByAppId(String appID) {

        if (StringUtils.isBlank(appID)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("appID is null");
        }
        // find appInfo
        ProcessGroup processGroupByAppId = processGroupDomain.getProcessGroupByAppId(appID);
        ProcessGroupVo processGroupVo = ProcessGroupUtils.processGroupPoToVo(processGroupByAppId);
        if (null == processGroupVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data was queried");

        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap.put("progress", (null != processGroupVo.getProgress() ? processGroupVo.getProgress() : "0.00"));
        rtnMap.put("state", (null != processGroupVo.getState() ? processGroupVo.getState().name() : "NO_STATE"));
        rtnMap.put("processGroupVo", processGroupVo);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Query  appInfo according to appID
     *
     * @param appIDs AppId array
     * @return string
     */
    @Override
    public String getAppInfoByAppIds(String[] appIDs) {
        if (null == appIDs || appIDs.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Incoming parameter is null");
        }
        List<ProcessGroup> processGroupListByAppIDs = processGroupMapper.getProcessGroupListByAppIDs(appIDs);
        if (CollectionUtils.isEmpty(processGroupListByAppIDs)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data was queried");
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        for (ProcessGroup processGroup : processGroupListByAppIDs) {
            if (null == processGroup) {
                continue;
            }
            ProcessGroupVo processGroupVo = ProcessGroupUtils.processGroupPoToVo(processGroup);
            if (null != processGroupVo) {
                rtnMap.put(processGroupVo.getAppId(), processGroupVo);
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Start processesGroup
     *
     * @param username       currentUser
     * @param processGroupId Run ProcessGroup Id
     * @param checkpoint     checkpoint
     * @return json
     */
    @Override
    @Transactional
    public String startProcessGroup(String username, String processGroupId, String checkpoint, String runMode) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(processGroupId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processGroupId is null");
        }
        RunModeType runModeType = null;
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        if (null == runModeType) {
            runModeType = RunModeType.RUN;
        }
        // Query Process by 'processGroupId'
        ProcessGroup processGroupById = processGroupDomain.getProcessGroupById(processGroupId);
        if (null == processGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data by process group Id'" + processGroupId + "'");
        }
        // copy and Create
        ProcessGroup copyProcessGroup = ProcessGroupUtils.copyProcessGroup(processGroupById, username, runModeType, false);
        copyProcessGroup = processGroupDomain.saveOrUpdate(username, copyProcessGroup);

        Map<String, Object> rtnMap = new HashMap<>();
        Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(copyProcessGroup, runModeType);
        copyProcessGroup.setLastUpdateUser(username);
        copyProcessGroup.setLastUpdateDttm(new Date());
        if (200 == (Integer) stringObjectMap.get("code")) {
            copyProcessGroup.setAppId((String) stringObjectMap.get("appId"));
            copyProcessGroup.setProcessId((String) stringObjectMap.get("appId"));
            copyProcessGroup.setState(ProcessState.STARTED);
            copyProcessGroup.setProcessParentType(ProcessParentType.GROUP);
            processGroupDomain.saveOrUpdate(username, copyProcessGroup);
            rtnMap.put("processGroupId", copyProcessGroup.getId());
            rtnMap.put("errorMsg", "Successful startup");
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
            logger.info("save process success,update success");
        } else {
            copyProcessGroup.setEnableFlag(false);
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.ERROR_CODE);
            rtnMap.put("errorMsg", "Calling interface failed, startup failed");
            logger.warn("Calling interface failed, startup failed");
            processGroupDomain.saveOrUpdate(username, copyProcessGroup);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Query processGroupVoList (parameter space-time non-paging)
     *
     * @param username username
     * @param isAdmin  isAdmin
     * @param offset   Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return json
     */
    @Override
    public String getProcessGroupVoListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        Page<ProcessGroup> processGroupListPage = processGroupDomain.getProcessGroupListPage(username, isAdmin, offset - 1, limit, param);
        List<ProcessGroup> content = processGroupListPage.getContent();
        List<ProcessGroupVo> processGroupVoList = null;
        if (null != content && content.size() > 0) {
            processGroupVoList = new ArrayList<>();
            ProcessGroupVo processGroupVo;
            for (ProcessGroup processGroup : content) {
                if (null == processGroup) {
                    continue;
                }
                processGroupVo = new ProcessGroupVo();
                BeanUtils.copyProperties(processGroup, processGroupVo);
                processGroupVoList.add(processGroupVo);
            }
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap.put("msg", "");
        rtnMap.put("count", processGroupListPage.getTotalElements());
        rtnMap.put("data", processGroupVoList);//Data collection
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Stop running processGroup
     *
     * @param processGroupId ProcessGroup Id
     * @return json
     */
    @Override
    public String stopProcessGroup(String username, boolean isAdmin, String processGroupId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(processGroupId)) {
            // Query Process by 'processGroupId'
            ProcessGroup processGroup = processGroupMapper.getProcessGroupByIdAndUser(username, isAdmin, processGroupId);
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
     * @param debugDataRequest DebugDataRequest
     * @return DebugDataResponse
     */
    @Override
    public DebugDataResponse getDebugData(DebugDataRequest debugDataRequest) {
        DebugDataResponse debugDataResponse = null;
        if (null == debugDataRequest) {
            logger.warn("param is null");
            return null;
        }
        // Returns true when all is null
        if (StringUtils.isAllEmpty(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName())) {
            logger.warn("param is null");
            return null;
        }
        String debugData = flowImpl.getDebugData(debugDataRequest.getAppID(), debugDataRequest.getStopName(), debugDataRequest.getPortName());
        if (StringUtils.isBlank(debugData)) {
            logger.warn("Interface call failed");
            return null;
        }
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
        return debugDataResponse;
    }

    /**
     * delProcessGroup
     *
     * @param processGroupID ProcessGroup Id
     * @return json
     */
    @Override
    public String delProcessGroup(String username, boolean isAdmin, String processGroupID) {
        if (StringUtils.isBlank(processGroupID)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processGroupID is null");
        }
        // Query Process by 'processGroupID'
        ProcessGroup processGroupById = processGroupMapper.getProcessGroupByIdAndUser(username, isAdmin, processGroupID);
        if (null == processGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No process ID is '" + processGroupID + "' process");
        }
        if (processGroupById.getState() == ProcessState.STARTED) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Status is STARTED, cannot be deleted");
        }
        int updateEnableFlagById = processGroupMapper.updateEnableFlagById(processGroupID, username);
        // Determine whether the deletion is successful
        if (updateEnableFlagById > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Successfully Deleted");
        } else {
            logger.warn("Failed to delete");
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Failed to delete");
        }
    }

    /**
     * getGroupLogData
     *
     * @param processGroupAppID ProcessGroup AppId
     * @return json
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
     * @param processGroupId ProcessGroup Id
     * @return json
     */
    @Override
    @Transactional
    public String getStartGroupJson(String processGroupId) {
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
     * @param fId    Parents Id
     * @param pageId MxGraph PageId
     * @return json
     */
    @Override
    public String getProcessIdByPageId(String fId, String pageId) {
        return processDomain.getProcessIdByPageId(fId, pageId);
    }

    /**
     * getProcessGroupIdByPageId
     *
     * @param fId    Parents Id
     * @param pageId MxGraph PageId
     * @return json
     */
    @Override
    public String getProcessGroupIdByPageId(String fId, String pageId) {
        return processGroupDomain.getProcessIdGroupByPageId(fId, pageId);
    }

    /**
     * getProcessGroupVoByPageId
     *
     * @param processGroupId ProcessGroup Id
     * @param pageId         MxGraph PageId
     * @return json
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

    /**
     * getProcessGroupPathVoByPageId
     *
     * @param processGroupId ProcessGroup Id
     * @param pageId         MxGraph PageId
     * @return json
     */
    public String getProcessGroupPathVoByPageId(String processGroupId, String pageId) {
        if (StringUtils.isAnyEmpty(processGroupId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        ProcessGroupPath processGroupPathByPageId = processGroupPathDomain.getProcessGroupPathByPageId(processGroupId, pageId);
        if (null == processGroupPathByPageId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        }
        List<String> pageIds = new ArrayList<>();
        String pathTo = processGroupPathByPageId.getTo();
        String pathFrom = processGroupPathByPageId.getFrom();
        if (StringUtils.isNotBlank(pathFrom)) {
            pageIds.add(pathFrom);
        }
        if (StringUtils.isNotBlank(pathTo)) {
            pageIds.add(pathTo);
        }
        if (StringUtils.isBlank(processGroupId) || null == pageIds || pageIds.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        List<Map<String, Object>> processGroupNamesAndPageIdsByPageIds = processGroupDomain.getProcessGroupNamesAndPageIdsByPageIds(processGroupId, pageIds);
        if (null == processGroupNamesAndPageIdsByPageIds || processGroupNamesAndPageIdsByPageIds.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        ProcessGroupPathVo processGroupPathVo = new ProcessGroupPathVo();
        pathTo = (null == pathTo ? "" : pathTo);
        pathFrom = (null == pathTo ? "" : pathFrom);
        for (Map<String, Object> processGroupNameAndPageId : processGroupNamesAndPageIdsByPageIds) {
            if (null != processGroupNameAndPageId) {
                String currentpageId = (String) processGroupNameAndPageId.get("pageId");
                if (pathTo.equals(currentpageId)) {
                    processGroupPathVo.setTo((String) processGroupNameAndPageId.get("name"));
                } else if (pathFrom.equals(currentpageId)) {
                    processGroupPathVo.setFrom((String) processGroupNameAndPageId.get("name"));
                }
            }
        }
        processGroupPathVo.setInport(StringUtils.isNotBlank(processGroupPathByPageId.getInport()) ? processGroupPathByPageId.getInport() : PortType.DEFAULT.getText());
        processGroupPathVo.setOutport(StringUtils.isNotBlank(processGroupPathByPageId.getOutport()) ? processGroupPathByPageId.getOutport() : PortType.DEFAULT.getText());

        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processGroupPathVo", processGroupPathVo);
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
        // Determine whether there is an'id'('load') of'Flow', and if there is, load it, otherwise generate'UUID' to return to the return page.
        if (StringUtils.isBlank(loadId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'load' is null");
        }

        ProcessGroup processGroup = processGroupMapper.getProcessGroupByIdAndUser(username, isAdmin, loadId);
        if (null == processGroup) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID : " + loadId);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        //set loadId
        rtnMap.put("load", loadId);
        // set current user
        UserVo currentUser = new UserVo();
        currentUser.setUsername(username);
        rtnMap.put("currentUser", currentUser);
        rtnMap.put("processType", "GROUP");
        //set parentAccessPath
        rtnMap.put("parentAccessPath", parentAccessPath);

        ProcessGroup parentsProcessGroup = processGroup.getProcessGroup();
        if (null != parentsProcessGroup) {
            rtnMap.put("parentsId", parentsProcessGroup.getId());
        }

        // process state
        String processStateStr = (processGroup.getState() != null ? processGroup.getState().name() : ProcessState.INIT.name());
        rtnMap.put("processState", processStateStr);

        //node pageId and state (process and processGroup)
        List<Map<String, String>> nodePageIdAndStateList = new ArrayList<>();
        // processGroupList
        List<ProcessGroup> processGroupList = processGroup.getProcessGroupList();
        if (null != processGroupList && processGroupList.size() > 0) {
            Map<String, String> processGroupNode;
            for (ProcessGroup processGroup_i : processGroupList) {
                if (null == processGroup_i) {
                    continue;
                }
                processGroupNode = new HashMap<>();
                processGroupNode.put("pageId", processGroup_i.getPageId());
                processGroupNode.put("state", (null != processGroup_i.getState()) ? processGroup_i.getState().getText() : ProcessState.INIT.name());
                nodePageIdAndStateList.add(processGroupNode);
            }
        }
        // processList
        List<Process> processList = processGroup.getProcessList();
        Map<String, String> processNode;
        if (null != processList && processList.size() > 0) {
            for (Process process_i : processList) {
                if (null == process_i) {
                    continue;
                }
                String process_i_stateStr = (null != process_i.getState() ? process_i.getState().getText() : "INIT");
                processNode = new HashMap<>();
                processNode.put("pageId", process_i.getPageId());
                processNode.put("state", process_i_stateStr);
                nodePageIdAndStateList.add(processNode);
            }
        }
        rtnMap.put("nodePageIdAndStateList", nodePageIdAndStateList);


        rtnMap.put("parentProcessId", processGroup.getParentProcessId());
        rtnMap.put("percentage", (null != processGroup.getProgress() ? processGroup.getProgress() : 0.00));
        rtnMap.put("appId", processGroup.getAppId());
        rtnMap.put("pID", processGroup.getProcessId());

        MxGraphModel mxGraphModel = processGroup.getMxGraphModel();
        if (null != mxGraphModel) {
            List<MxCell> root = mxGraphModel.getRoot();
            if (null != root && root.size() > 0) {
                List<MxCell> iconTranslate = new ArrayList<>();
                MxCell iconMxCell;
                for (MxCell mxCell : root) {
                    if (null == mxCell) {
                        continue;
                    }
                    String style = mxCell.getStyle();
                    if (StringUtils.isBlank(style) || style.indexOf("image;") != 0) {
                        continue;
                    }
                    if (null == mxCell.getMxGeometry()) {
                        continue;
                    }
                    iconMxCell = MxCellUtils.initIconMxCell(mxCell);
                    if (null == iconMxCell) {
                        continue;
                    }
                    iconTranslate.add(iconMxCell);
                }
                root.addAll(iconTranslate);
            }
            mxGraphModel.setRoot(root);
        }
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
        rtnMap.put("xmlDate", loadXml);

        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * getProcessGroupNode
     *
     * @param username
     * @param isAdmin
     * @param processGroupId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessGroupNode(String username, boolean isAdmin, String processGroupId, String pageId) {
        if (StringUtils.isAnyEmpty(processGroupId, pageId)) {
            logger.warn("Parameter passed in incorrectly");
        }

        String nodeType = "flow";
        Process process = processMapper.getProcessByPageId(username, isAdmin, processGroupId, pageId);
        ProcessGroup processGroup = processGroupDomain.getProcessGroupByPageId(processGroupId, pageId);
        ProcessVo processVo = null;
        ProcessGroupVo processGroupVo = null;
        if (null != process) {
            processVo = ProcessUtils.processPoToVo(process);
            nodeType = "flow";
        } else if (null != processGroup) {
            processGroupVo = new ProcessGroupVo();
            BeanUtils.copyProperties(processGroup, processGroupVo);
            nodeType = "flowGroup";
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap.put("processVo", processVo);
        rtnMap.put("processGroupVo", processGroupVo);
        rtnMap.put("nodeType", nodeType);
        return JsonUtils.toJsonNoException(rtnMap);
    }

}