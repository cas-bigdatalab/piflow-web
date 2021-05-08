package cn.cnic.component.flow.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.*;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.utils.PathsUtil;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessVo;
import cn.cnic.component.schedule.mapper.ScheduleMapper;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.vo.StopGroupVo;
import cn.cnic.component.flow.jpa.domain.FlowDomain;
import cn.cnic.component.flow.jpa.domain.FlowGroupDomain;
import cn.cnic.component.mxGraph.jpa.domain.MxCellDomain;
import cn.cnic.component.process.jpa.domain.ProcessDomain;
import cn.cnic.component.mxGraph.mapper.MxCellMapper;
import cn.cnic.component.mxGraph.mapper.MxGeometryMapper;
import cn.cnic.component.mxGraph.mapper.MxGraphModelMapper;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.third.service.IFlow;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class FlowServiceImpl implements IFlowService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private MxGraphModelMapper mxGraphModelMapper;

    @Resource
    private MxCellMapper mxCellMapper;

    @Resource
    private MxCellDomain mxCellDomain;

    @Resource
    private MxGeometryMapper mxGeometryMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private StopsMapper stopsMapper;

    @Resource
    private PropertyMapper propertyMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private FlowGroupMapper flowGroupMapper;

    @Resource
    private ProcessDomain processDomain;

    @Resource
    private IFlow flowImpl;

    @Resource
    private FlowDomain flowDomain;

    @Resource
    private FlowGroupDomain flowGroupDomain;

    @Resource
    private IStopGroupService stopGroupServiceImpl;

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     */
    @Override
    public Flow getFlowById(String username, boolean isAdmin, String id) {
        Flow flowById = flowMapper.getFlowById(id);
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
    @Transactional
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
    @Transactional
    public String getFlowVoById(String id) {
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        Flow flowById = flowDomain.getFlowById(id);
        if (null == flowById) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flow", null);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        FlowVo flowVo = new FlowVo();
        BeanUtils.copyProperties(flowById, flowVo);
        //Take out 'mxGraphModel' and convert to Vo
        MxGraphModelVo mxGraphModelVo = MxGraphModelUtils.mxGraphModelPoToVo(flowById.getMxGraphModel());
        //Take out 'stopsList' and turn it to Vo
        List<StopsVo> stopsVoList = StopsUtils.stopsListPoToVo(flowById.getStopsList());
        //Take out 'pathsList' and turn it to Vo
        List<PathsVo> pathsVoList = PathsUtil.pathsListPoToVo(flowById.getPathsList());
        //Take out 'flowInfoDb' and turn it to Vo
        //FlowInfoDbVo flowInfoDbVo = FlowInfoDbUtil.flowInfoDbToVo(flowById.getAppId());
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
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String addFlow(String username, FlowVo flowVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == flowVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        if (StringUtils.isBlank(flowVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("flow name is null");
        }
        String flowName = flowMapper.getFlowName(flowVo.getName());
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
        int addFlow = flowMapper.addFlow(flow);
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
    @Transactional
    public String updateFlow(String username, Flow flow) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        String id = flow.getId();
        flow.setId(id);
        flow.setName(flow.getName());
        flow.setDescription(flow.getDescription());
        flow.setLastUpdateDttm(new Date());
        flow.setLastUpdateUser(username);
        Flow flowDb = flowMapper.getFlowById(flow.getId());
        flow.setVersion(flowDb.getVersion());
        int i = flowMapper.updateFlow(flow);
        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
    }

    @Override
    public String deleteFLowInfo(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        int scheduleIdListByScheduleRunTemplateId = scheduleMapper.getScheduleIdListByScheduleRunTemplateId(isAdmin, username, id);
        if (scheduleIdListByScheduleRunTemplateId > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Unable to delete, there is an associated scheduled task");
        }
        Flow flowById = this.getFlowById(username, isAdmin, id);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        if (null != flowById.getStopsList()) {
            //Loop delete stop attribute
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
                    mxCellMapper.updateEnableFlagById(username, mxcell.getId());

                }
            }
            mxGraphModelMapper.updateEnableFlagByFlowId(username, flowById.getId());
        }
        // remove FLow
        int deleteFLowInfo = flowMapper.updateEnableFlagById(username, id);
        if (deleteFLowInfo > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
    }

    @Override
    public Integer getMaxStopPageId(String flowId) {
        return flowMapper.getMaxStopPageId(flowId);
    }

    @Override
    public List<FlowVo> getFlowList() {
        List<FlowVo> flowVoList = new ArrayList<>();
        List<Flow> flowList = flowMapper.getFlowList();
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
        Map<String, Object> rtnMap = new HashMap<>();
        if (null != offset && null != limit) {
            Page<Flow> flowListPage = null;
            if (isAdmin) {
                flowListPage = flowDomain.getFlowListPage(offset - 1, limit, param);
            } else {
                flowListPage = flowDomain.getFlowListPageByUser(offset - 1, limit, param, username);
            }
            List<FlowVo> contentVo = new ArrayList<>();
            List<Flow> content = flowListPage.getContent();
            if (content.size() > 0) {
                for (Flow flow : content) {
                    if (null == flow) {
                        continue;
                    }
                    FlowVo flowVo = new FlowVo();
                    BeanUtils.copyProperties(flow, flowVo);
                    contentVo.add(flowVo);
                }
            }
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
            rtnMap.put("msg", "");
            rtnMap.put("count", flowListPage.getTotalElements());
            rtnMap.put("data", contentVo);//Data collection
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String getFlowExampleList() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        List<FlowVo> flowVoList = new ArrayList<>();
        List<Flow> flowExampleList = flowMapper.getFlowExampleList();
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
    public String runFlow(String username, boolean isAdmin, String flowId, String runMode) {
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
        process = processDomain.saveOrUpdate(process);
        if (null == process) {
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
        Map<String, Object> stringObjectMap = flowImpl.startFlow(process, checkpoint, runModeType);
        if (null == stringObjectMap || 200 != ((Integer) stringObjectMap.get("code"))) {
            process.setEnableFlag(false);
            process.setLastUpdateDttm(new Date());
            process.setLastUpdateUser(username);
            processDomain.saveOrUpdate(process);
            return ReturnMapUtils.setFailedMsgRtnJsonStr((String) stringObjectMap.get("errorMsg"));
        }
        process.setAppId((String) stringObjectMap.get("appId"));
        process.setProcessId((String) stringObjectMap.get("appId"));
        process.setState(ProcessState.STARTED);
        process.setLastUpdateUser(username);
        process.setLastUpdateDttm(new Date());
        processDomain.saveOrUpdate(process);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("save process success,update success");
        rtnMap.put("processId", process.getId());
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String updateFlowBaseInfo(String username, String fId, FlowVo flowVo) {
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
        //find name in FlowGroup
        String[] flowNamesInGroup = flowDomain.getFlowNameByFlowGroupId(fId, flowVo.getName());//already add in group
        if (flowNamesInGroup.length > 1) {
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
        flowDomain.saveOrUpdate(flowById);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 200);
        rtnMap.put("flowVo", flowVo);
        rtnMap.put("errorMsg", "successfully saved");
        logger.info("The 'Flow' attribute was successfully modified.");
        FlowGroup flowGroup = flowGroupMapper.getFlowGroupById(fId);
        if (null == flowGroup) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        if (null == mxGraphModel) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxCell meCellByMxGraphIdAndPageId = mxCellMapper.getMxCellByMxGraphIdAndPageId(mxGraphModel.getId(), flowVo.getPageId());
        if (null == meCellByMxGraphIdAndPageId) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        meCellByMxGraphIdAndPageId.setValue(flowVo.getName());
        int i = mxCellMapper.updateMxCell(meCellByMxGraphIdAndPageId);
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
    @Transactional
    public String updateFlowNameById(String username, String id, String flowGroupId, String flowName, String pageId) {
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
        //Check if name is the same name
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
                mxCellDomain.saveOrUpdate(mxCell);
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
    @Transactional
    public Boolean updateFlowNameById(String username, String id, String flowName) {
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
        flowDomain.saveOrUpdate(flowById);
        return true;
    }

    @Override
    public Integer getMaxFlowPageIdByFlowGroupId(String flowGroupId) {
        return flowMapper.getMaxFlowPageIdByFlowGroupId(flowGroupId);
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

        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);

        rtnMap.put("parentAccessPath", parentAccessPath);

        if (null != flowById.getFlowGroup()) {
            String parentsId = flowById.getFlowGroup().getId();
            rtnMap.put("parentsId", parentsId);
        }
        // Group on the left and'stops'
        List<StopGroupVo> groupsVoList = stopGroupServiceImpl.getStopGroupAll();
        rtnMap.put("groupsVoList", groupsVoList);
        Integer maxStopPageId = this.getMaxStopPageId(load);
        //'maxStopPageId'defaults to 2 if it's empty, otherwise'maxStopPageId'+1
        maxStopPageId = null == maxStopPageId ? 2 : (maxStopPageId + 1);
        // Change the query'mxGraphModelVo'to'XML'
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, flowById.getMxGraphModel());
        rtnMap.put("xmlDate", loadXml);
        rtnMap.put("load", load);
        rtnMap.put("isExample", (null == flowById.getIsExample() ? false : flowById.getIsExample()));
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
