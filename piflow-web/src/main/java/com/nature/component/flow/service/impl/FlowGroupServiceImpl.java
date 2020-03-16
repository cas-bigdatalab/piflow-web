package com.nature.component.flow.service.impl;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessParentType;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.*;
import com.nature.component.flow.service.IFlowGroupService;
import com.nature.component.flow.utils.FlowGroupPathsUtil;
import com.nature.component.flow.utils.FlowUtil;
import com.nature.component.mxGraph.utils.MxCellUtils;
import com.nature.component.mxGraph.utils.MxGraphModelUtil;
import com.nature.component.flow.vo.FlowGroupPathsVo;
import com.nature.component.flow.vo.FlowGroupVo;
import com.nature.component.flow.vo.FlowVo;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.*;
import com.nature.component.process.utils.ProcessGroupUtils;
import com.nature.domain.flow.FlowGroupDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.process.*;
import com.nature.mapper.flow.FlowGroupMapper;
import com.nature.mapper.flow.FlowMapper;
import com.nature.third.service.IGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class FlowGroupServiceImpl implements IFlowGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowGroupDomain flowGroupDomain;

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private ProcessGroupDomain processGroupDomain;

    @Resource
    private ProcessDomain processDomain;

    @Resource
    private ProcessPathDomain processPathDomain;

    @Resource
    private ProcessGroupPathDomain processGroupPathDomain;

    @Resource
    private ProcessStopDomain processStopDomain;

    @Resource
    private ProcessStopPropertyDomain processStopPropertyDomain;

    @Resource
    private ProcessStopCustomizedPropertyDomain processStopCustomizedPropertyDomain;

    @Resource
    private IGroup groupImpl;

    @Resource
    private FlowGroupMapper flowGroupMapper;

    @Resource
    private MxCellDomain mxCellDomain;


    /**
     * group Drawing Board
     *
     * @param flowGroupId
     * @return
     */
    @Override
    public FlowGroup getFlowGroupById(String flowGroupId) {
        FlowGroup flowGroup = null;
        //Determine whether there is a flowGroup id (flowGroupId)
        if (StringUtils.isNotBlank(flowGroupId)) {
            flowGroup = flowGroupMapper.getFlowGroupById(flowGroupId);
        }
        return flowGroup;
    }

    /**
     * getFlowGroupVoById
     *
     * @param flowGroupId
     * @return
     */
    @Override
    public String getFlowGroupVoById(String flowGroupId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        FlowGroupVo flowGroupVo = null;
        FlowGroup flowGroupById = flowGroupMapper.getFlowGroupById(flowGroupId);
        if (null != flowGroupById) {
            flowGroupVo = new FlowGroupVo();
            BeanUtils.copyProperties(flowGroupById, flowGroupVo);
            List<FlowVo> flowVoList = FlowUtil.flowListPoToVo(flowGroupById.getFlowList());
            flowGroupVo.setFlowVoList(flowVoList);
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
        FlowGroup flowGroup = flowGroupDomain.getFlowByPageId(fid, pageId);
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
            MxGraphModelVo mxGraphModelVo = MxGraphModelUtil.mxGraphModelPoToVo(flowGroupById.getMxGraphModel());
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
    public String getFlowGroupListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<>();
        if (null != offset && null != limit) {
            Page<FlowGroup> flowGroupListPage = flowGroupDomain.getFlowGroupListPage(offset - 1, limit, param);
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
            rtnMap.put("iTotalDisplayRecords", flowGroupListPage.getTotalElements());
            rtnMap.put("iTotalRecords", flowGroupListPage.getTotalElements());
            rtnMap.put("pageData", contentVo);//Data collection
            logger.debug("success");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String saveOrUpdate(FlowGroupVo flowGroupVo) {
        if (null != flowGroupVo) {
            String id = flowGroupVo.getId();
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            if (StringUtils.isBlank(id)) {
                return this.insert(flowGroupVo, currentUser.getUsername());
            } else {
                return this.update(flowGroupVo, currentUser.getUsername());
            }
        } else {
            return null;
        }
    }

    private String insert(FlowGroupVo flowGroupVo, String username) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(username)) {
            if (null != flowGroupVo) {

                FlowGroup flowGroup = new FlowGroup();

                BeanUtils.copyProperties(flowGroupVo, flowGroup);
                flowGroup.setCrtDttm(new Date());
                flowGroup.setCrtUser(username);
                flowGroup.setLastUpdateDttm(new Date());
                flowGroup.setLastUpdateUser(username);
                flowGroup.setEnableFlag(true);

                MxGraphModel mxGraphModel = new MxGraphModel();
                mxGraphModel.setFlowGroup(flowGroup);
                mxGraphModel.setId(SqlUtils.getUUID32());
                mxGraphModel.setCrtDttm(new Date());
                mxGraphModel.setCrtUser(username);
                mxGraphModel.setLastUpdateDttm(new Date());
                mxGraphModel.setLastUpdateUser(username);
                mxGraphModel.setEnableFlag(true);

                flowGroup.setMxGraphModel(mxGraphModel);
                flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);
                rtnMap.put("code", 200);
                rtnMap.put("flowGroupId", flowGroup.getId());
            }
        } else {
            rtnMap.put("errorMsg", "Illegal users");
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
    public String runFlowGroup(String flowGroupId, String runMode) {
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isBlank(flowGroupId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowGroupId is null");
        }
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        String username = currentUser.getUsername();
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
        ProcessGroup processGroup = ProcessGroupUtils.flowGroupToProcessGroup(flowGroupById, username, runModeType);
        processGroup = processGroupDomain.saveOrUpdate(processGroup);

        Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(processGroup, runModeType);

        if (200 == ((Integer) stringObjectMap.get("code"))) {
            ProcessGroup processGroupById = processGroupDomain.getProcessGroupById(processGroup.getId());
            processGroupById.setAppId((String) stringObjectMap.get("appId"));
            processGroupById.setProcessId((String) stringObjectMap.get("appId"));
            processGroupById.setState(ProcessState.STARTED);
            processGroupById.setProcessParentType(ProcessParentType.GROUP);
            processGroupDomain.saveOrUpdate(processGroupById);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processGroupId", processGroup.getId());
        } else {
            processGroupDomain.updateEnableFlagById(processGroup.getId(), false);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(stringObjectMap.get("errorMsg").toString());
        }
    }

    private ProcessGroup flowGroupToProcessGroup(FlowGroup flowGroup, String username, RunModeType runModeType) {
        ProcessGroup processGroup = new ProcessGroup();
        // copy flowGroup信息到processGroup
        BeanUtils.copyProperties(flowGroup, processGroup);
        // set base info
        processGroup.setId(SqlUtils.getUUID32());
        processGroup.setCrtDttm(new Date());
        processGroup.setCrtUser(username);
        processGroup.setLastUpdateDttm(new Date());
        processGroup.setLastUpdateUser(username);
        processGroup.setEnableFlag(true);
        // Take out the sketchpad information of 'flowgroup'
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        // The 'flowGroup' palette information changes to 'viewXml'
        String viewXml = SvgUtils.mxGraphModelToViewXml(mxGraphModel, true, false);
        // set viewXml
        processGroup.setViewXml(viewXml);
        // set flowGroupId
        processGroup.setFlowId(flowGroup.getId());

        processGroup.setRunModeType(runModeType);
        processGroup.setProcessParentType(ProcessParentType.GROUP);
        processGroup = processGroupDomain.saveOrUpdate(processGroup);

        // Get the paths information of flow
        List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
        // isEmpty
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            List<ProcessGroupPath> processGroupPathList = new ArrayList<>();
            // Loop paths information
            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                // isEmpty
                if (null != flowGroupPaths) {
                    ProcessGroupPath processGroupPath = new ProcessGroupPath();
                    // Copy flowGroupPaths information into processGroupPath
                    BeanUtils.copyProperties(flowGroupPaths, processGroupPath);
                    // Set basic information
                    processGroupPath.setId(SqlUtils.getUUID32());
                    processGroupPath.setCrtDttm(new Date());
                    processGroupPath.setCrtUser(username);
                    processGroupPath.setLastUpdateDttm(new Date());
                    processGroupPath.setLastUpdateUser(username);
                    processGroupPath.setEnableFlag(true);
                    // Associated foreign key
                    processGroupPath.setProcessGroup(processGroup);
                    processGroupPathList.add(processGroupPath);
                }
            }
            processGroupPathList = processGroupPathDomain.saveOrUpdate(processGroupPathList);
            processGroup.setProcessGroupPathList(processGroupPathList);
        }

        // flow to remove flowGroup
        List<Flow> flowList = flowGroup.getFlowList();
        // flowList isEmpty
        if (null != flowList && flowList.size() > 0) {
            // List of stop of process
            List<Process> processList = new ArrayList<>();
            // Loop flowList
            for (Flow flow : flowList) {
                // isEmpty
                if (null == flow) {
                    continue;
                }

                Process process = new Process();
                // copy flow to process
                BeanUtils.copyProperties(flow, process);
                // set base info
                process.setId(SqlUtils.getUUID32());
                process.setCrtDttm(new Date());
                process.setCrtUser(username);
                process.setLastUpdateDttm(new Date());
                process.setLastUpdateUser(username);
                process.setEnableFlag(true);
                // Take out flow's Sketchpad information
                MxGraphModel flowMxGraphModel = flow.getMxGraphModel();
                // Flow Sketchpad information to viewxml
                String processViewXml = SvgUtils.mxGraphModelToViewXml(flowMxGraphModel, false, false);
                // set viewXml
                process.setViewXml(processViewXml);
                // set flowId
                process.setFlowId(flow.getId());
                process.setProcessParentType(ProcessParentType.GROUP);
                process.setRunModeType(runModeType);
                process = processDomain.saveOrUpdate(process);
                // Stops to remove flow
                List<Stops> stopsList = flow.getStopsList();
                // stopsList isEmpty
                if (null != stopsList && stopsList.size() > 0) {
                    // List of stop of process
                    List<ProcessStop> processStopList = new ArrayList<>();
                    // Loop stopsList
                    for (Stops stops : stopsList) {
                        // isEmpty
                        if (null != stops) {
                            ProcessStop processStop = new ProcessStop();
                            // copy stops的信息到processStop中
                            BeanUtils.copyProperties(stops, processStop);
                            // set base info
                            processStop.setId(SqlUtils.getUUID32());
                            processStop.setCrtDttm(new Date());
                            processStop.setCrtUser(username);
                            processStop.setLastUpdateDttm(new Date());
                            processStop.setLastUpdateUser(username);
                            processStop.setEnableFlag(true);
                            // link foreign key
                            processStop.setProcess(process);
                            processStop = processStopDomain.saveOrUpdate(processStop);
                            // Take out the stops attribute
                            List<Property> properties = stops.getProperties();
                            // Empty attribute of stops
                            if (null != properties && properties.size() > 0) {
                                List<ProcessStopProperty> processStopPropertyList = new ArrayList<>();
                                // Attributes of loop stops
                                for (Property property : properties) {
                                    // isEmpty
                                    if (null != property) {
                                        ProcessStopProperty processStopProperty = new ProcessStopProperty();
                                        // Copy property information into processStopProperty
                                        BeanUtils.copyProperties(property, processStopProperty);
                                        // Set basic information
                                        processStopProperty.setId(SqlUtils.getUUID32());
                                        processStopProperty.setCrtDttm(new Date());
                                        processStopProperty.setCrtUser(username);
                                        processStopProperty.setLastUpdateDttm(new Date());
                                        processStopProperty.setLastUpdateUser(username);
                                        processStopProperty.setEnableFlag(true);
                                        // Associated foreign key
                                        processStopProperty.setProcessStop(processStop);
                                        processStopPropertyList.add(processStopProperty);
                                    }
                                }
                                List<ProcessStopProperty> processStopProperties = processStopPropertyDomain.saveOrUpdate(processStopPropertyList);
                                processStop.setProcessStopPropertyList(processStopProperties);
                            }

                            // 取出stops的自定义属性
                            List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
                            // stops的属性判空
                            if (null != customizedPropertyList && customizedPropertyList.size() > 0) {
                                List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = new ArrayList<>();
                                // Attributes of loop stops
                                for (CustomizedProperty customizedProperty : customizedPropertyList) {
                                    // isEmpty
                                    if (null != customizedProperty) {
                                        ProcessStopCustomizedProperty processStopCustomizedProperty = new ProcessStopCustomizedProperty();
                                        // Copy customizedProperty information into processStopCustomizedProperty
                                        BeanUtils.copyProperties(customizedProperty, processStopCustomizedProperty);
                                        // Set basic information
                                        processStopCustomizedProperty.setId(SqlUtils.getUUID32());
                                        processStopCustomizedProperty.setCrtDttm(new Date());
                                        processStopCustomizedProperty.setCrtUser(username);
                                        processStopCustomizedProperty.setLastUpdateDttm(new Date());
                                        processStopCustomizedProperty.setLastUpdateUser(username);
                                        processStopCustomizedProperty.setEnableFlag(true);
                                        // Associated foreign key
                                        processStopCustomizedProperty.setProcessStop(processStop);
                                        processStopCustomizedPropertyList.add(processStopCustomizedProperty);
                                    }
                                }
                                processStopCustomizedPropertyList = processStopCustomizedPropertyDomain.saveOrUpdate(processStopCustomizedPropertyList);
                                processStop.setProcessStopCustomizedPropertyList(processStopCustomizedPropertyList);
                            }
                            processStopList.add(processStop);
                        }
                    }
                    process.setProcessStopList(processStopList);
                }
                // Get the paths information of flow
                List<Paths> pathsList = flow.getPathsList();
                // isEmpty
                if (null != pathsList && pathsList.size() > 0) {
                    List<ProcessPath> processPathList = new ArrayList<>();
                    // Loop paths information
                    for (Paths paths : pathsList) {
                        // isEmpty
                        if (null != paths) {
                            ProcessPath processPath = new ProcessPath();
                            // Copy paths information into processPath
                            BeanUtils.copyProperties(paths, processPath);
                            // Set basic information
                            processPath.setId(SqlUtils.getUUID32());
                            processPath.setCrtDttm(new Date());
                            processPath.setCrtUser(username);
                            processPath.setLastUpdateDttm(new Date());
                            processPath.setLastUpdateUser(username);
                            processPath.setEnableFlag(true);
                            // Associated foreign key
                            processPath.setProcess(process);
                            processPathList.add(processPath);
                        }
                    }
                    processPathDomain.saveOrUpdate(processPathList);
                    process.setProcessPathList(processPathList);
                }
                process.setProcessGroup(processGroup);
                processList.add(process);
            }
            processGroup.setProcessList(processList);
        }
        return processGroup;
    }

    @Override
    public int deleteFLowGroupInfo(String id) {
        int deleteFLowInfo = 0;
        if (StringUtils.isNotBlank(id)) {
            deleteFLowInfo = flowGroupDomain.updateEnableFlagById(id, false);
        }
        return deleteFLowInfo;
    }

    /**
     * Copy flow to group
     *
     * @param flowId
     * @param flowGroupId
     * @return
     */
    @Override
    public String copyFlowToGroup(String flowId, String flowGroupId) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
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
        Flow flowNew = FlowUtil.copyCreateFlow(flow, currentUser.getUsername());
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
            root.addAll(MxCellUtils.initMxCell(currentUser.getUsername(),mxGraphModel));

        }
        // Get the maximum value of pageid in stop
        String maxStopPageIdByFlowGroupId = flowMapper.getMaxFlowPageIdByFlowGroupId(flowGroupId);
        maxStopPageIdByFlowGroupId = StringUtils.isNotBlank(maxStopPageIdByFlowGroupId) ? maxStopPageIdByFlowGroupId : "1";
        int maxPageId = Integer.parseInt(maxStopPageIdByFlowGroupId);

        flowNew.setPageId((maxPageId + 1) + "");
        flowNew.setName(flowNew.getName() + (maxPageId + 1));

        MxCell mxCell = new MxCell();
        mxCell.setMxGraphModel(mxGraphModel);
        mxCell.setCrtDttm(new Date());
        mxCell.setCrtUser(currentUser.getUsername());
        mxCell.setLastUpdateDttm(new Date());
        mxCell.setLastUpdateUser(currentUser.getUsername());
        mxCell.setPageId((maxPageId + 1) + "");
        mxCell.setParent("1");
        mxCell.setStyle("image;html=1;labelBackgroundColor=#ffffff00;image=/piflow-web/img/flow_02_128x128.png");
        mxCell.setValue(flowNew.getName());
        mxCell.setVertex("1");

        MxGeometry mxGeometry = new MxGeometry();
        mxGeometry.setMxCell(mxCell);
        mxGeometry.setCrtDttm(new Date());
        mxGeometry.setCrtUser(currentUser.getUsername());
        mxGeometry.setLastUpdateDttm(new Date());
        mxGeometry.setLastUpdateUser(currentUser.getUsername());
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
        MxGraphModelVo mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModelNew);
        // Change the query'mxGraphModelVo'to'XML'
        String loadXml = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("success");
        rtnMap.put("xmlStr", loadXml);
        return JsonUtils.toFormatJsonNoException(rtnMap);
    }

    @Override
    public String updateFlowGroupNameById(String id, String parentsId, String flowGroupName, String pageId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        UserVo user = SessionUserUtil.getCurrentUser();
        if (null == user) {
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
        boolean updateFlowNameById = this.updateFlowGroupNameById(id, flowGroupName);
        if (!updateFlowNameById) {
            logger.info("Modify flowName failed");
            rtnMap.put("errorMsg", "Modify flowName failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String username = user.getUsername();
        for (MxCell mxCell : root) {
            if (null != mxCell) {
                if (mxCell.getPageId().equals(pageId)) {
                    mxCell.setValue(flowGroupName);
                    mxCell.setLastUpdateDttm(new Date());
                    mxCell.setLastUpdateUser(username);
                    mxCellDomain.saveOrUpdate(mxCell);
                    MxGraphModelVo mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
                    // Convert the mxGraphModelVo from the query to XML
                    String loadXml = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
                    loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
                    rtnMap.put("XmlData", loadXml);
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
    public Boolean updateFlowGroupNameById(String id, String flowGroupName) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(flowGroupName)) {
            FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(id);
            if (null != flowGroupById) {
                flowGroupById.setLastUpdateUser(username);
                flowGroupById.setLastUpdateDttm(new Date());
                flowGroupById.setName(flowGroupName);
                flowGroupDomain.saveOrUpdate(flowGroupById);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public String updateFlowGroupBaseInfo(FlowGroupVo flowGroupVo) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == flowGroupVo || StringUtils.isBlank(flowGroupVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed error");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupVo.getId());
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Database save failed");
        }
        flowGroupById.setDescription(flowGroupVo.getDescription());
        flowGroupById.setLastUpdateDttm(new Date());
        flowGroupById.setLastUpdateUser(currentUser.getUsername());
        flowGroupDomain.saveOrUpdate(flowGroupById);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowGroupVo", flowGroupVo);
    }

}
