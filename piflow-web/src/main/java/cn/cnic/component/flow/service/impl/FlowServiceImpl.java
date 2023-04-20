package cn.cnic.component.flow.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowGroupDomain;
import cn.cnic.component.flow.domain.FlowStopsPublishingDomain;
import cn.cnic.component.flow.entity.*;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.utils.FlowGlobalParamsUtils;
import cn.cnic.component.flow.utils.PathsUtil;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.mxGraph.domain.MxCellDomain;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.mxGraph.utils.MxGraphUtils;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessVo;
import cn.cnic.component.schedule.domain.ScheduleDomain;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.vo.StopGroupVo;
import cn.cnic.controller.requestVo.FlowInfoVoRequestAdd;
import cn.cnic.controller.requestVo.FlowInfoVoRequestUpdate;
import cn.cnic.third.service.IFlow;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FlowServiceImpl implements IFlowService {

    private Logger logger = LoggerUtil.getLogger();

    private final ProcessDomain processDomain;
    private final MxCellDomain mxCellDomain;
    private final FlowDomain flowDomain;
    private final FlowGroupDomain flowGroupDomain;
    private final ScheduleDomain scheduleDomain;
    private final IStopGroupService stopGroupServiceImpl;
    private final IFlow flowImpl;
    private final DataSourceDomain dataSourceDomain;
    private final FlowStopsPublishingDomain flowStopsPublishingDomain;

    @Autowired
    public FlowServiceImpl(ProcessDomain processDomain,
                           MxCellDomain mxCellDomain,
                           FlowDomain flowDomain,
                           FlowGroupDomain flowGroupDomain,
                           ScheduleDomain scheduleDomain,
                           IStopGroupService stopGroupServiceImpl,
                           IFlow flowImpl,
                           DataSourceDomain dataSourceDomain, FlowStopsPublishingDomain flowStopsPublishingDomain) {
        this.processDomain = processDomain;
        this.mxCellDomain = mxCellDomain;
        this.flowDomain = flowDomain;
        this.flowGroupDomain = flowGroupDomain;
        this.scheduleDomain = scheduleDomain;
        this.stopGroupServiceImpl = stopGroupServiceImpl;
        this.flowImpl = flowImpl;
        this.dataSourceDomain = dataSourceDomain;
        this.flowStopsPublishingDomain = flowStopsPublishingDomain;
    }


    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     */
    @Override
    public Flow getFlowById(String username, boolean isAdmin, String id) {
        Flow flowById = flowDomain.getFlowById(id);
        if (null != flowById && !isAdmin) {
            Boolean isExample = flowById.getIsExample();
            String crtUser = flowById.getCrtUser();
            if ((!isExample) && (!username.equals(crtUser))) {
                flowById = null;
            }
        }
        return flowById;
    }

    /**
     * Query flow information based on pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    @Override
    public FlowVo getFlowByPageId(String fid, String pageId) {
        FlowVo flowVo = null;
        Flow flowById = flowDomain.getFlowByPageId(fid, pageId);
        if (null != flowById) {
            flowVo = new FlowVo();
            BeanUtils.copyProperties(flowById, flowVo);
            List<Stops> stopsList = flowById.getStopsList();
            if (null != stopsList) {
                flowVo.setStopQuantity(stopsList.size());
            }
        }
        return flowVo;
    }

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     */
    @Override
    public String getFlowVoById(String id) {
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        Flow flowById = flowDomain.getFlowById(id);
        if (null == flowById) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flow", null);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        FlowVo flowVo = new FlowVo();
        BeanUtils.copyProperties(flowById, flowVo);
        // Take out 'mxGraphModel' and convert to Vo
        MxGraphModelVo mxGraphModelVo = MxGraphModelUtils.mxGraphModelPoToVo(flowById.getMxGraphModel());
        // Take out 'stopsList' and turn it to Vo
        List<StopsVo> stopsVoList = StopsUtils.stopsListPoToVo(flowById.getStopsList());
        // Take out 'pathsList' and turn it to Vo
        List<PathsVo> pathsVoList = PathsUtil.pathsListPoToVo(flowById.getPathsList());
        flowVo.setMxGraphModelVo(mxGraphModelVo);
        flowVo.setStopsVoList(stopsVoList);
        flowVo.setPathsVoList(pathsVoList);
        if (null != stopsVoList) {
            flowVo.setStopQuantity(stopsVoList.size());
        }
        rtnMap.put("flow", flowVo);

        List<Process> processList = processDomain.getRunningProcessList(id);
        if (CollectionUtils.isNotEmpty(processList)) {
            List<ProcessVo> processVoList = new ArrayList<ProcessVo>();
            ProcessVo processVo;
            for (Process process : processList) {
                if (null == process) {
                    continue;
                }
                processVo = new ProcessVo();
                BeanUtils.copyProperties(process, processVo);
                processVoList.add(processVo);
            }
            rtnMap.put("runningProcessVoList", processVoList);
        }
        List<FlowGlobalParams> flowGlobalParamsByIds = flowById.getFlowGlobalParamsList();
        if (null != flowGlobalParamsByIds && flowGlobalParamsByIds.size() > 0 && flowGlobalParamsByIds.get(0) != null) {
        	rtnMap.put("globalParamsList", flowGlobalParamsByIds);
        }
        return ReturnMapUtils.toJson(rtnMap);
    }

    @Override
    public String addFlow(String username, FlowInfoVoRequestAdd flowVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == flowVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (StringUtils.isBlank(flowVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("name"));
        }
        String flowName = flowDomain.getFlowName(flowVo.getName());
        if (StringUtils.isNotBlank(flowName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DUPLICATE_NAME_MSG(flowName));
        }
        Flow flow = new Flow();
        BeanUtils.copyProperties(flowVo, flow);
        String id = UUIDUtils.getUUID32();
        flow.setId(id);
        flow.setCrtDttm(new Date());
        flow.setCrtUser(username);
        flow.setLastUpdateDttm(new Date());
        flow.setLastUpdateUser(username);
        flow.setEnableFlag(true);
        flow.setUuid(id);
        List<FlowGlobalParams> globalParamsIdToGlobalParams = FlowGlobalParamsUtils.globalParamsIdToGlobalParams(flowVo.getGlobalParamsIds());
        flow.setFlowGlobalParamsList(globalParamsIdToGlobalParams);
        int addFlow = flowDomain.addFlow(flow);
        if (addFlow <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
        }
        int optDataCount = addFlow;
        MxGraphModel mxGraphModel = new MxGraphModel();
        mxGraphModel.setFlow(flow);
        mxGraphModel.setId(UUIDUtils.getUUID32());
        mxGraphModel.setCrtDttm(new Date());
        mxGraphModel.setCrtUser(username);
        mxGraphModel.setLastUpdateDttm(new Date());
        mxGraphModel.setLastUpdateUser(username);
        mxGraphModel.setEnableFlag(true);
        if (null != mxGraphModel) {
            mxGraphModel.setFlow(flow);
            int addMxGraphModel = flowDomain.addMxGraphModel(mxGraphModel);
            if (addMxGraphModel > 0) {
                flow.setMxGraphModel(mxGraphModel);
                optDataCount += addMxGraphModel;
            }
        }
        if (optDataCount > 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowId", id);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
        }
    }

    @Override
    public String deleteFLowInfo(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        int scheduleIdListByScheduleRunTemplateId = scheduleDomain.getScheduleIdListByScheduleRunTemplateId(isAdmin, username, id);
        if (scheduleIdListByScheduleRunTemplateId > 0) {
        	return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_LINK_SCHEDULED_ERROR_MSG());
        }
        List<String> publishingNameList = flowStopsPublishingDomain.getPublishingNameListByFlowId(id);
        if (null != publishingNameList && publishingNameList.size() > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.STOP_PUBLISHED_CANNOT_DEL_FLOW_MSG(publishingNameList.toString().replace("[", "'").replace("]", "'")));
        }
        /*Flow flowById = this.getFlowById(username, isAdmin, id);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }*/
        // remove FLow
        int deleteFLowInfo = 0;
        try {
            deleteFLowInfo = flowDomain.deleteFlowInfoById(username, id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (deleteFLowInfo > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
    }

    @Override
    public Integer getMaxStopPageId(String flowId) {
        return flowDomain.getMaxStopPageId(flowId);
    }

    @Override
    public List<FlowVo> getFlowList() {
        List<FlowVo> flowVoList = new ArrayList<>();
        List<Flow> flowList = flowDomain.getFlowList();
        if (null != flowList && flowList.size() > 0) {
            for (Flow flow : flowList) {
                if (null != flow) {
                    FlowVo flowVo = new FlowVo();
                    BeanUtils.copyProperties(flow, flowVo);
                    flowVo.setCrtDttm(flow.getCrtDttm());
                    flowVoList.add(flowVo);
                }
            }
        }
        return flowVoList;
    }

    @Override
    public String getFlowListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<FlowVo> page = PageHelper.startPage(offset, limit,"crt_dttm desc");
        flowDomain.getFlowListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String getFlowExampleList() {
        List<FlowVo> flowVoList = new ArrayList<>();
        List<Flow> flowExampleList = flowDomain.getFlowExampleList();
        // list判空
        if (CollectionUtils.isEmpty(flowExampleList)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        flowExampleList.forEach(flow -> {
            FlowVo flowVo = new FlowVo();
            flowVo.setId(flow.getId());
            flowVo.setName(flow.getName());
            flowVoList.add(flowVo);
        });
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowExampleList", flowVoList);
    }

    @Override
    public String runFlow(String username, boolean isAdmin, String flowId, String runMode) throws Exception {
        logger.info("=======run flow start===============");
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("FlowId"));
        }
        // find flow by flowId
        Flow flowById = this.getFlowById(username, isAdmin, flowId);
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(flowId));
        }
        Process process = ProcessUtils.flowToProcess(flowById, username, false);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG());
        }
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        process.setRunModeType(runModeType);
        process.setId(UUIDUtils.getUUID32());
        int updateProcess = processDomain.addProcess(process);
        if (updateProcess <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG());
        }
        StringBuffer checkpoint = new StringBuffer();
        List<Stops> stopsList = flowById.getStopsList();
        for (Stops stops : stopsList) {
            stops.getIsCheckpoint();
            if (null == stops.getIsCheckpoint() || !stops.getIsCheckpoint()) {
                continue;
            }
            if (StringUtils.isNotBlank(checkpoint)) {
                checkpoint.append(",");
            }
            checkpoint.append(stops.getName());
        }
        String processId = process.getId();
        Map<String, Object> stringObjectMap = flowImpl.startFlow(process, checkpoint.toString(), runModeType);
        if (null == stringObjectMap || 200 != ((Integer) stringObjectMap.get("code"))) {
            processDomain.updateProcessEnableFlag(username, isAdmin, processId);
            return ReturnMapUtils.setFailedMsgRtnJsonStr((String) stringObjectMap.get("errorMsg"));
        }
        process = processDomain.getProcessById(username, isAdmin, processId);
        process.setLastUpdateDttm(new Date());
        process.setLastUpdateUser(username);
        process.setAppId((String) stringObjectMap.get("appId"));
        process.setProcessId((String) stringObjectMap.get("appId"));
        process.setState(ProcessState.STARTED);
        process.setLastUpdateUser(username);
        process.setLastUpdateDttm(new Date());
        processDomain.updateProcess(process);
        logger.info("========run flow finish==================");
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processId", process.getId());
    }

    @Override
    public String runFlowByPublishingId(String username, boolean isAdmin, String publishingId, String runMode) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(publishingId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("publishingId"));
        }
        List<String> flowIdByPublishingId = flowStopsPublishingDomain.getFlowIdByPublishingId(publishingId);
        if (null == flowIdByPublishingId || flowIdByPublishingId.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        return runFlow(username,isAdmin,flowIdByPublishingId.get(0), runMode);
    }

    @Override
    public String updateFlowBaseInfo(String username, String fId, FlowInfoVoRequestUpdate flowVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == flowVo || StringUtils.isBlank(flowVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Flow flowById = flowDomain.getFlowById(flowVo.getId());
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Database save failed");
        }
        if (StringUtils.isNotBlank(flowVo.getName())) {
            flowById.setName(flowVo.getName());
        }
        // find name in FlowGroup
        List<String> flowNamesInGroup = flowDomain.getFlowNamesByFlowGroupId(fId, flowVo.getName());// already add in group
        if (flowNamesInGroup.size() > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DUPLICATE_NAME_MSG(flowVo.getName()));
        }
        String[] flowGroupsInGroup = flowGroupDomain.getFlowGroupNameByNameInGroup(fId, flowVo.getName());
        if (flowGroupsInGroup.length > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DUPLICATE_NAME_MSG(flowVo.getName()));
        }

        flowById.setDescription(flowVo.getDescription());
        flowById.setDriverMemory(flowVo.getDriverMemory());
        flowById.setExecutorCores(flowVo.getExecutorCores());
        flowById.setExecutorMemory(flowVo.getExecutorMemory());
        flowById.setExecutorNumber(flowVo.getExecutorNumber());
        flowById.setLastUpdateDttm(new Date());
        flowById.setLastUpdateUser(username);
        List<FlowGlobalParams> globalParamsIdToGlobalParams = FlowGlobalParamsUtils.globalParamsIdToGlobalParams(flowVo.getGlobalParamsIds());
        flowById.setFlowGlobalParamsList(globalParamsIdToGlobalParams);
        flowDomain.updateFlow(flowById);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 200);
        rtnMap.put("flowVo", flowVo);
        rtnMap.put("errorMsg", "successfully saved");
        logger.info("The 'Flow' attribute was successfully modified.");
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(fId);
        if (null == flowGroup) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        if (null == mxGraphModel) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        MxCell meCellByMxGraphIdAndPageId = mxCellDomain.getMxCellByMxGraphIdAndPageId(mxGraphModel.getId(), flowVo.getPageId());
        if (null == meCellByMxGraphIdAndPageId) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        meCellByMxGraphIdAndPageId.setValue(flowVo.getName());
        int i = mxCellDomain.updateMxCell(meCellByMxGraphIdAndPageId);
        if (i <= 0) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        MxGraphModel mxGraphModelById = flowDomain.getMxGraphModelById(mxGraphModel.getId());
        // Convert the mxGraphModelById from the query to XML
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModelById);
        loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
        rtnMap.put("XmlData", loadXml);
        return ReturnMapUtils.toJson(rtnMap);
    }

    @Override
    public String updateFlowNameById(String username, String id, String flowGroupId, String flowName, String pageId) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isAnyEmpty(id, flowName, flowGroupId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameter is empty");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow query is null,flowGroupId:" + flowGroupId);
        }
        MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
        if (null == mxGraphModel) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(flowGroupById.getId() + "No flow information,update failed");
        }
        List<MxCell> root = mxGraphModel.getRoot();
        if (null == root || root.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(flowGroupById.getId() + "No flow information,update failed");
        }
        // Check if name is the same name
        String checkResult = flowDomain.getFlowIdByNameAndFlowGroupId(flowGroupId, flowName);
        if (StringUtils.isNotBlank(checkResult)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(flowName + "The name has been repeated and the save failed.");
        }
        boolean updateFlowNameById = this.updateFlowNameById(username, id, flowName);
        if (!updateFlowNameById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Modify flowName failed");
        }
        String loadXml = null;
        for (MxCell mxCell : root) {
            if (null == mxCell) {
                continue;
            }
            if (!pageId.equals(mxCell.getPageId())) {
                continue;
            }
            mxCell.setValue(flowName);
            mxCell.setLastUpdateDttm(new Date());
            mxCell.setLastUpdateUser(username);
            mxCellDomain.updateMxCell(mxCell);
            // Convert the mxGraphModel from the query to XML
            loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
            loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
            break;
        }
        if (StringUtils.isBlank(loadXml)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("nameContent", flowName);
        return ReturnMapUtils.appendValuesToJson(rtnMap, "XmlData", loadXml);
    }

    @Override
    public Boolean updateFlowNameById(String username, String id, String flowName) throws Exception {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        if (StringUtils.isBlank(id) || StringUtils.isBlank(flowName)) {
            return false;
        }
        Flow flowById = flowDomain.getFlowById(id);
        if (null == flowById) {
            return false;
        }
        flowById.setLastUpdateUser(username);
        flowById.setLastUpdateDttm(new Date());
        flowById.setName(flowName);
        flowDomain.updateFlow(flowById);
        return true;
    }

    @Override
    public Integer getMaxFlowPageIdByFlowGroupId(String flowGroupId) {
        return flowDomain.getMaxFlowPageIdByFlowGroupId(flowGroupId);
    }

    @Override
    public String drawingBoardData(String username, boolean isAdmin, String load, String parentAccessPath) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(load)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("load"));
        }
        // Query by loading'id'
        Flow flowById = this.getFlowById(username, isAdmin, load);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(load));
        }

        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());

        rtnMap.put("parentAccessPath", parentAccessPath);

        if (null != flowById.getFlowGroup()) {
            String parentsId = flowById.getFlowGroup().getId();
            rtnMap.put("parentsId", parentsId);
        }
        // Group on the left and'stops'
        List<StopGroupVo> groupsVoList = stopGroupServiceImpl.getStopGroupAll();
        rtnMap.put("groupsVoList", groupsVoList);
        // DataSource the left
        List<DataSourceVo> dataSourceVoList = dataSourceDomain.getStopDataSourceForFlowPage(username, isAdmin);
        rtnMap.put("dataSourceVoList",dataSourceVoList);
        Integer maxStopPageId = this.getMaxStopPageId(load);
        // 'maxStopPageId'defaults to 2 if it's empty, otherwise'maxStopPageId'+1
        maxStopPageId = null == maxStopPageId ? 2 : (maxStopPageId + 1);
        // Change the query'mxGraphModelVo'to'XML'
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, flowById.getMxGraphModel());
        rtnMap.put("xmlDate", loadXml);
        rtnMap.put("load", load);
        rtnMap.put("isExample", (null == flowById.getIsExample() ? false : flowById.getIsExample()));
        List<String> stopsDisabledPagesList = flowDomain.getStopsDisabledPagesListByFlowId(load);
        rtnMap.put("stopsDisabled", stopsDisabledPagesList);
        return ReturnMapUtils.toJson(rtnMap);
    }
}
