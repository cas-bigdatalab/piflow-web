package com.nature.component.flow.service.impl;

import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessParentType;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.*;
import com.nature.component.flow.service.IFlowGroupService;
import com.nature.component.flow.utils.FlowGroupPathsUtil;
import com.nature.component.flow.utils.FlowUtil;
import com.nature.component.flow.utils.MxGraphModelUtil;
import com.nature.component.flow.vo.FlowGroupPathsVo;
import com.nature.component.flow.vo.FlowGroupVo;
import com.nature.component.flow.vo.FlowVo;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.*;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.FlowGroupDomain;
import com.nature.domain.flow.FlowGroupPathsDomain;
import com.nature.domain.flow.PathsDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.domain.process.*;
import com.nature.mapper.flow.FlowGroupMapper;
import com.nature.mapper.mxGraph.MxGraphModelMapper;
import com.nature.third.service.IGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class FlowGroupServiceImpl implements IFlowGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private FlowGroupDomain flowGroupDomain;

    @Autowired
    private FlowDomain flowDomain;

    @Autowired
    private MxGraphModelDomain mxGraphModelDomain;

    @Autowired
    private MxCellDomain mxCellDomain;

    @Autowired
    private MxGeometryDomain mxGeometryDomain;

    @Autowired
    private FlowGroupPathsDomain flowGroupPathsDomain;

    @Autowired
    private ProcessGroupDomain processGroupDomain;

    @Autowired
    private ProcessDomain processDomain;

    @Autowired
    private ProcessPathDomain processPathDomain;

    @Autowired
    private ProcessGroupPathDomain processGroupPathDomain;

    @Autowired
    private ProcessStopDomain processStopDomain;

    @Autowired
    private ProcessStopPropertyDomain processStopPropertyDomain;

    @Autowired
    private ProcessStopCustomizedPropertyDomain processStopCustomizedPropertyDomain;

    @Autowired
    private IGroup groupImpl;

    @Resource
    private FlowGroupMapper flowGroupMapper;


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
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
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
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
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
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null != offset && null != limit) {
            Page<FlowGroup> flowGroupListPage = flowGroupDomain.getFlowGroupListPage(offset - 1, limit, param);
            List<FlowGroupVo> contentVo = new ArrayList<>();
            List<FlowGroup> content = flowGroupListPage.getContent();
            if (null != content && content.size() > 0) {
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
    @Transactional
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
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(username)) {
            if (null != flowGroupVo) {

                FlowGroup flowGroup = new FlowGroup();

                BeanUtils.copyProperties(flowGroupVo, flowGroup);
                String id = SqlUtils.getUUID32();
                flowGroup.setId(id);
                flowGroup.setCrtDttm(new Date());
                flowGroup.setCrtUser(username);
                flowGroup.setLastUpdateDttm(new Date());
                flowGroup.setLastUpdateUser(username);
                flowGroup.setEnableFlag(true);
                flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);

                MxGraphModel mxGraphModel = new MxGraphModel();
                mxGraphModel.setFlowGroup(flowGroup);
                mxGraphModel.setId(SqlUtils.getUUID32());
                mxGraphModel.setCrtDttm(new Date());
                mxGraphModel.setCrtUser(username);
                mxGraphModel.setLastUpdateDttm(new Date());
                mxGraphModel.setLastUpdateUser(username);
                mxGraphModel.setEnableFlag(true);
                mxGraphModel.setFlowGroup(flowGroup);
                mxGraphModelDomain.saveOrUpdate(mxGraphModel);
                rtnMap.put("code", 200);
                rtnMap.put("flowGroupId", flowGroup.getId());
            }
        } else {
            rtnMap.put("errorMsg", "Illegal users");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    private String update(FlowGroupVo flowGroupVo, String username) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
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
     * save or add flowGroup
     *
     * @param imageXML
     * @param loadId
     * @param operType
     * @param flag
     * @return
     */
    @Override
    @Transactional
    public String saveOrUpdateFlowGroupAll(String imageXML, String loadId, String operType, boolean flag) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal operation");
            logger.info("Illegal operation");
            return JsonUtils.toJsonNoException(rtnMap);
        } else {
            if (StringUtils.isAnyEmpty(imageXML, loadId, operType)) {
                rtnMap.put("errorMsg", "The incoming parameters are empty");
                logger.info("The incoming parameters are empty");
                return JsonUtils.toJsonNoException(rtnMap);
            } else {
                // Change the `XML'from the page to `mxGraphModel'
                MxGraphModelVo mxGraphModelVo = FlowXmlUtils.xmlToMxGraphModel(imageXML);
                StatefulRtnBase statefulRtnBase = null;
                // Parameter null
                if (!StringUtils.isAnyEmpty(loadId, operType)) {
                    // mxGraphModelVo Parameter null
                    if (null != mxGraphModelVo) {
                        if ("ADD".equals(operType)) {
                            logger.info("ADD Operation begins");
                            statefulRtnBase = this.addFlows(mxGraphModelVo, loadId, flag, currentUser.getUsername());
                        } else if ("MOVED".equals(operType)) {
                            logger.info("MOVED Operation begins");
                            statefulRtnBase = this.updateMxGraph(mxGraphModelVo, loadId, currentUser.getUsername());
                        } else if ("REMOVED".equals(operType)) {
                            logger.info("REMOVED Operation begins");
                            statefulRtnBase = this.updateFlowGroup(mxGraphModelVo, loadId);
                        } else {
                            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No operType:" + operType + "type");
                            logger.warn("No operType:" + operType + "type");
                        }
                    } else {
                        statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("The passed parameter mxGraphModelVo is empty and the save failed.");
                        logger.warn("The passed parameter mxGraphModelVo is empty and the save failed.");
                    }
                } else {
                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("The incoming parameter flowId or operType is empty and the save failed.");
                    logger.warn("The incoming parameter flowId or operType is empty and the save failed.");
                }
                // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
                if (null != statefulRtnBase && statefulRtnBase.isReqRtnStatus()) {
                    rtnMap.put("code", 200);
                    rtnMap.put("errorMsg", "Successful Preservation");
                    logger.info("Successful Preservation");
                } else {
                    rtnMap.put("errorMsg", "Save failed");
                    logger.info("Save failed");
                }
                return JsonUtils.toJsonNoException(rtnMap);
            }
        }
    }

    /**
     * add flows and drawing board mxCell
     *
     * @param mxGraphModelVo
     * @param flowGroupId
     * @param flag           Whether to add stop information
     * @return
     */
    private StatefulRtnBase addFlows(MxGraphModelVo mxGraphModelVo, String flowGroupId, boolean flag, String username) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        // Query 'flowGroup' according to 'flowGroupId'
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null != flowGroup) {
            // Determine if 'mxGraphModelVo' and 'flowGroup' are empty
            if (null != mxGraphModelVo && null != flowGroup) {
                // update flow
                flowGroup.setLastUpdateDttm(new Date()); // last update date time
                flowGroup.setLastUpdateUser(username);// last update user
                flowGroup.setEnableFlag(true);// is it effective
                // Update flow information
                flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);
                // Take out the drawing board of the data inventory
                MxGraphModel mxGraphModelDb = flowGroup.getMxGraphModel();
                // Determine if the artboard of the data inventory exists
                if (null != mxGraphModelDb) {
                    // Put the page's artboard information into the database canvas
                    // Copy the value from 'mxGraphModelVo' to 'mxGraphModelDb'
                    BeanUtils.copyProperties(mxGraphModelVo, mxGraphModelDb);
                    mxGraphModelDb.setEnableFlag(true);
                    mxGraphModelDb.setLastUpdateUser(username);
                    mxGraphModelDb.setLastUpdateDttm(new Date());
                    mxGraphModelDb.setFlowGroup(flowGroup);
                    // The 'flow' foreign key of the artboard is unchanged, no need to update
                    // mxGraphModelDb.setFlow(flow); Add a foreign key
                    // update mxGraphModel
                    mxGraphModelDb = mxGraphModelDomain.saveOrUpdate(mxGraphModelDb);

                    // The data passed from the page mxCellVoList
                    List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
                    // Map of the data sent from the page
                    Map<String, MxCellVo> mxCellVoMap = new HashMap<String, MxCellVo>();
                    // The mxCellList passed to the page is transferred to the map, and the key is pageId.
                    if (null != mxCellVoList && mxCellVoList.size() > 0) {
                        // The mxCellList passed to the page is transferred to the map, and the key is pageId.
                        for (MxCellVo mxCellVo : mxCellVoList) {
                            if (null != mxCellVo && StringUtils.isNotBlank(mxCellVo.getPageId())) {
                                mxCellVoMap.put(mxCellVo.getPageId(), mxCellVo);
                            }
                        }
                    }
                    // Loop database data
                    List<MxCell> mxCellDbRoot = mxGraphModelDb.getRoot();
                    for (MxCell mxCell : mxCellDbRoot) {
                        if (null != mxCell) {
                            // Use pageId to go to map
                            MxCellVo mxCellVo = mxCellVoMap.get(mxCell.getPageId());
                            // Get the description database already exists, do not need to add, remove the value removed in the map
                            if (null != mxCellVo) {
                                mxCellVoMap.remove(mxCell.getPageId());
                            }
                        }
                    }
                    // Determine whether there is data in the map after remove, if there is any new processing
                    if (mxCellVoMap.size() > 0) {
                        // Convert MxCellVo map to MxCellVoList
                        List<MxCellVo> addMxCellVoList = new ArrayList<MxCellVo>(mxCellVoMap.values());
                        if (null != addMxCellVoList && addMxCellVoList.size() > 0) {
                            for (MxCellVo mxCellVo : addMxCellVoList) {
                                if (null != mxCellVo) {
                                    // save MxCell
                                    // new
                                    MxCell mxCell = new MxCell();
                                    // Copy the value in mxCellVo to mxCell
                                    BeanUtils.copyProperties(mxCellVo, mxCell);
                                    if (null != mxCellVo.getValue()) {
                                        mxCell.setValue(mxCellVo.getValue() + mxCellVo.getPageId());
                                    }
                                    // Basic properties of mxCell (Required when creating)
                                    mxCell.setId(SqlUtils.getUUID32());
                                    mxCell.setCrtDttm(new Date());
                                    mxCell.setCrtUser(username);
                                    // Basic properties of mxCell
                                    mxCell.setEnableFlag(true);
                                    mxCell.setLastUpdateUser(username);
                                    mxCell.setLastUpdateDttm(new Date());
                                    // mxGraphModel Foreign key
                                    mxCell.setMxGraphModel(mxGraphModelDb);
                                    // 保存mxCell
                                    mxCell = mxCellDomain.saveOrUpdate(mxCell);
                                    MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                                    if (null != mxGeometryVo) {
                                        // save MxGeometry
                                        // new
                                        MxGeometry mxGeometry = new MxGeometry();
                                        // Copy the value from mxGeometryVo to mxGeometry
                                        BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
                                        // Basic properties of mxGeometry (required when creating)
                                        mxGeometry.setId(SqlUtils.getUUID32());
                                        mxGeometry.setCrtDttm(new Date());
                                        mxGeometry.setCrtUser(username);
                                        // Set mxGraphModel basic properties
                                        mxGeometry.setEnableFlag(true);
                                        mxGeometry.setLastUpdateUser(username);
                                        mxGeometry.setLastUpdateDttm(new Date());
                                        // mxCell Foreign key
                                        mxGeometry.setMxCell(mxCell);
                                        // save mxGeometry
                                        mxGeometryDomain.saveOrUpdate(mxGeometry);
                                    }

                                }
                            }


                            // Separate the flows and lines that need to be added in addMxCellVoList
                            Map<String, Object> flowsPathsMap = MxGraphModelUtil.distinguishElementsPaths(addMxCellVoList);

                            // Take mxCellVoList (list of elements) from Map
                            List<MxCellVo> objectElements = (ArrayList<MxCellVo>) flowsPathsMap.get("elements");

                            // Flows list
                            List<Flow> addFlowsList = new ArrayList<>();
                            // Generate a list of elements based on the contents of the MxCellList
                            addFlowsList = this.mxCellVoListToFlowsList(objectElements, flowGroup, username);
                            // save addElementsList
                            if (flag) {
                                if (null != addFlowsList && addFlowsList.size() > 0) {
                                    for (Flow flow : addFlowsList) {
                                        MxGraphModel mxGraphModel = flow.getMxGraphModel();
                                        Flow flow1 = flowDomain.saveOrUpdate(flow);
                                        mxGraphModel.setFlow(flow1);
                                        mxGraphModelDomain.saveOrUpdate(mxGraphModel);
                                    }
                                }
                            }

                            // Take "mxCellVoList" from the "Map" (array of lines)
                            List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) flowsPathsMap.get("paths");

                            // array of lines
                            List<FlowGroupPaths> addFlowGroupPathsList = new ArrayList<>();

                            // Generate a list of paths based on the contents of the MxCellList
                            addFlowGroupPathsList = MxGraphModelUtil.mxCellVoListToFlowGroupPathsList(objectPaths, flowGroup);
                            if (flag) {
                                // Judge empty pathsList
                                if (null != addFlowGroupPathsList && addFlowGroupPathsList.size() > 0) {
                                    // save addPathsList
                                    flowGroupPathsDomain.saveOrUpdate(addFlowGroupPathsList);
                                } else {
                                    logger.info("addPathsList is empty, not saved");
                                }
                            }
                        } else {
                            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No data can be added, the addition failed");
                        }
                    } else {
                        statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No data can be added, the addition failed");
                    }
                } else {
                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Database without artboard, adding failed");
                    logger.warn("Database without artboard, adding failed");
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("The passed parameter mxGraphModelVo is empty or the flow does not exist and the addition fails.");
                logger.warn("The passed parameter mxGraphModelVo is empty or the flow does not exist and the addition fails.");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No query to flowId is: " + flowGroupId + " Flow information");
            logger.warn("No query to flowId is: " + flowGroupId + " Flow information");
        }
        return statefulRtnBase;

    }

    /**
     * Modification of the artboard
     *
     * @param mxGraphModelVo
     * @param flowGroupId
     * @return
     */
    private StatefulRtnBase updateMxGraph(MxGraphModelVo mxGraphModelVo, String flowGroupId, String username) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        if (StringUtils.isNotBlank(username)) {
            // Determine if the incoming data is empty
            if (null != mxGraphModelVo) {
                MxGraphModel mxGraphModel = mxGraphModelDomain.getMxGraphModelByFlowId(flowGroupId);
                // Determine if the database data to be modified is empty
                if (null != mxGraphModel) {
                    // Copy the value from mxGraphModelVo to mxGraphModelDb
                    BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
                    // setmxGraphModel basic properties
                    mxGraphModel.setLastUpdateUser(username);// Last updater
                    mxGraphModel.setLastUpdateDttm(new Date());// Last update time
                    mxGraphModel.setEnableFlag(true);// is it effective
                    // save MxGraphModel
                    mxGraphModelDomain.saveOrUpdate(mxGraphModel);
                    // The data passed from the page MxCellVo
                    List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
                    // Take out the MxCellList information queried by the database.
                    List<MxCell> mxCellList = mxGraphModel.getRoot();
                    // Save and process mxCellList
                    statefulRtnBase = this.updateMxCellList(mxCellVoList, mxCellList, username);
                } else {
                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No query to flowGroupId is: “" + flowGroupId + "”mxGraphModel information is empty, modification failed");
                    logger.warn("No query to flowGroupId is: “" + flowGroupId + "”mxGraphModel information is empty, modification failed");
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Illegal operation");
        }

        return statefulRtnBase;
    }

    /**
     * Modify Flow
     *
     * @param mxGraphModelVo Information from the page
     * @param flowGroupId    The data to be modified
     * @return
     */
    private StatefulRtnBase updateFlowGroup(MxGraphModelVo mxGraphModelVo, String flowGroupId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null != flowGroup) {
            if (null != mxGraphModelVo) {
                // Last update time
                flowGroup.setLastUpdateDttm(new Date());
                // Last updater
                flowGroup.setLastUpdateUser(username);

                // save flowGroup
                flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);

                // Take out the artboard information
                MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
                // Save and modify the artboard information
                StatefulRtnBase updateMxGraphRtn = this.updateMxGraph(mxGraphModelVo, flowGroupId, username);
                // Determine if mxGraphModel is saved successfully
                if (null != updateMxGraphRtn && updateMxGraphRtn.isReqRtnStatus()) {
                    // MxCellVo's list from the page
                    List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();

                    // Separate the flow and lines in the mxCellVoList
                    Map<String, Object> stopsPathsMap = MxGraphModelUtil.distinguishElementsPaths(mxCellVoList);

                    if (null != stopsPathsMap) {
                        // Get out the PathsList stored in the database
                        List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
                        // Determine whether the list of lines in the database is empty, do not empty to continue the judgment operation below, or directly add
                        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
                            // Take the line of mxCellVoList
                            List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) stopsPathsMap.get("paths");
                            // Key is the PageId of PathsVo (the id in the artboard), and the value is Paths
                            Map<String, MxCellVo> objectPathsMap = new HashMap<String, MxCellVo>();

                            // The pathsList to be modified
                            List<FlowGroupPaths> updatePaths = new ArrayList<>();
                            //Determine if the objectStops passed from the page is empty.
                            if (null != objectPaths) {
                                for (MxCellVo mxCellVo : objectPaths) {
                                    if (null != mxCellVo) {
                                        objectPathsMap.put(mxCellVo.getPageId(), mxCellVo);
                                    }
                                }
                            }

                            // The data pathsList of the loop database is retrieved by using the pageId in the stops to convert the map to the value of the page passed by the map.
                            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                                if (null != flowGroupPaths) {
                                    String pageId = flowGroupPaths.getPageId();
                                    MxCellVo mxCellVo = objectPathsMap.get(pageId);
                                    // If you get it, you need to modify it. Otherwise, it is to be deleted.
                                    if (null != mxCellVo) {
                                        //There is no operation to modify the stop information when you operate the artboard. You can modify the properties, but save it immediately. No operation is required here.
                                    } else {
                                        flowGroupPaths.setEnableFlag(false);
                                        flowGroupPaths.setLastUpdateUser(username);
                                        flowGroupPaths.setLastUpdateDttm(new Date());
                                        flowGroupPaths.setFlowGroup(flowGroup);
                                        updatePaths.add(flowGroupPaths);
                                    }
                                }
                            }
                            // save update updateStops
                            if (null != updatePaths && updatePaths.size() > 0) {
                                flowGroupPathsDomain.saveOrUpdate(updatePaths);
                            }
                        } else {
                            //The stopsList in the database is empty and the modification failed.
                            logger.info("The pathsList in the database is empty");
                        }


                        // Get out the flowList stored in the database
                        List<Flow> flowList = flowGroup.getFlowList();

                        // If the flowList in the database is empty, the modification fails because this method only processes the modifications, and is not responsible for adding
                        if (null != flowList && flowList.size() > 0) {
                            //Take the flow of mxCellVoList
                            List<MxCellVo> objectStops = (ArrayList<MxCellVo>) stopsPathsMap.get("elements");
                            // Key is the PageId of mxCellVo (the id in the artboard), and the value is mxCellVo
                            Map<String, MxCellVo> objectStopsMap = new HashMap<String, MxCellVo>();

                            // FlowList to be modified
                            List<Flow> updateFlowList = new ArrayList<>();

                            // Determine if the objectStops passed from the page is empty.
                            if (null != objectStops) {
                                // Loop Convert objectStops (page data) to objectStopsMap
                                for (MxCellVo mxCellVo : objectStops) {
                                    if (null != mxCellVo) {
                                        objectStopsMap.put(mxCellVo.getPageId(), mxCellVo);
                                    }
                                }
                            }
                            // The data flowList of the loop database is converted to the map of the value passed by the page after the map is converted by the pageId in the flow.
                            for (Flow flow : flowList) {
                                if (null != flow) {
                                    String pageId = flow.getPageId();
                                    MxCellVo mxCellVo = objectStopsMap.get(pageId);
                                    // If you get it, you need to modify it. Otherwise, it is to be deleted.
                                    if (null == mxCellVo) {
                                        flow.setEnableFlag(false);//logically delete
                                        flow.setLastUpdateDttm(new Date());//Last update time
                                        flow.setLastUpdateUser(username);//Last updater
                                        updateFlowList.add(flow);
                                    }
                                }
                            }
                            if (null != updateFlowList && updateFlowList.size() > 0) {
                                // update save updateFlowList
                                flowDomain.saveOrUpdate(updateFlowList);
                            }
                        } else {
                            // The stops data in the database is empty.
                            logger.info("The stops data in the database is empty.");
                        }
                    }
                } else {
                    statefulRtnBase = updateMxGraphRtn;
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("The flowGroupId cannot find the corresponding flowGroup, and the modification fails.");
        }
        return statefulRtnBase;
    }

    /**
     * Generate a list of flows based on the contents of MxCellVoList
     *
     * @param objectFlows
     * @param flowGroup
     * @return
     */
    private List<Flow> mxCellVoListToFlowsList(List<MxCellVo> objectFlows, FlowGroup flowGroup, String username) {
        List<Flow> flowsList = null;
        if (null != objectFlows && objectFlows.size() > 0) {
            flowsList = new ArrayList<Flow>();
            // Loop objectFlows
            for (MxCellVo mxCellVo : objectFlows) {
                Flow flow = new Flow();
                if (null != flow) {
                    String id = SqlUtils.getUUID32();
                    flow.setId(id);
                    flow.setCrtDttm(new Date());
                    flow.setCrtUser(username);
                    flow.setLastUpdateDttm(new Date());
                    flow.setLastUpdateUser(username);
                    flow.setEnableFlag(true);
                    flow.setUuid(id);
                    flow.setPageId(mxCellVo.getPageId());

                    flow.setName("flow" + mxCellVo.getPageId());
                    MxGraphModel mxGraphModel = new MxGraphModel();
                    mxGraphModel.setFlow(flow);
                    mxGraphModel.setId(SqlUtils.getUUID32());
                    mxGraphModel.setCrtDttm(new Date());
                    mxGraphModel.setCrtUser(username);
                    mxGraphModel.setLastUpdateDttm(new Date());
                    mxGraphModel.setLastUpdateUser(username);
                    mxGraphModel.setEnableFlag(true);
                    flow.setMxGraphModel(mxGraphModel);
                    flow.setFlowGroup(flowGroup);
                    flowsList.add(flow);
                }
            }
        }
        return flowsList;
    }

    /**
     * Save and process mxCellList
     *
     * @param mxCellVoList
     * @param mxCellList
     * @return
     */
    private StatefulRtnBase updateMxCellList(List<MxCellVo> mxCellVoList, List<MxCell> mxCellList, String username) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        // If the mxCellList is empty, the modification fails because this method only processes the modifications and is not responsible for adding
        if (null != mxCellList && mxCellList.size() > 0) {

            // Including modified and tombstoned
            List<MxCell> updateMxCellList = new ArrayList<MxCell>();
            // Convert the list passed to the page to map key for pageId
            Map<String, MxCellVo> mxCellVoMap = new HashMap<String, MxCellVo>();
            // Judge
            if (null != mxCellVoList) {
                for (MxCellVo mxCell : mxCellVoList) {
                    mxCellVoMap.put(mxCell.getPageId(), mxCell);
                }
            }
            // Loop page data for classification (requires modification and tombstone)
            for (MxCell mxCell : mxCellList) {
                if (null != mxCell) {
                    // Graphic ID (pageId) on the artboard
                    String pageId = mxCell.getPageId();
                    // According to the pageId to go to map,
                    // Get the description database has a page, do the modification operation,
                    // Otherwise, the database has no pages, and the logical deletion is performed.
                    MxCellVo mxCellVo = mxCellVoMap.get(pageId);
                    if (null != mxCellVo) {
                        // Copy the value in mxCellVo to mxCell
                        BeanUtils.copyProperties(mxCellVo, mxCell);
                        //  Basic properties of mxCell
                        mxCell.setEnableFlag(true);// is it effective
                        mxCell.setLastUpdateUser(username);// Last updater
                        mxCell.setLastUpdateDttm(new Date());// Last update time

                        // Do not handle foreign keys when modifying, unless you cancel or modify the foreign key
                        // mxGraphModel foreign key
                        // mxCell.setMxGraphModel(mxGraphModel);
                        MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                        MxGeometry mxGeometry = mxCell.getMxGeometry();
                        if (null != mxGeometry) {
                            if (null != mxGeometryVo) {
                                // Copy the value from mxGeometryVo into mxGeometry
                                BeanUtils.copyProperties(mxGeometryVo, mxGeometry);

                                // setmxGraphModel basic properties
                                mxGeometry.setLastUpdateUser(username);// Last updater
                                mxGeometry.setLastUpdateDttm(new Date());// Last update time
                                mxGeometry.setEnableFlag(true);// is it effective
                                mxGeometry.setMxCell(mxCell);
                            }
                        }
                    } else {
                        //Logical deletion
                        mxCell.setEnableFlag(false);
                        mxCell.setLastUpdateDttm(new Date());
                        mxCell.setLastUpdateUser(username);
                    }
                    // Fill in the modified list
                    updateMxCellList.add(mxCell);
                }
            }
            if (null != updateMxCellList && updateMxCellList.size() > 0) {
                for (MxCell mxCell : updateMxCellList) {
                    if (null != mxCell) {
                        MxGeometry mxGeometry = mxCell.getMxGeometry();
                        // save mxCell
                        mxCell = mxCellDomain.saveOrUpdate(mxCell);
                        if (null != mxGeometry) {
                            mxGeometry.setMxCell(mxCell);
                            // Take the modified save mxGeometry
                            mxGeometryDomain.saveOrUpdate(mxGeometry);
                        }
                    }
                }
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("The database mxCellList is empty and the modification failed.");
        }
        return statefulRtnBase;
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
        String errorMsg = "";
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
                                List<ProcessStop> processStopList = new ArrayList<ProcessStop>();
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
                                            List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();
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
                                List<ProcessPath> processPathList = new ArrayList<ProcessPath>();
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
                    errorMsg = (String) stringObjectMap.get("errorMsg");
                    rtnMap.put("errorMsg", errorMsg);
                    logger.warn(errorMsg);
                }
            } else {
                errorMsg = "Flow with FlowGroupId" + flowGroupId + "was not queried";
                rtnMap.put("errorMsg", errorMsg);
                logger.warn(errorMsg);
            }
        } else {
            errorMsg = "FlowGroupId is null";
            rtnMap.put("errorMsg", errorMsg);
            logger.warn(errorMsg);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public int deleteFLowGroupInfo(String id) {
        int deleteFLowInfo = 0;
        if (StringUtils.isNotBlank(id)) {
            deleteFLowInfo = flowGroupDomain.updateEnableFlagById(id, false);
            if (deleteFLowInfo > 0) {
            }
        }
        return deleteFLowInfo;
    }

}
