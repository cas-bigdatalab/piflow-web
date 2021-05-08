package cn.cnic.component.flow.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.ProcessParentType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.mapper.FlowGroupPathsMapper;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.utils.FlowGroupPathsUtil;
import cn.cnic.component.flow.utils.FlowUtil;
import cn.cnic.component.flow.vo.FlowGroupPathsVo;
import cn.cnic.component.flow.vo.FlowGroupVo;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.mxGraph.entity.MxCell;

import cn.cnic.component.mxGraph.entity.MxGeometry;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.utils.MxGraphComponentVoUtils;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.flow.jpa.domain.FlowDomain;
import cn.cnic.component.flow.jpa.domain.FlowGroupDomain;
import cn.cnic.component.mxGraph.jpa.domain.MxCellDomain;
import cn.cnic.component.process.jpa.domain.ProcessGroupDomain;
import cn.cnic.component.flow.mapper.FlowGroupMapper;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.mxGraph.mapper.MxCellMapper;
import cn.cnic.component.mxGraph.mapper.MxGraphModelMapper;
import cn.cnic.component.schedule.mapper.ScheduleMapper;
import cn.cnic.third.service.IGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class FlowGroupServiceImpl implements IFlowGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private FlowGroupDomain flowGroupDomain;

    @Autowired
    private FlowMapper flowMapper;

    @Autowired
    private ProcessGroupDomain processGroupDomain;

    @Autowired
    private IGroup groupImpl;

    @Autowired
    private FlowGroupMapper flowGroupMapper;

    @Autowired
    private FlowGroupPathsMapper flowGroupPathsMapper;

    @Autowired
    private MxGraphModelMapper mxGraphModelMapper;

    @Autowired
    private MxCellMapper mxCellMapper;

    @Autowired
    private MxCellDomain mxCellDomain;

    @Autowired
    private FlowDomain flowDomain;

    @Autowired
    private IFlowService flowServiceImpl;

    @Autowired
    private ScheduleMapper scheduleMapper;

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
        //Determine whether there is a flowGroup id (flowGroupId)
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
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        FlowGroupVo flowGroupVo = null;
        FlowGroup flowGroupById = flowGroupMapper.getFlowGroupById(flowGroupId);
        if (null != flowGroupById) {
            flowGroupVo = new FlowGroupVo();
            BeanUtils.copyProperties(flowGroupById, flowGroupVo);
            flowGroupVo.setFlowQuantity((null != flowGroupById.getFlowList() ? flowGroupById.getFlowList().size() : 0));
            flowGroupVo.setFlowGroupQuantity((null != flowGroupById.getFlowGroupList() ? flowGroupById.getFlowGroupList().size() : 0));
        }
        rtnMap.put("code", 200);
        rtnMap.put("flowGroupVo", flowGroupVo);
        return JsonUtils.toJsonNoException(rtnMap);
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
        FlowGroupVo flowGroupVo = null;
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupByPageId(fid, pageId);
        if (null != flowGroup) {
            flowGroupVo = new FlowGroupVo();
            BeanUtils.copyProperties(flowGroup, flowGroupVo);
            List<FlowGroup> flowGroupList = flowGroup.getFlowGroupList();
            List<Flow> flowList = flowGroup.getFlowList();
            if (null != flowGroupList) {
                flowGroupVo.setFlowGroupQuantity(flowGroupList.size());
            }
            if (null != flowList) {
                flowGroupVo.setFlowQuantity(flowList.size());
            }
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
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        FlowGroupVo flowGroupVo = null;
        FlowGroup flowGroupById = flowGroupMapper.getFlowGroupById(flowGroupId);
        if (null != flowGroupById) {
            flowGroupVo = new FlowGroupVo();
            BeanUtils.copyProperties(flowGroupById, flowGroupVo);
            //取出mxGraphModel，并转为Vo
            MxGraphModelVo mxGraphModelVo = MxGraphModelUtils.mxGraphModelPoToVo(flowGroupById.getMxGraphModel());
            //取出flowVoList，并转为Vo
            List<FlowVo> flowVoList = FlowUtil.flowListPoToVo(flowGroupById.getFlowList());
            //取出pathsList，并转为Vo
            List<FlowGroupPathsVo> flowGroupPathsVoList = FlowGroupPathsUtil.flowGroupPathsPoToVo(flowGroupById.getFlowGroupPathsList());
            flowGroupVo.setMxGraphModelVo(mxGraphModelVo);
            flowGroupVo.setFlowVoList(flowVoList);
            flowGroupVo.setFlowGroupPathsVoList(flowGroupPathsVoList);
        }
        rtnMap.put("code", 200);
        rtnMap.put("flowGroupVo", flowGroupVo);
        return JsonUtils.toJsonNoException(rtnMap);
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
        Map<String, Object> rtnMap = new HashMap<>();
        if (null != offset && null != limit) {
            Page<FlowGroup> flowGroupListPage;
            if (isAdmin) {
                flowGroupListPage = flowGroupDomain.adminGetFlowGroupListPage(offset - 1, limit, param);
            } else {
                flowGroupListPage = flowGroupDomain.userGetFlowGroupListPage(offset - 1, limit, param, username);
            }
            List<FlowGroupVo> contentVo = new ArrayList<>();
            List<FlowGroup> content = flowGroupListPage.getContent();
            if (content.size() > 0) {
                for (FlowGroup flowGroup : content) {
                    if (null != flowGroup) {
                        FlowGroupVo flowGroupVo = new FlowGroupVo();
                        BeanUtils.copyProperties(flowGroup, flowGroupVo);
                        contentVo.add(flowGroupVo);
                    }
                }
            }
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
            rtnMap.put("msg", "");
            rtnMap.put("count", flowGroupListPage.getTotalElements());
            rtnMap.put("data", contentVo);//Data collection
            logger.debug("success");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String saveOrUpdate(String username, FlowGroupVo flowGroupVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == flowGroupVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        String id = flowGroupVo.getId();
        if (StringUtils.isBlank(id)) {
            return this.insert(flowGroupVo, username);
        } else {
            return this.update(flowGroupVo, username);
        }
    }

    private String insert(FlowGroupVo flowGroupVo, String username) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == flowGroupVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
        }
        if (StringUtils.isBlank(flowGroupVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("flowGroup is null");
        }
        String flowGroupName = flowGroupMapper.getFlowGroupName(flowGroupVo.getName());
        if (StringUtils.isNotBlank(flowGroupName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Repeat flowGroup name!");
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
        flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);

        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowGroupId", flowGroup.getId());
    }

    private String update(FlowGroupVo flowGroupVo, String username) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isBlank(username)) {
            rtnMap.put("errorMsg", "Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (null == flowGroupVo) {
            rtnMap.put("errorMsg", "Parameter is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String id = flowGroupVo.getId();
        if (StringUtils.isBlank(id)) {
            rtnMap.put("errorMsg", "Id is null");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(id);
        if (null == flowGroup) {
            rtnMap.put("errorMsg", "The current Id does not exist for the flowGroup");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        flowGroup.setName(flowGroupVo.getName());
        flowGroup.setDescription(flowGroupVo.getDescription());
        flowGroup.setLastUpdateDttm(new Date());
        flowGroup.setLastUpdateUser(username);
        flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);
        rtnMap.put("code", 200);
        rtnMap.put("flowGroupId", flowGroup.getId());

        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * run flow group
     *
     * @param flowGroupId
     * @param runMode
     * @return
     */
    @Override
    @Transactional
    public String runFlowGroup(String username, String flowGroupId, String runMode) {
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(flowGroupId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowGroupId is null");
        }
        // find flow by flowId
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow with FlowGroupId" + flowGroupId + "was not queried");
        }
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        //ProcessGroup processGroup = flowGroupToProcessGroup(flowGroupById, username, runModeType);
        ProcessGroup processGroup = ProcessGroupUtils.flowGroupToProcessGroup(flowGroupById, username, runModeType, false);
        processGroup = processGroupDomain.saveOrUpdate(username, processGroup);

        Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(processGroup, runModeType);
        processGroup.setLastUpdateDttm(new Date());
        processGroup.setLastUpdateUser(username);
        if (200 == ((Integer) stringObjectMap.get("code"))) {
            processGroup.setAppId((String) stringObjectMap.get("appId"));
            processGroup.setProcessId((String) stringObjectMap.get("appId"));
            processGroup.setState(ProcessState.STARTED);
            processGroup.setProcessParentType(ProcessParentType.GROUP);
            processGroupDomain.saveOrUpdate(username, processGroup);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processGroupId", processGroup.getId());
        } else {
            processGroup.setEnableFlag(false);
            processGroupDomain.saveOrUpdate(username, processGroup);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(stringObjectMap.get("errorMsg").toString());
        }
    }

    @Override
    public String deleteFLowGroupInfo(boolean isAdmin, String username, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal user");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        int scheduleIdListByScheduleRunTemplateId = scheduleMapper.getScheduleIdListByScheduleRunTemplateId(isAdmin, username, id);
        if (scheduleIdListByScheduleRunTemplateId > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Unable to delete, there is an associated scheduled task");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(id);
        flowGroupById.setLastUpdateDttm(new Date());
        flowGroupById.setLastUpdateUser(username);
        flowGroupById.setEnableFlag(false);
        flowGroupDomain.updateEnableFlagById(id, false);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("delete succeed");
    }

    /**
     * Copy flow to group
     *
     * @param flowId
     * @param flowGroupId
     * @return
     */
    @Override
    public String copyFlowToGroup(String username, String flowId, String flowGroupId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal user, Load failed");
        }
        if (StringUtils.isBlank(flowGroupId) || StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("flowGroupId or flowId is empty");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Save failed, flowGroup is empty");
        }
        Flow flow = flowMapper.getFlowById(flowId);
        if (null == flow) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Save failed, Flow is empty");
        }
        Flow flowNew = FlowUtil.copyCreateFlow(flow, username);
        if (null == flowNew) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Save failed, Copy failed");
        }
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
        Integer maxFlowPageId = flowMapper.getMaxFlowPageIdByFlowGroupId(flowGroupId);
        maxFlowPageId = (null != maxFlowPageId) ? maxFlowPageId : 1;
        //TODO get max group_path_pageID in group
        Integer maxFlowGroupPageId = flowGroupMapper.getMaxFlowGroupPageIdByFlowGroupId(flowGroupId);
        maxFlowGroupPageId = (null != maxFlowGroupPageId) ? maxFlowGroupPageId : 1;
        //TODO: get max group_pageID in group
        Integer maxFlowGroupPathPageId = flowGroupPathsMapper.getMaxFlowGroupPathPageIdByFlowGroupId(flowGroupId);
        maxFlowGroupPathPageId = (null != maxFlowGroupPathPageId) ? maxFlowGroupPathPageId : 1;

        int maxPageId = Math.max(Math.max(maxFlowPageId, maxFlowGroupPageId), maxFlowGroupPathPageId);
        //maxStopPageIdByFlowGroupId = StringUtils.isNotBlank(maxStopPageIdByFlowGroupId) ? maxStopPageIdByFlowGroupId : "1";


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
        //flowNew = flowDomain.saveOrUpdate(flowNew);
        flowNew.setFlowGroup(flowGroupById);
        flowList.add(flowNew);
        flowGroupById.setFlowList(flowList);
        flowGroupById = flowGroupDomain.saveOrUpdate(flowGroupById);
        MxGraphModel mxGraphModelNew = flowGroupById.getMxGraphModel();
        // Change the query'mxGraphModelNew'to'XML'
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModelNew);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("success");
        rtnMap.put("xmlStr", loadXml);
        return JsonUtils.toFormatJsonNoException(rtnMap);
    }

    @Override
    public String updateFlowGroupNameById(String username, String id, String parentsId, String flowGroupName, String pageId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isAnyEmpty(id, flowGroupName, parentsId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameter is empty");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(parentsId);
        //FlowGroup parentsFlowGroup = flowGroupDomain.getFlowGroupById(parentsId);
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
        //Check if name is the same name
        String checkResult = flowGroupDomain.getFlowIdByNameAndFlowGroupId(parentsId, flowGroupName);
        if (StringUtils.isNotBlank(checkResult)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The name '" + flowGroupName + "' has been repeated and the save failed.");
        }
        boolean updateFlowNameById = this.updateFlowGroupNameById(username, id, flowGroupName);
        if (!updateFlowNameById) {
            logger.info("Modify flowName failed");
            rtnMap.put("errorMsg", "Modify flowName failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        for (MxCell mxCell : root) {
            if (null != mxCell) {
                if (mxCell.getPageId().equals(pageId)) {
                    mxCell.setValue(flowGroupName);
                    mxCell.setLastUpdateDttm(new Date());
                    mxCell.setLastUpdateUser(username);
                    mxCellDomain.saveOrUpdate(mxCell);
                    // Convert the mxGraphModel from the query to XML
                    String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
                    loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
                    rtnMap.put("XmlData", loadXml);
                    rtnMap.put("nameContent", flowGroupName);
                    rtnMap.put("code", 200);
                    rtnMap.put("errorMsg", "Successfully modified");
                    logger.info("Successfully modified");
                    rtnMap.put("errorMsg", "Successfully modified");
                    break;
                }
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public Boolean updateFlowGroupNameById(String username, String id, String flowGroupName) {
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
        flowGroupDomain.saveOrUpdate(flowGroupById);
        return true;
    }

    @Override
    @Transactional
    public String updateFlowGroupBaseInfo(String username, String fId, FlowGroupVo flowGroupVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == flowGroupVo || StringUtils.isBlank(flowGroupVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed error");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupVo.getId());
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Database save failed");
        }
        //find name in FlowGroup

        String[] flowGroupsInGroup = flowGroupDomain.getFlowGroupNameByNameInGroup(fId, flowGroupVo.getName());
        if (flowGroupsInGroup.length > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Repeat flow group name!");
        }

        String[] flowNamesInGroup = flowDomain.getFlowNameByFlowGroupId(fId, flowGroupVo.getName());
        if (flowNamesInGroup.length > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Repeat flow group name!");
        }

        flowGroupById.setName(flowGroupVo.getName());
        flowGroupById.setDescription(flowGroupVo.getDescription());
        flowGroupById.setLastUpdateDttm(new Date());
        flowGroupById.setLastUpdateUser(username);
        flowGroupDomain.saveOrUpdate(flowGroupById);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("flowGroupVo", flowGroupVo);
        FlowGroup flowGroup = flowGroupMapper.getFlowGroupById(fId);
        if (null == flowGroup) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        if (null == mxGraphModel) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxCell meCellByMxGraphIdAndPageId = mxCellMapper.getMxCellByMxGraphIdAndPageId(mxGraphModel.getId(), flowGroupVo.getPageId());
        if (null == meCellByMxGraphIdAndPageId) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        meCellByMxGraphIdAndPageId.setValue(flowGroupVo.getName());
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
    public String rightRun(String username, boolean isAdmin, String pId, String nodeId, String nodeType) {
        if (StringUtils.isBlank(pId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("pId is null");
        }
        if (StringUtils.isBlank(nodeId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("nodeId is null");
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
            return runFlowGroup(username, flowGroupId, null);
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
    public String queryIdInfo(String fid, String pageId) {
        if (StringUtils.isBlank(fid) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Missing parameters");
        }
        Map<String, Object> rtnMap = new HashMap<>();
        FlowVo flowVo = flowServiceImpl.getFlowByPageId(fid, pageId);
        if (null != flowVo) {
            rtnMap.put("nodeType", "flow");
            rtnMap.put("flowVo", flowVo);
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroupVo flowGroupVo = this.getFlowGroupByPageId(fid, pageId);
        if (null != flowGroupVo) {
            rtnMap.put("nodeType", "flowGroup");
            rtnMap.put("flowGroupVo", flowGroupVo);
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
            return JsonUtils.toJsonNoException(rtnMap);
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("no Data");
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
        FlowGroup flowGroupById = this.getFlowGroupById(username, isAdmin, load);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID : " + load);
        }

        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);

        rtnMap.put("parentAccessPath", parentAccessPath);

        if (null != flowGroupById.getFlowGroup()) {
            String parentsId = flowGroupById.getFlowGroup().getId();
            rtnMap.put("parentsId", parentsId);
        }
        //set drawingBoardType
        rtnMap.put("drawingBoardType", "GROUP");
        rtnMap.put("mxGraphComponentList", MxGraphComponentVoUtils.InitDefaultGroupMxGraphComponentList());

        MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
        // Change the query'mxGraphModel'to'XML'
        String loadXml = MxGraphUtils.mxGraphModelToMxGraph(false, mxGraphModel);
        rtnMap.put("xmlDate", loadXml);
        rtnMap.put("load", load);
        rtnMap.put("isExample", (null == flowGroupById.getIsExample() ? false : flowGroupById.getIsExample()));

        return JsonUtils.toJsonNoException(rtnMap);
    }

}
