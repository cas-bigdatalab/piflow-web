package cn.cnic.component.flow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.mxGraph.utils.MxGraphUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowGroupDomain;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGlobalParams;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.mapper.PropertyMapper;
import cn.cnic.component.flow.mapper.StopsMapper;
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
import cn.cnic.component.mxGraph.mapper.MxGeometryMapper;
import cn.cnic.component.mxGraph.mapper.MxGraphModelMapper;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessVo;
import cn.cnic.component.schedule.domain.ScheduleDomain;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.vo.StopGroupVo;
import cn.cnic.controller.requestVo.FlowInfoVoRequestAdd;
import cn.cnic.controller.requestVo.FlowInfoVoRequestUpdate;
import cn.cnic.third.service.IFlow;


@Service
public class FlowServiceImpl implements IFlowService {

    private Logger logger = LoggerUtil.getLogger();

    private final PathsMapper pathsMapper;
    private final StopsMapper stopsMapper;
    private final PropertyMapper propertyMapper;
    private final ProcessMapper processMapper;
    private final MxGraphModelMapper mxGraphModelMapper;
    private final ProcessDomain processDomain;
    private final IFlow flowImpl;
    private final MxGeometryMapper mxGeometryMapper;
    private final MxCellDomain mxCellDomain;
    private final FlowDomain flowDomain;
    private final FlowGroupDomain flowGroupDomain;
    private final IStopGroupService stopGroupServiceImpl;
    private final ScheduleDomain scheduleDomain;

    @Autowired
    public FlowServiceImpl(PathsMapper pathsMapper,
                           StopsMapper stopsMapper,
                           PropertyMapper propertyMapper,
                           ProcessMapper processMapper,
                           MxGraphModelMapper mxGraphModelMapper,
                           ProcessDomain processDomain,
                           IFlow flowImpl,
                           MxGeometryMapper mxGeometryMapper,
                           MxCellDomain mxCellDomain,
                           FlowDomain flowDomain,
                           FlowGroupDomain flowGroupDomain,
                           IStopGroupService stopGroupServiceImpl,
                           ScheduleDomain scheduleDomain) {
        this.pathsMapper = pathsMapper;
        this.stopsMapper = stopsMapper;
        this.propertyMapper = propertyMapper;
        this.processMapper = processMapper;
        this.mxGraphModelMapper = mxGraphModelMapper;
        this.processDomain = processDomain;
        this.flowImpl = flowImpl;
        this.mxGeometryMapper = mxGeometryMapper;
        this.mxCellDomain = mxCellDomain;
        this.flowDomain = flowDomain;
        this.flowGroupDomain = flowGroupDomain;
        this.stopGroupServiceImpl = stopGroupServiceImpl;
        this.scheduleDomain = scheduleDomain;
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        Flow flowById = flowDomain.getFlowById(id);
        if (null == flowById) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flow", null);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
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

        List<Process> processList = processMapper.getRunningProcessList(id);
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
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String addFlow(String username, FlowInfoVoRequestAdd flowVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == flowVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        if (StringUtils.isBlank(flowVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("flow name is null");
        }
        String flowName = flowDomain.getFlowName(flowVo.getName());
        if (StringUtils.isNotBlank(flowName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Repeat flow name!");
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("add failed");
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
            int addMxGraphModel = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
            if (addMxGraphModel > 0) {
                flow.setMxGraphModel(mxGraphModel);
                optDataCount += addMxGraphModel;
            }
        }
        if (optDataCount > 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowId", id);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("add failed");
        }
    }

    @Override
    public String deleteFLowInfo(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        int scheduleIdListByScheduleRunTemplateId = scheduleDomain.getScheduleIdListByScheduleRunTemplateId(isAdmin, username, id);
        if (scheduleIdListByScheduleRunTemplateId > 0) {
        	return ReturnMapUtils.setFailedMsgRtnJsonStr("Unable to delete, there is an associated scheduled task");
        }
        Flow flowById = this.getFlowById(username, isAdmin, id);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        if (null != flowById.getStopsList()) {
            // Loop delete stop attribute
            for (Stops stopId : flowById.getStopsList()) {
                if (null != stopId.getProperties())
                    for (Property property : stopId.getProperties()) {
                        propertyMapper.updateEnableFlagByStopId(username, property.getId());
                    }
            }
        }
        // remove stop
        stopsMapper.updateEnableFlagByFlowId(username, flowById.getId());
        // remove paths
        pathsMapper.updateEnableFlagByFlowId(username, flowById.getId());
        if (null != flowById.getMxGraphModel()) {
            List<MxCell> root = flowById.getMxGraphModel().getRoot();
            if (null != root && !root.isEmpty()) {
                for (MxCell mxcell : root) {
                    if (mxcell.getMxGeometry() != null) {
                        logger.info(mxcell.getMxGeometry().getId());
                        mxGeometryMapper.updateEnableFlagById(username, mxcell.getMxGeometry().getId());
                    }
                    mxCellDomain.updateEnableFlagById(username, mxcell.getId());

                }
            }
            mxGraphModelMapper.updateEnableFlagByFlowId(username, flowById.getId());
        }
        // remove FLow
        int deleteFLowInfo = flowDomain.updateEnableFlagById(username, id);
        if (deleteFLowInfo > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        Page<FlowVo> page = PageHelper.startPage(offset, limit,"crt_dttm desc");
        flowDomain.getFlowListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String getFlowExampleList() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        List<FlowVo> flowVoList = new ArrayList<>();
        List<Flow> flowExampleList = flowDomain.getFlowExampleList();
        // list判空
        if (CollectionUtils.isNotEmpty(flowExampleList)) {
            flowExampleList.forEach(flow -> {
                FlowVo flowVo = new FlowVo();
                flowVo.setId(flow.getId());
                flowVo.setName(flow.getName());
                flowVoList.add(flowVo);
            });
            rtnMap.put("code", 200);
            rtnMap.put("flowExampleList", flowVoList);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String runFlow(String username, boolean isAdmin, String flowId, String runMode) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowId is null");
        }
        // find flow by flowId
        Flow flowById = this.getFlowById(username, isAdmin, flowId);
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow with FlowId" + flowId + "was not queried");
        }
        Process process = ProcessUtils.flowToProcess(flowById, username, false);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Conversion failed");
        }
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        process.setRunModeType(runModeType);
        process.setId(UUIDUtils.getUUID32());
        int updateProcess = processDomain.addProcess(process);
        if (updateProcess <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Save failed, transform failed");
        }
        String checkpoint = "";
        List<Stops> stopsList = flowById.getStopsList();
        for (Stops stops : stopsList) {
            stops.getIsCheckpoint();
            if (null != stops.getIsCheckpoint() && stops.getIsCheckpoint()) {
                if (StringUtils.isNotBlank(checkpoint)) {
                    checkpoint = (checkpoint + ",");
                }
                checkpoint = (checkpoint + stops.getName());
            }
        }
        String processId = process.getId();
        Map<String, Object> stringObjectMap = flowImpl.startFlow(process, checkpoint, runModeType);
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
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("save process success,update success");
        rtnMap.put("processId", process.getId());
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String updateFlowBaseInfo(String username, String fId, FlowInfoVoRequestUpdate flowVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == flowVo || StringUtils.isBlank(flowVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed error");
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Repeat flow name!");
        }
        String[] flowGroupsInGroup = flowGroupDomain.getFlowGroupNameByNameInGroup(fId, flowVo.getName());
        if (flowGroupsInGroup.length > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Repeat flow name!");
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
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        if (null == mxGraphModel) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxCell meCellByMxGraphIdAndPageId = mxCellDomain.getMxCellByMxGraphIdAndPageId(mxGraphModel.getId(), flowVo.getPageId());
        if (null == meCellByMxGraphIdAndPageId) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        meCellByMxGraphIdAndPageId.setValue(flowVo.getName());
        int i = mxCellDomain.updateMxCell(meCellByMxGraphIdAndPageId);
        if (i <= 0) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxGraphModel mxGraphModelById = mxGraphModelMapper.getMxGraphModelById(mxGraphModel.getId());
        // Convert the mxGraphModelById from the query to XML
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModelById);
        loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
        rtnMap.put("XmlData", loadXml);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String updateFlowNameById(String username, String id, String flowGroupId, String flowName, String pageId) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
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
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        for (MxCell mxCell : root) {
            if (null == mxCell) {
                continue;
            }
            if (mxCell.getPageId().equals(pageId)) {
                mxCell.setValue(flowName);
                mxCell.setLastUpdateDttm(new Date());
                mxCell.setLastUpdateUser(username);
                mxCellDomain.updateMxCell(mxCell);
                // Convert the mxGraphModel from the query to XML
                String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
                loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
                rtnMap.put("XmlData", loadXml);
                rtnMap.put("nameContent", flowName);
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "Successfully modified");
                logger.info("Successfully modified");
                rtnMap.put("errorMsg", "Successfully modified");
                break;
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(load)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'load' is null");
        }
        // Query by loading'id'
        Flow flowById = this.getFlowById(username, isAdmin, load);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID : " + load);
        }

        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));

        rtnMap.put("parentAccessPath", parentAccessPath);

        if (null != flowById.getFlowGroup()) {
            String parentsId = flowById.getFlowGroup().getId();
            rtnMap.put("parentsId", parentsId);
        }
        // Group on the left and'stops'
        List<StopGroupVo> groupsVoList = stopGroupServiceImpl.getStopGroupAll();
        rtnMap.put("groupsVoList", groupsVoList);
        Integer maxStopPageId = this.getMaxStopPageId(load);
        // 'maxStopPageId'defaults to 2 if it's empty, otherwise'maxStopPageId'+1
        maxStopPageId = null == maxStopPageId ? 2 : (maxStopPageId + 1);
        // Change the query'mxGraphModelVo'to'XML'
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, flowById.getMxGraphModel());
        rtnMap.put("xmlDate", loadXml);
        rtnMap.put("load", load);
        rtnMap.put("isExample", (null == flowById.getIsExample() ? false : flowById.getIsExample()));
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
