package cn.cnic.component.flow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomain;
import cn.cnic.component.mxGraph.utils.MxGraphUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.ProcessParentType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowGroupDomain;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.utils.FlowGroupPathsUtil;
import cn.cnic.component.flow.utils.FlowUtil;
import cn.cnic.component.flow.vo.FlowGroupPathsVo;
import cn.cnic.component.flow.vo.FlowGroupVo;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.mxGraph.domain.MxCellDomain;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGeometry;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.utils.MxGraphComponentVoUtils;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.domain.ProcessGroupDomain;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.schedule.domain.ScheduleDomain;
import cn.cnic.controller.requestVo.FlowGroupInfoVoRequest;
import cn.cnic.controller.requestVo.FlowGroupInfoVoRequestUpDate;
import cn.cnic.third.service.IGroup;


@Service
public class FlowGroupServiceImpl implements IFlowGroupService {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private MxGraphModelDomain mxGraphModelDomain;
    @Autowired
    private FlowGroupDomain flowGroupDomain;
    @Autowired
    private FlowDomain flowDomain;
    @Autowired
    private ProcessGroupDomain processGroupDomain;
    @Autowired
    private MxCellDomain mxCellDomain;
    @Autowired
    private ScheduleDomain scheduleDomain;
    @Autowired
    private IFlowService flowServiceImpl;
    @Autowired
    private IGroup groupImpl;

    /**
     * get FlowGroup by id
     *
     * @param username
     * @param isAdmin
     * @param flowGroupId
     * @return
     */
    @Override
    public FlowGroup getFlowGroupById(String username, boolean isAdmin, String flowGroupId) {
        // Determine whether there is a flowGroup id (flowGroupId)
        if (StringUtils.isBlank(flowGroupId)) {
            return null;
        }
        return flowGroupDomain.getFlowGroupById(flowGroupId);
    }

    /**
     * getFlowGroupVoById
     *
     * @param flowGroupId
     * @return
     */
    @Override
    public String getFlowGroupVoInfoById(String flowGroupId) {
        if (StringUtils.isBlank(flowGroupId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("flowGroupId"));
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        FlowGroupVo flowGroupVo = new FlowGroupVo();
        BeanUtils.copyProperties(flowGroupById, flowGroupVo);
        flowGroupVo.setFlowQuantity((null != flowGroupById.getFlowList() ? flowGroupById.getFlowList().size() : 0));
        flowGroupVo.setFlowGroupQuantity((null != flowGroupById.getFlowGroupList() ? flowGroupById.getFlowGroupList().size() : 0));
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowGroupVo", flowGroupVo);    
    }

    /**
     * Query FlowGroupVo information based on pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    @Override
    public FlowGroupVo getFlowGroupByPageId(String fid, String pageId) {
        if (StringUtils.isBlank(fid) || StringUtils.isBlank(pageId)) {
            return null;
        }
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupByPageId(fid, pageId);
        if (null == flowGroup) {
            return null;
        }
        FlowGroupVo flowGroupVo = new FlowGroupVo();
        BeanUtils.copyProperties(flowGroup, flowGroupVo);
        List<FlowGroup> flowGroupList = flowGroup.getFlowGroupList();
        List<Flow> flowList = flowGroup.getFlowList();
        if (null != flowGroupList) {
            flowGroupVo.setFlowGroupQuantity(flowGroupList.size());
        }
        if (null != flowList) {
            flowGroupVo.setFlowQuantity(flowList.size());
        }
        return flowGroupVo;
    }

    /**
     * getFlowGroupVoAllById
     *
     * @param flowGroupId
     * @return
     */
    @Override
    public String getFlowGroupVoAllById(String flowGroupId) {
        if (StringUtils.isBlank(flowGroupId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("flowGroupId"));
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }

        FlowGroupVo flowGroupVo = new FlowGroupVo();
        BeanUtils.copyProperties(flowGroupById, flowGroupVo);
        // 取出mxGraphModel，并转为Vo
        MxGraphModelVo mxGraphModelVo = MxGraphModelUtils.mxGraphModelPoToVo(flowGroupById.getMxGraphModel());
        // 取出flowVoList，并转为Vo
        List<FlowVo> flowVoList = FlowUtil.flowListPoToVo(flowGroupById.getFlowList());
        // 取出pathsList，并转为Vo
        List<FlowGroupPathsVo> flowGroupPathsVoList = FlowGroupPathsUtil.flowGroupPathsPoToVo(flowGroupById.getFlowGroupPathsList());
        flowGroupVo.setMxGraphModelVo(mxGraphModelVo);
        flowGroupVo.setFlowVoList(flowVoList);
        flowGroupVo.setFlowGroupPathsVoList(flowGroupPathsVoList);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowGroupVo", flowGroupVo);

    }

    /**
     * Paging query flowMxGraphModelVo
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    @Override
    public String getFlowGroupListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<FlowGroupVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        flowGroupDomain.getFlowGroupListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String saveOrUpdate(String username, FlowGroupInfoVoRequest flowGroupVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == flowGroupVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String id = flowGroupVo.getId();
        if (StringUtils.isBlank(id)) {
            return this.insert(flowGroupVo, username);
        } else {
            return this.update(flowGroupVo, username);
        }
    }

    private String insert(FlowGroupInfoVoRequest flowGroupVo, String username) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == flowGroupVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (StringUtils.isBlank(flowGroupVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("name"));
        }
        String flowGroupName = flowGroupDomain.getFlowGroupName(flowGroupVo.getName());
        if (StringUtils.isNotBlank(flowGroupName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DUPLICATE_NAME_MSG(flowGroupName));
        }
        FlowGroup flowGroup = new FlowGroup();

        BeanUtils.copyProperties(flowGroupVo, flowGroup);
        flowGroup.setCrtDttm(new Date());
        flowGroup.setCrtUser(username);
        flowGroup.setLastUpdateDttm(new Date());
        flowGroup.setLastUpdateUser(username);
        flowGroup.setEnableFlag(true);

        MxGraphModel mxGraphModel = new MxGraphModel();
        mxGraphModel.setFlowGroup(flowGroup);
        mxGraphModel.setId(UUIDUtils.getUUID32());
        mxGraphModel.setCrtDttm(new Date());
        mxGraphModel.setCrtUser(username);
        mxGraphModel.setLastUpdateDttm(new Date());
        mxGraphModel.setLastUpdateUser(username);
        mxGraphModel.setEnableFlag(true);

        flowGroup.setMxGraphModel(mxGraphModel);
        flowGroupDomain.addFlowGroup(flowGroup);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowGroupId", flowGroup.getId());
    }

    private String update(FlowGroupInfoVoRequest flowGroupVo, String username) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == flowGroupVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String id = flowGroupVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(id);
        if (null == flowGroup) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(id));
        }
        flowGroup.setName(flowGroupVo.getName());
        flowGroup.setDescription(flowGroupVo.getDescription());
        flowGroup.setLastUpdateDttm(new Date());
        flowGroup.setLastUpdateUser(username);
        flowGroupDomain.updateFlowGroup(flowGroup);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowGroupId", flowGroup.getId());
    }

    /**
     * run flow group
     *
     * @param flowGroupId
     * @param runMode
     * @return
     * @throws Exception
     */
    @Override
    public String runFlowGroup(boolean isAdmin, String username, String flowGroupId, String runMode) throws Exception {
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(flowGroupId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("flowGroupId"));
        }
        // find flow by flowId
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(flowGroupId));
        }
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        // ProcessGroup processGroup = flowGroupToProcessGroup(flowGroupById, username, runModeType);
        ProcessGroup processGroup = ProcessGroupUtils.flowGroupToProcessGroup(flowGroupById, username, runModeType, false);
        processGroup.setId(UUIDUtils.getUUID32());
        processGroupDomain.addProcessGroup(processGroup);
        String processGroupId = processGroup.getId();
        processGroup = processGroupDomain.getProcessGroupById(username, isAdmin, processGroupId);
        Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(processGroup, runModeType);

        if (200 == ((Integer) stringObjectMap.get("code"))) {
            processGroup.setLastUpdateDttm(new Date());
            processGroup.setLastUpdateUser(username);
            processGroup.setAppId((String) stringObjectMap.get("appId"));
            processGroup.setProcessId((String) stringObjectMap.get("appId"));
            processGroup.setState(ProcessState.STARTED);
            processGroup.setProcessParentType(ProcessParentType.GROUP);
            processGroupDomain.updateProcessGroup(processGroup);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processGroupId", processGroup.getId());
        } else {
            processGroupDomain.updateEnableFlagById(processGroupId, username);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(stringObjectMap.get("errorMsg").toString());
        }
    }

    @Override
    public String deleteFLowGroupInfo(boolean isAdmin, String username, String id) {
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
        flowGroupDomain.updateEnableFlagById(username, id, false);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
    }

    /**
     * Copy flow to group
     *
     * @param flowId
     * @param flowGroupId
     * @return
     * @throws Exception
     */
    @Override
    public String copyFlowToGroup(String username, String flowId, String flowGroupId) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(flowGroupId) || StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Flow flow = flowDomain.getFlowById(flowId);
        if (null == flow) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Flow flowNew = FlowUtil.copyCreateFlow(flow, username);
        if (null == flowNew) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Save failed, Copy failed");
        }
        flowNew.setFlowGlobalParamsList(flow.getFlowGlobalParamsList());
        MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
        if (null == mxGraphModel) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Save failed, MxGraphModel is empty");
        }
        List<MxCell> root = mxGraphModel.getRoot();
        if (null == root) {
            root = new ArrayList<>();
        }
        if (root.size() <= 0) {
            root.addAll(MxCellUtils.initMxCell(username, mxGraphModel));

        }
        // Get the maximum value of pageid in stop
        Integer maxFlowPageId = flowDomain.getMaxFlowPageIdByFlowGroupId(flowGroupId);
        maxFlowPageId = (null != maxFlowPageId) ? maxFlowPageId : 1;
        // TODO get max group_path_pageID in group
        Integer maxFlowGroupPageId = flowGroupDomain.getMaxFlowGroupPageIdByFlowGroupId(flowGroupId);
        maxFlowGroupPageId = (null != maxFlowGroupPageId) ? maxFlowGroupPageId : 1;
        // TODO: get max group_pageID in group
        Integer maxFlowGroupPathPageId = flowGroupDomain.getMaxFlowGroupPathPageIdByFlowGroupId(flowGroupId);
        maxFlowGroupPathPageId = (null != maxFlowGroupPathPageId) ? maxFlowGroupPathPageId : 1;

        int maxPageId = Math.max(Math.max(maxFlowPageId, maxFlowGroupPageId), maxFlowGroupPathPageId);
        // maxStopPageIdByFlowGroupId = StringUtils.isNotBlank(maxStopPageIdByFlowGroupId) ? maxStopPageIdByFlowGroupId : "1";


        flowNew.setPageId((maxPageId + 1) + "");
        flowNew.setName(flowNew.getName() + (maxPageId + 1));

        MxCell mxCell = new MxCell();
        mxCell.setMxGraphModel(mxGraphModel);
        mxCell.setCrtDttm(new Date());
        mxCell.setCrtUser(username);
        mxCell.setLastUpdateDttm(new Date());
        mxCell.setLastUpdateUser(username);
        mxCell.setPageId((maxPageId + 1) + "");
        mxCell.setParent("1");
        mxCell.setStyle("image;html=1;labelBackgroundColor=#ffffff00;image=/piflow-web/img/flow.png");
        mxCell.setValue(flowNew.getName());
        mxCell.setVertex("1");

        MxGeometry mxGeometry = new MxGeometry();
        mxGeometry.setMxCell(mxCell);
        mxGeometry.setCrtDttm(new Date());
        mxGeometry.setCrtUser(username);
        mxGeometry.setLastUpdateDttm(new Date());
        mxGeometry.setLastUpdateUser(username);
        mxGeometry.setAs("geometry");
        mxGeometry.setHeight("66");
        mxGeometry.setWidth("66");
        mxGeometry.setX("0");
        mxGeometry.setY("0");

        mxCell.setMxGeometry(mxGeometry);
        root.add(mxCell);
        mxGraphModel.setRoot(root);
        flowGroupById.setMxGraphModel(mxGraphModel);

        List<Flow> flowList = flowGroupById.getFlowList();
        if (null == flowList) {
            flowList = new ArrayList<>();
        }
        // flowNew = flowJpa.saveOrUpdate(flowNew);
        flowNew.setFlowGroup(flowGroupById);
        flowList.add(flowNew);
        flowGroupById.setFlowList(flowList);
        flowGroupDomain.updateFlowGroup(flowGroupById);
        flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        MxGraphModel mxGraphModelNew = flowGroupById.getMxGraphModel();
        // Change the query'mxGraphModelNew'to'XML'
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModelNew);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("xmlStr", loadXml);
    }

    @Override
    public String updateFlowGroupNameById(String username, String id, String parentsId, String flowGroupName, String pageId) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isAnyEmpty(id, flowGroupName, parentsId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameter is empty");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(parentsId);
        // FlowGroup parentsFlowGroup = flowGroupJpa.getFlowGroupById(parentsId);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("ParentsFlowGroup query is null,parentsId:" + parentsId);
        }
        MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
        if (null == mxGraphModel) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No flow information,update failed. ParentsFlowGroup Id:" + flowGroupById.getId());
        }
        List<MxCell> root = mxGraphModel.getRoot();
        if (null == root || root.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No flow information,update failed. ParentsFlowGroup Id:" + flowGroupById.getId());
        }
        // Check if name is the same name
        String checkResult = flowGroupDomain.getFlowIdByNameAndFlowGroupId(parentsId, flowGroupName);
        if (StringUtils.isNotBlank(checkResult)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The name '" + flowGroupName + "' has been repeated and the save failed.");
        }
        boolean updateFlowNameById = this.updateFlowGroupNameById(username, id, flowGroupName);
        if (!updateFlowNameById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
        String loadXml = null;
        for (MxCell mxCell : root) {
            if (null == mxCell) {
                continue;
            }
            if (!mxCell.getPageId().equals(pageId)) {
                continue;
            }
            mxCell.setValue(flowGroupName);
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
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("nameContent", flowGroupName);
        return ReturnMapUtils.appendValuesToJson(rtnMap, "XmlData", loadXml);
    }

    @Override
    public Boolean updateFlowGroupNameById(String username, String id, String flowGroupName) throws Exception {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        if (StringUtils.isBlank(id) || StringUtils.isBlank(flowGroupName)) {
            return false;
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(id);
        if (null == flowGroupById) {
            return false;
        }
        flowGroupById.setLastUpdateUser(username);
        flowGroupById.setLastUpdateDttm(new Date());
        flowGroupById.setName(flowGroupName);
        flowGroupDomain.updateFlowGroup(flowGroupById);
        return true;
    }

    @Override
    public String updateFlowGroupBaseInfo(String username, String fId, FlowGroupInfoVoRequestUpDate flowGroupVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == flowGroupVo || StringUtils.isBlank(flowGroupVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed error");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupVo.getId());
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Database save failed");
        }
        // find name in FlowGroup

        String[] flowGroupsInGroup = flowGroupDomain.getFlowGroupNameByNameInGroup(fId, flowGroupVo.getName());
        if (flowGroupsInGroup.length > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DUPLICATE_NAME_MSG(flowGroupVo.getName()));
        }

        List<String> flowNamesInGroup = flowDomain.getFlowNamesByFlowGroupId(fId, flowGroupVo.getName());
        if (flowNamesInGroup.size() > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DUPLICATE_NAME_MSG(flowGroupVo.getName()));
        }

        flowGroupById.setName(flowGroupVo.getName());
        flowGroupById.setDescription(flowGroupVo.getDescription());
        flowGroupById.setLastUpdateDttm(new Date());
        flowGroupById.setLastUpdateUser(username);
        flowGroupDomain.updateFlowGroup(flowGroupById);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("flowGroupVo", flowGroupVo);
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(fId);
        if (null == flowGroup) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        if (null == mxGraphModel) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        MxCell meCellByMxGraphIdAndPageId = mxCellDomain.getMxCellByMxGraphIdAndPageId(mxGraphModel.getId(), flowGroupVo.getPageId());
        if (null == meCellByMxGraphIdAndPageId) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        meCellByMxGraphIdAndPageId.setValue(flowGroupVo.getName());
        int i = mxCellDomain.updateMxCell(meCellByMxGraphIdAndPageId);
        if (i <= 0) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        MxGraphModel mxGraphModelById = mxGraphModelDomain.getMxGraphModelById(mxGraphModel.getId());
        // Convert the mxGraphModelById from the query to XML
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModelById);
        loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
        rtnMap.put("XmlData", loadXml);
        return ReturnMapUtils.toJson(rtnMap);
    }

    @Override
    public String rightRun(String username, boolean isAdmin, String pId, String nodeId, String nodeType) throws Exception {
        if (StringUtils.isBlank(pId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("pId"));
        }
        if (StringUtils.isBlank(nodeId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("nodeId"));
        }
        String flowGroupId = null;
        String flowId = null;
        if (StringUtils.isBlank(nodeType)) {
            flowId = flowDomain.getFlowIdByPageId(pId, nodeId);
            flowGroupId = flowGroupDomain.getFlowGroupIdByPageId(pId, nodeId);
        } else if ("TASK".equals(nodeType)) {
            flowId = flowDomain.getFlowIdByPageId(pId, nodeId);
        } else if ("GROUP".equals(nodeType)) {
            flowGroupId = flowGroupDomain.getFlowGroupIdByPageId(pId, nodeId);
        }
        if (StringUtils.isNotBlank(flowId)) {
            return flowServiceImpl.runFlow(username, isAdmin, flowId, null);
        } else if (StringUtils.isNotBlank(flowGroupId)) {
            return runFlowGroup(isAdmin, username, flowGroupId, null);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data found for this node (" + nodeId + ")");
        }
    }

    /**
     * Query FlowGroupVo or FlowVo information based on pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    @Override
    public String queryIdInfo(String fid, String pageId) {
        if (StringUtils.isBlank(fid) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        FlowVo flowVo = flowServiceImpl.getFlowByPageId(fid, pageId);
        if (null != flowVo) {
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("nodeType", "flow");
            return ReturnMapUtils.appendValuesToJson(rtnMap, "flowVo", flowVo);
        }
        FlowGroupVo flowGroupVo = this.getFlowGroupByPageId(fid, pageId);
        if (null != flowGroupVo) {
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("nodeType", "flowGroup");
            return ReturnMapUtils.appendValuesToJson(rtnMap, "flowGroupVo", flowGroupVo);
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
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
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(load);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(load));
        }

        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());

        rtnMap.put("parentAccessPath", parentAccessPath);

        if (null != flowGroupById.getFlowGroup()) {
            String parentsId = flowGroupById.getFlowGroup().getId();
            rtnMap.put("parentsId", parentsId);
        }
        // set drawingBoardType
        rtnMap.put("drawingBoardType", "GROUP");
        rtnMap.put("mxGraphComponentList", MxGraphComponentVoUtils.InitDefaultGroupMxGraphComponentList());

        MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
        // Change the query'mxGraphModel'to'XML'
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
        rtnMap.put("xmlDate", loadXml);
        rtnMap.put("load", load);
        rtnMap.put("isExample", (null == flowGroupById.getIsExample() ? false : flowGroupById.getIsExample()));

        return ReturnMapUtils.toJson(rtnMap);
    }

}
