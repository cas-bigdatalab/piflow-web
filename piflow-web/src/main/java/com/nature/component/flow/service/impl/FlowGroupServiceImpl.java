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
import com.nature.domain.flow.FlowGroupDomain;
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

//    @Resource
//    private FlowDomain flowDomain;
//
//    @Resource
//    private MxGraphModelDomain mxGraphModelDomain;
//
//    @Resource
//    private MxCellDomain mxCellDomain;
//
//    @Resource
//    private MxGeometryDomain mxGeometryDomain;
//
//    @Resource
//    private FlowGroupPathsDomain flowGroupPathsDomain;


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
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isNotBlank(flowGroupId)) {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            String username = currentUser.getUsername();
            // find flow by flowId
            FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
            // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
            if (null != flowGroupById) {


                ProcessGroup processGroup = new ProcessGroup();
                // copy flowGroup信息到processGroup
                BeanUtils.copyProperties(flowGroupById, processGroup);
                // set base info
                processGroup.setId(SqlUtils.getUUID32());
                processGroup.setCrtDttm(new Date());
                processGroup.setCrtUser(username);
                processGroup.setLastUpdateDttm(new Date());
                processGroup.setLastUpdateUser(username);
                processGroup.setEnableFlag(true);
                // Take out the sketchpad information of 'flowgroup'
                MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
                // The 'flowGroup' palette information changes to 'viewXml'
                String viewXml = SvgUtils.mxGraphModelToViewXml(mxGraphModel, true, false);
                // set viewXml
                processGroup.setViewXml(viewXml);
                // set flowGroupId
                processGroup.setFlowId(flowGroupById.getId());
                if (StringUtils.isNotBlank(runMode)) {
                    runModeType = RunModeType.selectGender(runMode);
                }
                processGroup.setRunModeType(runModeType);
                processGroup.setProcessParentType(ProcessParentType.GROUP);
                processGroup = processGroupDomain.saveOrUpdate(processGroup);

                // Get the paths information of flow
                List<FlowGroupPaths> flowGroupPathsList = flowGroupById.getFlowGroupPathsList();
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
                List<Flow> flowList = flowGroupById.getFlowList();
                // flowList isEmpty
                if (null != flowList && flowList.size() > 0) {
                    // List of stop of process
                    List<Process> processList = new ArrayList<>();
                    // Loop flowList
                    for (Flow flow : flowList) {
                        // isEmpty
                        if (null != flow) {
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
                    }
                    processGroup.setProcessList(processList);
                }

                Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(processGroup, runModeType);

                if (200 == ((Integer) stringObjectMap.get("code"))) {
                    ProcessGroup processGroupById = processGroupDomain.getProcessGroupById(processGroup.getId());
                    processGroupById.setAppId((String) stringObjectMap.get("appId"));
                    processGroupById.setProcessId((String) stringObjectMap.get("appId"));
                    processGroupById.setState(ProcessState.STARTED);
                    processGroupById.setProcessParentType(ProcessParentType.GROUP);
                    processGroupDomain.saveOrUpdate(processGroupById);
                    rtnMap.put("code", 200);
                    rtnMap.put("processGroupId", processGroup.getId());
                    rtnMap.put("errorMsg", "save process success,update success");
                    logger.info("save process success,update success");
                } else {
                    processGroupDomain.updateEnableFlagById(processGroup.getId(), false);
                    rtnMap.put("errorMsg", stringObjectMap.get("errorMsg"));
                    logger.warn(stringObjectMap.get("errorMsg").toString());
                }
            } else {
                rtnMap.put("errorMsg", "Flow with FlowGroupId" + flowGroupId + "was not queried");
                logger.warn("Flow with FlowGroupId" + flowGroupId + "was not queried");
            }
        } else {
            rtnMap.put("errorMsg", "FlowGroupId is null");
            logger.warn("FlowGroupId is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
            MxCell mxCell0 = new MxCell();
            mxCell0.setMxGraphModel(mxGraphModel);
            mxCell0.setCrtDttm(new Date());
            mxCell0.setCrtUser(currentUser.getUsername());
            mxCell0.setLastUpdateDttm(new Date());
            mxCell0.setLastUpdateUser(currentUser.getUsername());
            mxCell0.setPageId("0");
            root.add(mxCell0);
            MxCell mxCell1 = new MxCell();
            mxCell1.setMxGraphModel(mxGraphModel);
            mxCell1.setCrtDttm(new Date());
            mxCell1.setCrtUser(currentUser.getUsername());
            mxCell1.setLastUpdateDttm(new Date());
            mxCell1.setLastUpdateUser(currentUser.getUsername());
            mxCell1.setPageId("1");
            mxCell1.setParent("0");
            root.add(mxCell1);
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
        rtnMap.put("xmlStr",loadXml);
        return JsonUtils.toFormatJsonNoException(rtnMap);
    }

//    /**
//     * save or add flowGroup
//     *
//     * @param imageXML
//     * @param loadId
//     * @param operType
//     * @param flag
//     * @return
//     */
//    @Override
//    @Transactional
//    public String saveOrUpdateFlowGroupAll(String imageXML, String loadId, String operType, boolean flag) {
//        Map<String, Object> rtnMap = new HashMap<>();
//        rtnMap.put("code", 500);
//        UserVo currentUser = SessionUserUtil.getCurrentUser();
//        if (null == currentUser) {
//            rtnMap.put("errorMsg", "Illegal operation");
//            logger.info("Illegal operation");
//            return JsonUtils.toJsonNoException(rtnMap);
//        }
//        if (StringUtils.isAnyEmpty(imageXML, loadId, operType)) {
//            rtnMap.put("errorMsg", "The incoming parameters are empty");
//            logger.info("The incoming parameters are empty");
//            return JsonUtils.toJsonNoException(rtnMap);
//        }
//        // Change the `XML'from the page to `mxGraphModel'
//        MxGraphModelVo mxGraphModelVo = FlowXmlUtils.xmlToMxGraphModel(imageXML);
//        // Parameter null
//        if (StringUtils.isAnyEmpty(loadId, operType)) {
//            rtnMap.put("errorMsg", "The incoming parameter flowId or operType is empty and the save failed.");
//            logger.warn("The incoming parameter flowId or operType is empty and the save failed.");
//            return JsonUtils.toJsonNoException(rtnMap);
//        }
//        // mxGraphModelVo Parameter null
//        if (null == mxGraphModelVo) {
//            rtnMap.put("errorMsg", "The passed parameter mxGraphModelVo is empty and the save failed.");
//            logger.warn("The passed parameter mxGraphModelVo is empty and the save failed.");
//            return JsonUtils.toJsonNoException(rtnMap);
//        }
//        if ("ADD".equals(operType)) {
//            logger.info("ADD Operation begins");
//            rtnMap = this.addFlows(mxGraphModelVo, loadId, currentUser.getUsername());
//        } else if ("MOVED".equals(operType)) {
//            logger.info("MOVED Operation begins");
//            rtnMap = this.updateMxGraph(mxGraphModelVo, loadId, currentUser.getUsername());
//        } else if ("REMOVED".equals(operType)) {
//            logger.info("REMOVED Operation begins");
//            rtnMap = this.updateFlowGroup(mxGraphModelVo, loadId);
//        } else {
//            rtnMap.put("errorMsg", ("No operType:" + operType + "type"));
//            logger.warn("No operType:" + operType + "type");
//        }
//        return JsonUtils.toJsonNoException(rtnMap);
//    }
    
//    /**
//     * add flows and drawing board mxCell
//     *
//     * @param mxGraphModelVo
//     * @param flowGroupId
//     * @return
//     */
//    private Map<String, Object> addFlows(MxGraphModelVo mxGraphModelVo, String flowGroupId, String username) {
//        if (StringUtils.isBlank(username)) {
//            return ReturnMapUtils.setFailedMsg("Illegal operation");
//        }
//        // Query 'flowGroup' according to 'flowGroupId'
//        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(flowGroupId);
//        if (null == flowGroup) {
//            return ReturnMapUtils.setFailedMsg("No query to flowId is: " + flowGroupId + " Flow information");
//        }
//        // Determine if 'mxGraphModelVo' and 'flowGroup' are empty
//        if (null == mxGraphModelVo) {
//            return ReturnMapUtils.setFailedMsg("The passed parameter mxGraphModelVo is empty or the flow does not exist and the addition fails.");
//        }
//        // update flow
//        flowGroup.setLastUpdateDttm(new Date()); // last update date time
//        flowGroup.setLastUpdateUser(username);// last update user
//        flowGroup.setEnableFlag(true);// is it effective
//
//        // Take out the drawing board of the data inventory
//        MxGraphModel mxGraphModelDb = flowGroup.getMxGraphModel();
//        // Determine if the artboard of the data inventory exists
//        if (null == mxGraphModelDb) {
//            return ReturnMapUtils.setFailedMsg("Database without artboard, adding failed");
//        }
//        // Put the page's artboard information into the database canvas
//        // Copy the value from 'mxGraphModelVo' to 'mxGraphModelDb'
//        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModelDb);
//        mxGraphModelDb.setEnableFlag(true);
//        mxGraphModelDb.setLastUpdateUser(username);
//        mxGraphModelDb.setLastUpdateDttm(new Date());
//        mxGraphModelDb.setFlowGroup(flowGroup);
//
//        // The data passed from the page mxCellVoList
//        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
//        // Map of the data sent from the page
//        Map<String, MxCellVo> mxCellVoMap = new HashMap<>();
//        // The mxCellList passed to the page is transferred to the map, and the key is pageId.
//        if (null != mxCellVoList && mxCellVoList.size() > 0) {
//            // The mxCellList passed to the page is transferred to the map, and the key is pageId.
//            for (MxCellVo mxCellVo : mxCellVoList) {
//                if (null != mxCellVo && StringUtils.isNotBlank(mxCellVo.getPageId())) {
//                    mxCellVoMap.put(mxCellVo.getPageId(), mxCellVo);
//                }
//            }
//        }
//        // Loop database data
//        List<MxCell> mxCellDbRoot = mxGraphModelDb.getRoot();
//        for (MxCell mxCell : mxCellDbRoot) {
//            if (null != mxCell) {
//                // Use pageId to go to map
//                MxCellVo mxCellVo = mxCellVoMap.get(mxCell.getPageId());
//                // Get the description database already exists, do not need to add, remove the value removed in the map
//                if (null != mxCellVo) {
//                    mxCellVoMap.remove(mxCell.getPageId());
//                }
//            }
//        }
//        // Determine whether there is data in the map after remove, if there is any new processing
//        if (mxCellVoMap.size() <= 0) {
//            return ReturnMapUtils.setFailedMsg("No data can be added, the addition failed");
//        }
//        // Convert MxCellVo map to MxCellVoList
//        List<MxCellVo> addMxCellVoList = new ArrayList<>(mxCellVoMap.values());
//        if (addMxCellVoList.size() <= 0) {
//            return ReturnMapUtils.setFailedMsg("No data can be added, the addition failed");
//        }
//
//        for (MxCellVo mxCellVo : addMxCellVoList) {
//            if (null != mxCellVo) {
//                // save MxCell
//                // new
//                MxCell mxCell = new MxCell();
//                // Copy the value in mxCellVo to mxCell
//                BeanUtils.copyProperties(mxCellVo, mxCell);
//                if (null != mxCellVo.getValue()) {
//                    mxCell.setValue(mxCellVo.getValue() + mxCellVo.getPageId());
//                }
//                // Basic properties of mxCell (Required when creating)
//                mxCell.setCrtDttm(new Date());
//                mxCell.setCrtUser(username);
//                // Basic properties of mxCell
//                mxCell.setEnableFlag(true);
//                mxCell.setLastUpdateUser(username);
//                mxCell.setLastUpdateDttm(new Date());
//                // mxGraphModel Foreign key
//                mxCell.setMxGraphModel(mxGraphModelDb);
//
//                MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
//                if (null != mxGeometryVo) {
//                    // save MxGeometry
//                    // new
//                    MxGeometry mxGeometry = new MxGeometry();
//                    // Copy the value from mxGeometryVo to mxGeometry
//                    BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
//                    // Basic properties of mxGeometry (required when creating)
//                    mxGeometry.setCrtDttm(new Date());
//                    mxGeometry.setCrtUser(username);
//                    // Set mxGraphModel basic properties
//                    mxGeometry.setEnableFlag(true);
//                    mxGeometry.setLastUpdateUser(username);
//                    mxGeometry.setLastUpdateDttm(new Date());
//                    // mxCell Foreign key
//                    mxGeometry.setMxCell(mxCell);
//
//                    mxCell.setMxGeometry(mxGeometry);
//                }
//                mxCellDbRoot.add(mxCell);
//            }
//        }
//        mxGraphModelDb.setRoot(mxCellDbRoot);
//        mxGraphModelDb = mxGraphModelDomain.saveOrUpdate(mxGraphModelDb);
//        flowGroup.setMxGraphModel(mxGraphModelDb);
//
//        // Separate the flows and lines that need to be added in addMxCellVoList
//        Map<String, List<MxCellVo>> flowsPathsMap = MxGraphModelUtil.distinguishElementsPaths(addMxCellVoList);
//
//        // Take mxCellVoList (list of elements) from Map
//        List<MxCellVo> objectElements = flowsPathsMap.get("elements");
//
//        // Generate a list of elements based on the contents of the MxCellList
//        List<Flow> addFlowsList = this.mxCellVoListToFlowsList(objectElements, flowGroup, username);
//
//        List<Flow> flowList = flowGroup.getFlowList();
//        if (null != addFlowsList && addFlowsList.size() > 0) {
//            if (null == flowList) {
//                flowList = new ArrayList<>();
//            }
//            for (Flow flow : addFlowsList) {
//                flow.setFlowGroup(flowGroup);
//                flowList.add(flow);
//            }
//            flowList = flowDomain.saveOrUpdate(flowList);
//            flowGroup.setFlowList(flowList);
//        }
//
//        // Take "mxCellVoList" from the "Map" (array of lines)
//        List<MxCellVo> objectPaths = flowsPathsMap.get("paths");
//
//        // Generate a list of paths based on the contents of the MxCellList
//        List<FlowGroupPaths> addFlowGroupPathsList = MxGraphModelUtil.mxCellVoListToFlowGroupPathsList(objectPaths, flowGroup);
//        // Judge empty pathsList
//        if (null != addFlowGroupPathsList && addFlowGroupPathsList.size() > 0) {
//            List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
//            for (FlowGroupPaths addFlowGroupPaths : addFlowGroupPathsList) {
//                addFlowGroupPaths.setFlowGroup(flowGroup);
//                flowGroupPathsList.add(addFlowGroupPaths);
//            }
//        }
//
//        // Update flow information
//        flowGroupDomain.saveOrUpdate(flowGroup);
//        return ReturnMapUtils.setSucceededMsg("Succeeded");
//    }
    
//    /**
//     * Generate a list of flows based on the contents of MxCellVoList
//     *
//     * @param objectFlows
//     * @param flowGroup
//     * @return
//     */
//    private List<Flow> mxCellVoListToFlowsList(List<MxCellVo> objectFlows, FlowGroup flowGroup, String username) {
//        List<Flow> flowsList = null;
//        if (null != objectFlows && objectFlows.size() > 0) {
//            flowsList = new ArrayList<>();
//            // Loop objectFlows
//            for (MxCellVo mxCellVo : objectFlows) {
//                Flow flow = new Flow();
//                if (null != mxCellVo) {
//                    flow.setCrtDttm(new Date());
//                    flow.setCrtUser(username);
//                    flow.setLastUpdateDttm(new Date());
//                    flow.setLastUpdateUser(username);
//                    flow.setEnableFlag(true);
//                    flow.setPageId(mxCellVo.getPageId());
//
//                    flow.setName("flow" + mxCellVo.getPageId());
//                    MxGraphModel mxGraphModel = new MxGraphModel();
//                    mxGraphModel.setFlow(flow);
//                    mxGraphModel.setCrtDttm(new Date());
//                    mxGraphModel.setCrtUser(username);
//                    mxGraphModel.setLastUpdateDttm(new Date());
//                    mxGraphModel.setLastUpdateUser(username);
//                    mxGraphModel.setEnableFlag(true);
//                    flow.setMxGraphModel(mxGraphModel);
//                    flow.setFlowGroup(flowGroup);
//                    flowsList.add(flow);
//                }
//            }
//        }
//        return flowsList;
//    }
    
//    /**
//     * Modify Flow
//     *
//     * @param mxGraphModelVo Information from the page
//     * @param flowGroupId    The data to be modified
//     * @return
//     */
//    private Map<String, Object> updateFlowGroup(MxGraphModelVo mxGraphModelVo, String flowGroupId) {
//        UserVo user = SessionUserUtil.getCurrentUser();
//        if (null == user) {
//            return ReturnMapUtils.setFailedMsg("Illegal operation");
//        }
//        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(flowGroupId);
//        if (null == flowGroup) {
//            return ReturnMapUtils.setFailedMsg("The flowGroupId cannot find the corresponding flowGroup, and the modification fails.");
//        }
//        if (null == mxGraphModelVo) {
//            return ReturnMapUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
//        }
//        // Last update time
//        flowGroup.setLastUpdateDttm(new Date());
//        // Last updater
//        flowGroup.setLastUpdateUser(user.getUsername());
//
//        // save flowGroup
//        flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);
//        // Save and modify the artboard information
//        Map<String, Object> map = this.updateMxGraph(mxGraphModelVo, flowGroupId, user.getUsername());
//        // Determine if mxGraphModel is saved successfully
//        if (null != map) {
//            Object code = map.get("code");
//            if (null == code || 200 != (int) code) {
//                return map;
//            }
//        } else {
//            return ReturnMapUtils.setFailedMsg("failed, Near 'updateMxGraph' ");
//        }
//
//        // MxCellVo's list from the page
//        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
//
//        // Separate the flow and lines in the mxCellVoList
//        Map<String, List<MxCellVo>> stopsPathsMap = MxGraphModelUtil.distinguishElementsPaths(mxCellVoList);
//
//        if (null != stopsPathsMap) {
//            // Get out the PathsList stored in the database
//            List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
//            // Determine whether the list of lines in the database is empty, do not empty to continue the judgment operation below, or directly add
//            if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
//                // Take the line of mxCellVoList
//                List<MxCellVo> objectPaths = stopsPathsMap.get("paths");
//                // Key is the PageId of PathsVo (the id in the artboard), and the value is Paths
//                Map<String, MxCellVo> objectPathsMap = new HashMap<>();
//
//                // The pathsList to be modified
//                List<FlowGroupPaths> updatePaths = new ArrayList<>();
//                //Determine if the objectStops passed from the page is empty.
//                if (null != objectPaths) {
//                    for (MxCellVo mxCellVo : objectPaths) {
//                        if (null != mxCellVo) {
//                            objectPathsMap.put(mxCellVo.getPageId(), mxCellVo);
//                        }
//                    }
//                }
//
//                // The data pathsList of the loop database is retrieved by using the pageId in the stops to convert the map to the value of the page passed by the map.
//                for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
//                    if (null != flowGroupPaths) {
//                        String pageId = flowGroupPaths.getPageId();
//                        MxCellVo mxCellVo = objectPathsMap.get(pageId);
//                        // If you get it, you need to modify it. Otherwise, it is to be deleted.
//                        if (null == mxCellVo) {
//                            flowGroupPaths.setEnableFlag(false);
//                            flowGroupPaths.setLastUpdateUser(user.getUsername());
//                            flowGroupPaths.setLastUpdateDttm(new Date());
//                            flowGroupPaths.setFlowGroup(flowGroup);
//                            updatePaths.add(flowGroupPaths);
//                        }
//                    }
//                }
//                // save update updateStops
//                if (updatePaths.size() > 0) {
//                    flowGroupPathsDomain.saveOrUpdate(updatePaths);
//                }
//            } else {
//                //The stopsList in the database is empty and the modification failed.
//                logger.info("The pathsList in the database is empty");
//            }
//
//
//            // Get out the flowList stored in the database
//            List<Flow> flowList = flowGroup.getFlowList();
//
//            // If the flowList in the database is empty, the modification fails because this method only processes the modifications, and is not responsible for adding
//            if (null != flowList && flowList.size() > 0) {
//                //Take the flow of mxCellVoList
//                List<MxCellVo> objectStops = stopsPathsMap.get("elements");
//                // Key is the PageId of mxCellVo (the id in the artboard), and the value is mxCellVo
//                Map<String, MxCellVo> objectStopsMap = new HashMap<>();
//
//                // FlowList to be modified
//                List<Flow> updateFlowList = new ArrayList<>();
//
//                // Determine if the objectStops passed from the page is empty.
//                if (null != objectStops) {
//                    // Loop Convert objectStops (page data) to objectStopsMap
//                    for (MxCellVo mxCellVo : objectStops) {
//                        if (null != mxCellVo) {
//                            objectStopsMap.put(mxCellVo.getPageId(), mxCellVo);
//                        }
//                    }
//                }
//                // The data flowList of the loop database is converted to the map of the value passed by the page after the map is converted by the pageId in the flow.
//                for (Flow flow : flowList) {
//                    if (null != flow) {
//                        String pageId = flow.getPageId();
//                        MxCellVo mxCellVo = objectStopsMap.get(pageId);
//                        // If you get it, you need to modify it. Otherwise, it is to be deleted.
//                        if (null == mxCellVo) {
//                            flow.setEnableFlag(false);//logically delete
//                            flow.setLastUpdateDttm(new Date());//Last update time
//                            flow.setLastUpdateUser(user.getUsername());//Last updater
//                            updateFlowList.add(flow);
//                        }
//                    }
//                }
//                if (updateFlowList.size() > 0) {
//                    // update save updateFlowList
//                    flowDomain.saveOrUpdate(updateFlowList);
//                }
//            } else {
//                // The stops data in the database is empty.
//                logger.info("The stops data in the database is empty.");
//            }
//        }
//        return ReturnMapUtils.setSucceededMsg("Succeeded");
//    }
    
//    /**
//     * Modification of the artboard
//     *
//     * @param mxGraphModelVo
//     * @param flowGroupId
//     * @return
//     */
//    private Map<String, Object> updateMxGraph(MxGraphModelVo mxGraphModelVo, String flowGroupId, String username) {
//        if (StringUtils.isBlank(username)) {
//            return ReturnMapUtils.setFailedMsg("Illegal operation");
//        }
//        // Determine if the incoming data is empty
//        if (null == mxGraphModelVo) {
//            return ReturnMapUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
//        }
//        MxGraphModel mxGraphModel = mxGraphModelDomain.getMxGraphModelByFlowId(flowGroupId);
//        // Determine if the database data to be modified is empty
//        if (null == mxGraphModel) {
//            return ReturnMapUtils.setFailedMsg("No query to flowGroupId is: “" + flowGroupId + "”mxGraphModel information is empty, modification failed");
//        }
//        // Copy the value from mxGraphModelVo to mxGraphModelDb
//        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
//        // setmxGraphModel basic properties
//        mxGraphModel.setLastUpdateUser(username);// Last updater
//        mxGraphModel.setLastUpdateDttm(new Date());// Last update time
//        mxGraphModel.setEnableFlag(true);// is it effective
//        // save MxGraphModel
//        mxGraphModelDomain.saveOrUpdate(mxGraphModel);
//        // The data passed from the page MxCellVo
//        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
//        // Take out the MxCellList information queried by the database.
//        List<MxCell> mxCellList = mxGraphModel.getRoot();
//        // Save and process mxCellList
//        return this.updateMxCellList(mxCellVoList, mxCellList, username);
//    }
    
//    /**
//     * Save and process mxCellList
//     *
//     * @param mxCellVoList
//     * @param mxCellList
//     * @return
//     */
//    private Map<String, Object> updateMxCellList(List<MxCellVo> mxCellVoList, List<MxCell> mxCellList, String username) {
//        // If the mxCellList is empty, the modification fails because this method only processes the modifications and is not responsible for adding
//        if (null == mxCellList || mxCellList.size() <= 0) {
//            return ReturnMapUtils.setFailedMsg("The database mxCellList is empty and the modification failed.");
//        }
//        // Including modified and tombstoned
//        List<MxCell> updateMxCellList = new ArrayList<>();
//        // Convert the list passed to the page to map key for pageId
//        Map<String, MxCellVo> mxCellVoMap = new HashMap<>();
//        // Judge
//        if (null != mxCellVoList) {
//            for (MxCellVo mxCell : mxCellVoList) {
//                mxCellVoMap.put(mxCell.getPageId(), mxCell);
//            }
//        }
//        // Loop page data for classification (requires modification and tombstone)
//        for (MxCell mxCell : mxCellList) {
//            if (null != mxCell) {
//                // Graphic ID (pageId) on the artboard
//                String pageId = mxCell.getPageId();
//                // According to the pageId to go to map,
//                // Get the description database has a page, do the modification operation,
//                // Otherwise, the database has no pages, and the logical deletion is performed.
//                MxCellVo mxCellVo = mxCellVoMap.get(pageId);
//                if (null != mxCellVo) {
//                    // Copy the value in mxCellVo to mxCell
//                    BeanUtils.copyProperties(mxCellVo, mxCell);
//                    //  Basic properties of mxCell
//                    mxCell.setEnableFlag(true);// is it effective
//                    mxCell.setLastUpdateUser(username);// Last updater
//                    mxCell.setLastUpdateDttm(new Date());// Last update time
//
//                    // Do not handle foreign keys when modifying, unless you cancel or modify the foreign key
//                    // mxGraphModel foreign key
//                    // mxCell.setMxGraphModel(mxGraphModel);
//                    MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
//                    MxGeometry mxGeometry = mxCell.getMxGeometry();
//                    if (null != mxGeometry) {
//                        if (null != mxGeometryVo) {
//                            // Copy the value from mxGeometryVo into mxGeometry
//                            BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
//
//                            // setmxGraphModel basic properties
//                            mxGeometry.setLastUpdateUser(username);// Last updater
//                            mxGeometry.setLastUpdateDttm(new Date());// Last update time
//                            mxGeometry.setEnableFlag(true);// is it effective
//                            mxGeometry.setMxCell(mxCell);
//                        }
//                    }
//                } else {
//                    //Logical deletion
//                    mxCell.setEnableFlag(false);
//                    mxCell.setLastUpdateDttm(new Date());
//                    mxCell.setLastUpdateUser(username);
//                }
//                // Fill in the modified list
//                updateMxCellList.add(mxCell);
//            }
//        }
//        if (updateMxCellList.size() > 0) {
//            for (MxCell mxCell : updateMxCellList) {
//                if (null != mxCell) {
//                    MxGeometry mxGeometry = mxCell.getMxGeometry();
//                    // save mxCell
//                    mxCell = mxCellDomain.saveOrUpdate(mxCell);
//                    if (null != mxGeometry) {
//                        mxGeometry.setMxCell(mxCell);
//                        // Take the modified save mxGeometry
//                        mxGeometryDomain.saveOrUpdate(mxGeometry);
//                    }
//                }
//            }
//        }
//        return ReturnMapUtils.setSucceededMsg("Succeeded");
//    }

}
