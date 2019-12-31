package com.nature.component.flow.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.PortType;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.*;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.utils.MxGraphModelUtil;
import com.nature.component.flow.utils.PathsUtil;
import com.nature.component.flow.utils.StopsUtil;
import com.nature.component.flow.vo.FlowVo;
import com.nature.component.flow.vo.PathsVo;
import com.nature.component.flow.vo.StopsVo;
import com.nature.component.group.model.PropertyTemplate;
import com.nature.component.group.model.StopsTemplate;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.process.model.Process;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.FlowGroupDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.mapper.flow.*;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.mapper.mxGraph.MxGeometryMapper;
import com.nature.mapper.mxGraph.MxGraphModelMapper;
import com.nature.third.service.IFlow;
import com.nature.transaction.process.ProcessTransaction;
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
@Transactional
public class FlowServiceImpl implements IFlowService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private StopsTemplateMapper stopsTemplateMapper;

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

    @Autowired
    private ProcessTransaction processTransaction;

    @Autowired
    private IFlow flowImpl;

    @Autowired
    private FlowDomain flowDomain;

    @Autowired
    private FlowGroupDomain flowGroupDomain;

    /**
     * Add a flow to the database
     *
     * @param mxGraphModelVo
     * @param flowId
     * @param operType
     * @return
     */
    @Override
    @Transactional
    public StatefulRtnBase saveOrUpdateFlowAll(MxGraphModelVo mxGraphModelVo, String flowId, String operType, boolean flag) {
        // Parameter null
        if (StringUtils.isAnyEmpty(flowId, operType)) {
            logger.warn("The incoming parameter flowId or operType is empty and the save failed.");
            return StatefulRtnBaseUtils.setFailedMsg("The incoming parameter flowId or operType is empty and the save failed.");
        }
        // mxGraphModelVo Parameter null
        if (null == mxGraphModelVo) {
            logger.warn("The passed parameter mxGraphModelVo is empty and the save failed.");
            return StatefulRtnBaseUtils.setFailedMsg("The passed parameter mxGraphModelVo is empty and the save failed.");
        }
        if ("ADD".equals(operType)) {
            logger.info("ADD Operation begins");
            return this.addFlowStops(mxGraphModelVo, flowId, flag);
        } else if ("MOVED".equals(operType)) {
            logger.info("MOVED Operation begins");
            return this.updateMxGraph(mxGraphModelVo, flowId);
        } else if ("REMOVED".equals(operType)) {
            logger.info("REMOVED Operation begins");
            return this.updateFlow(mxGraphModelVo, flowId);
        } else {
            logger.warn("Can't find operType: " + operType + " type ");
            return StatefulRtnBaseUtils.setFailedMsg("Can't find operType:" + operType + " type ");
        }
    }

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     * @author Nature
     */
    @Override
    public Flow getFlowById(String id) {
        Flow flowById = flowMapper.getFlowById(id);
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (null != flowById && !isAdmin) {
            Boolean isExample = flowById.getIsExample();
            String crtUser = flowById.getCrtUser();
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            String username = currentUser.getUsername();
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
        }
        return flowVo;
    }

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     * @author Nature
     */
    @Override
    @Transactional
    public String getFlowVoById(String id) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        FlowVo flowVo = null;
        Flow flowById = flowDomain.getFlowById(id);
        if (null != flowById) {
            flowVo = new FlowVo();
            BeanUtils.copyProperties(flowById, flowVo);
            //Take out 'mxGraphModel' and convert to Vo
            MxGraphModelVo mxGraphModelVo = MxGraphModelUtil.mxGraphModelPoToVo(flowById.getMxGraphModel());
            //Take out 'stopsList' and turn it to Vo
            List<StopsVo> stopsVoList = StopsUtil.stopsListPoToVo(flowById.getStopsList());
            //Take out 'pathsList' and turn it to Vo
            List<PathsVo> pathsVoList = PathsUtil.pathsListPoToVo(flowById.getPathsList());
            //Take out 'flowInfoDb' and turn it to Vo
            //FlowInfoDbVo flowInfoDbVo = FlowInfoDbUtil.flowInfoDbToVo(flowById.getAppId());
            flowVo.setMxGraphModelVo(mxGraphModelVo);
            flowVo.setStopsVoList(stopsVoList);
            flowVo.setPathsVoList(pathsVoList);
        }
        rtnMap.put("code", 200);
        rtnMap.put("flow", flowVo);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * save appId
     *
     * @param flowId
     * @param appId
     * @return
     */
    @Override
    @Transactional
    public StatefulRtnBase saveAppId(String flowId, FlowInfoDb appId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase satefulRtnBase = new StatefulRtnBase();
        if (StringUtils.isNotBlank(flowId)) {
            // find flow by flowId
            Flow flowById = flowDomain.getFlowById(flowId);
            //FlowInfoDb oldAppId = flowById.getAppId();
            if (null != flowById) {
                flowById.setAppId(appId);
                flowById.setLastUpdateDttm(new Date());
                flowById.setLastUpdateUser(username);
                flowDomain.saveOrUpdate(flowById);
                satefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("AppId failed to save");
            } else {
                satefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Can't find 'flow' with ‘flowId’ as " + flowId + " , save failed");
            }
        } else {
            satefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("flowId is null,save failed");
        }
        return satefulRtnBase;
    }

    @Override
    @Transactional
    public String addFlow(FlowVo flowVo, UserVo user) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        int optDataCount = 0;
        if (null != flowVo) {
            String username = (null != user) ? user.getUsername() : "-1";
            Flow flow = new Flow();

            BeanUtils.copyProperties(flowVo, flow);
            String id = SqlUtils.getUUID32();
            flow.setId(id);
            flow.setCrtDttm(new Date());
            flow.setCrtUser(username);
            flow.setLastUpdateDttm(new Date());
            flow.setLastUpdateUser(username);
            flow.setEnableFlag(true);
            flow.setUuid(id);
            int addFlow = flowMapper.addFlow(flow);
            if (addFlow > 0) {
                optDataCount = addFlow;
                MxGraphModel mxGraphModel = new MxGraphModel();
                mxGraphModel.setFlow(flow);
                mxGraphModel.setId(SqlUtils.getUUID32());
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

            }

            if (optDataCount > 0) {
                rtnMap.put("code", 200);
                rtnMap.put("flowId", id);
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public int updateFlow(Flow flow, UserVo user) {
        String username = (null != user) ? user.getUsername() : "-1";
        String id = flow.getId();
        flow.setId(id);
        flow.setName(flow.getName());
        flow.setDescription(flow.getDescription());
        flow.setLastUpdateDttm(new Date());
        flow.setLastUpdateUser(username);
        Flow flowDb = flowMapper.getFlowById(flow.getId());
        flow.setVersion(flowDb.getVersion());
        return flowMapper.updateFlow(flow);
    }

    @Override
    public int deleteFLowInfo(String id) {
        int deleteFLowInfo = 0;
        if (StringUtils.isNotBlank(id)) {
            Flow flowById = this.getFlowById(id);
            if (null != flowById) {
                if (null != flowById.getStopsList())
                    //Loop delete stop attribute
                    for (Stops stopId : flowById.getStopsList()) {
                        if (null != stopId.getProperties())
                            for (Property property : stopId.getProperties()) {
                                propertyMapper.updateEnableFlagByStopId(property.getId());
                            }
                    }
                // remove stop
                stopsMapper.updateEnableFlagByFlowId(flowById.getId());
                // remove paths
                pathsMapper.updateEnableFlagByFlowId(flowById.getId());
                // remove FLowInfo
                if (flowById.getAppId() != null) {
                    //flowInfoDbServiceImpl.deleteFlowInfoById(flowById.getAppId().getId());
                }
                if (null != flowById.getMxGraphModel()) {
                    List<MxCell> root = flowById.getMxGraphModel().getRoot();
                    if (null != root && !root.isEmpty()) {
                        for (MxCell mxcell : root) {
                            if (mxcell.getMxGeometry() != null) {
                                logger.info(mxcell.getMxGeometry().getId());
                                mxGeometryMapper.updateEnableFlagById(mxcell.getMxGeometry().getId());
                            }
                            mxCellMapper.updateEnableFlagById(mxcell.getId());

                        }
                    }
                    mxGraphModelMapper.updateEnableFlagByFlowId(flowById.getId());
                }
                // remove FLow
                deleteFLowInfo = flowMapper.updateEnableFlagById(id);
            }
        }
        return deleteFLowInfo;
    }

    /**
     * Add stops and artboard mxCell
     *
     * @param mxGraphModelVo
     * @param flowId
     * @param flag           Whether to add stop information
     * @return
     */
    @SuppressWarnings("unchecked")
	private StatefulRtnBase addFlowStops(MxGraphModelVo mxGraphModelVo, String flowId, boolean flag) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            logger.warn("illegal user");
            return StatefulRtnBaseUtils.setFailedMsg("illegal user");
        }
        // Query flow by flowId
        Flow flow = flowMapper.getFlowById(flowId);
        if (null == flow) {
            logger.warn("Flow information with flowId: " + flowId + " is not queried");
            return StatefulRtnBaseUtils.setFailedMsg("Flow information with flowId: " + flowId + " is not queried");
        }
        // Determine if 'mxGraphModelVo' and 'flow' are empty
        if (null == mxGraphModelVo || null == flow) {
            logger.warn("The passed parameter mxGraphModelVo is empty or the flow does not exist and the addition fails.");
            return StatefulRtnBaseUtils.setFailedMsg("The passed parameter mxGraphModelVo is empty or the flow does not exist and the addition fails.");
        }
        String username = currentUser.getUsername();
        // update flow
        flow.setLastUpdateDttm(new Date()); // last update time
        flow.setLastUpdateUser(username);// last update user
        flow.setEnableFlag(true);// is it effective
        // update flow info
        int updateFlowNum = flowMapper.updateFlow(flow);
        if (updateFlowNum <= 0) {
            logger.warn("flow update failed, update failed");
            return StatefulRtnBaseUtils.setFailedMsg("flow update failed, update failed");
        }
        //Take out the drawing board of the data inventory
        MxGraphModel mxGraphModelDb = flow.getMxGraphModel();
        //Determine if the artboard of the data inventory exists
        if (null == mxGraphModelDb) {
            logger.warn("Database without artboard, adding failed");
            return StatefulRtnBaseUtils.setFailedMsg("Database without artboard, adding failed");
        }
        // Put the page's artboard information into the database canvas
        //Copy the value from 'mxGraphModelVo' to 'mxGraphModelDb'
        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModelDb);
        mxGraphModelDb.setEnableFlag(true);
        mxGraphModelDb.setLastUpdateUser(username);
        mxGraphModelDb.setLastUpdateDttm(new Date());
        mxGraphModelDb.setFlow(flow);
        //The flow's flow foreign key has no change, no need to update
        //mxGraphModelDb.setFlow(flow); Add a foreign key
        // update mxGraphModel
        int updateMxGraphModel = mxGraphModelMapper.updateMxGraphModel(mxGraphModelDb);
        //Determine if mxGraphModelDb is updated successfully
        if (updateMxGraphModel <= 0) {
            logger.warn("flow update failed, update failed");
            return StatefulRtnBaseUtils.setFailedMsg("mxGraphModel update failed, update failed");
        }
        //The data passed from the page mxCellVoList
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
        if (mxCellVoMap.size() <= 0) {
            logger.warn("No data can be added, the addition failed");
            return StatefulRtnBaseUtils.setFailedMsg("No data can be added, the addition failed");
        }
        // Convert MxCellVo map to MxCellVoList
        List<MxCellVo> addMxCellVoList = new ArrayList<MxCellVo>(mxCellVoMap.values());
        if (null == addMxCellVoList || addMxCellVoList.size() <= 0) {
            logger.warn("No data can be added, the addition failed");
            return StatefulRtnBaseUtils.setFailedMsg("No data can be added, the addition failed");
        }
        for (MxCellVo mxCellVo : addMxCellVoList) {
            if (null != mxCellVo) {
                String stopByNameAndFlowId = stopsMapper.getStopByNameAndFlowId(flow.getId(), mxCellVo.getValue());
                // save MxCell
                // new
                MxCell mxCell = new MxCell();
                // Copy the value in mxCellVo to mxCell
                BeanUtils.copyProperties(mxCellVo, mxCell);
                if (StringUtils.isNotBlank(stopByNameAndFlowId)) {
                    mxCell.setValue(mxCellVo.getValue() + mxCellVo.getPageId());
                }
                // 'mxCell' basic properties (required when creating)
                mxCell.setId(SqlUtils.getUUID32());
                mxCell.setCrtDttm(new Date());
                mxCell.setCrtUser(username);
                // mxCell basic properties
                mxCell.setEnableFlag(true);
                mxCell.setLastUpdateUser(username);
                mxCell.setLastUpdateDttm(new Date());
                // mxGraphModel foreign key
                mxCell.setMxGraphModel(mxGraphModelDb);
                // save mxCell
                mxCellMapper.addMxCell(mxCell);
                MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                if (null != mxGeometryVo) {
                    // save MxGeometry
                    // new
                    MxGeometry mxGeometry = new MxGeometry();
                    // Copy the value in mxGeometryVo to mxGeometry
                    BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
                    // mxGeometry basic properties(required when creating)
                    mxGeometry.setId(SqlUtils.getUUID32());
                    mxGeometry.setCrtDttm(new Date());
                    mxGeometry.setCrtUser(username);
                    // setmxGraphModel basic properties
                    mxGeometry.setEnableFlag(true);
                    mxGeometry.setLastUpdateUser(username);
                    mxGeometry.setLastUpdateDttm(new Date());
                    // mxCell foreign key
                    mxGeometry.setMxCell(mxCell);
                    // save mxGeometry
                    mxGeometryMapper.addMxGeometry(mxGeometry);
                }

            }
        }

        //Separate 'stops' from 'path' in addMxCellVoList
        Map<String, Object> stopsPathsMap = MxGraphModelUtil.distinguishElementsPaths(addMxCellVoList);

        // Take mxCellVoList(stopsList) from the Map
        List<MxCellVo> objectStops = (ArrayList<MxCellVo>) stopsPathsMap.get("elements");

        // stops list
        List<Stops> addStopsList = new ArrayList<Stops>();
        // Generate a stopList based on the contents of the MxCellList
        addStopsList = this.mxCellVoListToStopsList(objectStops, flow);
        // save addStopsList
        if (flag) {
            this.addStopList(addStopsList);
        }

        // Take mxCellVoList(pathsList) from the Map
        List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) stopsPathsMap.get("paths");

        // path list
        List<Paths> addPathsList = new ArrayList<Paths>();

        // Generate a pathsList based on the contents of the MxCellList
        addPathsList = MxGraphModelUtil.mxCellVoListToPathsList(objectPaths, flow);
        if (flag) {
            // Judge empty
            if (null != addPathsList && addPathsList.size() > 0) {
                // save addPathsList
                int addPathsListNum = pathsMapper.addPathsList(addPathsList);
                if (addPathsListNum <= 0) {
                    logger.error("addPathsList Save failed flow：flowId(" + flow.getId() + ")", new Exception("addPathsList save failed"));
                }
            } else {
                logger.info("addPathsList is null,do not save");
            }
        }
        return statefulRtnBase;

    }

    /**
     * Modification of the artboard
     *
     * @param mxGraphModelVo
     * @param flowId
     * @return
     */
    private StatefulRtnBase updateMxGraph(MxGraphModelVo mxGraphModelVo, String flowId) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            logger.warn("illegal user");
            return StatefulRtnBaseUtils.setFailedMsg("illegal user");
        }
        MxGraphModel mxGraphModel = mxGraphModelMapper.getMxGraphModelByFlowId(flowId);
        // Determine if the incoming data is empty
        if (null == mxGraphModelVo) {
            return StatefulRtnBaseUtils.setFailedMsg("mxGraphModelVo is null, fail to edit");
        }
        // Determine if the database data to be modified is empty
        if (null == mxGraphModel) {
            return StatefulRtnBaseUtils.setFailedMsg("The mxGraphModel information with flowId is: " + flowId + " is not found, fail to edit");
        }
        String username = currentUser.getUsername();
        // Copy the value from mxGraphModelVo to mxGraphModelDb
        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
        // setmxGraphModel basic attribute
        mxGraphModel.setLastUpdateUser(username);// last update user
        mxGraphModel.setLastUpdateDttm(new Date());// last update time
        mxGraphModel.setEnableFlag(true);// is it effective
        // save MxGraphModel
        int updateMxGraphModel = mxGraphModelMapper.updateMxGraphModel(mxGraphModel);
        // Determine if mxGraphModel is saved successfully
        if (updateMxGraphModel <= 0) {
            return StatefulRtnBaseUtils.setFailedMsg("save failed");
        }
        // The data passed from the page MxCellVo
        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
        // Take out the MxCellList information queried by the database.
        List<MxCell> mxCellList = mxGraphModel.getRoot();
        // Save and process mxCellList
        return this.updateMxCellList(mxCellVoList, mxCellList);
    }

    /**
     * Modify Flow
     *
     * @param mxGraphModelVo Information from the page
     * @param flowId         The data to be modified
     * @return
     */
    @SuppressWarnings("unchecked")
	private StatefulRtnBase updateFlow(MxGraphModelVo mxGraphModelVo, String flowId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        Flow flow = flowMapper.getFlowById(flowId);
        if (null != flow) {
            if (null != mxGraphModelVo) {
                // last update time
                flow.setLastUpdateDttm(new Date());
                // last update user
                flow.setLastUpdateUser(username);

                // save flow
                int updateFlow = flowMapper.updateFlow(flow);
                // Determine whether the save is successful
                if (updateFlow > 0) {
                    // Save and modify the artboard information
                    StatefulRtnBase updateMxGraphRtn = this.updateMxGraph(mxGraphModelVo, flowId);
                    // Determine if mxGraphModel is saved successfully
                    if (null != updateMxGraphRtn && updateMxGraphRtn.isReqRtnStatus()) {
                        // Page of MxCellVo's list
                        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();

                        // Separate the stops and path in mxCellVoList
                        Map<String, Object> stopsPathsMap = MxGraphModelUtil.distinguishElementsPaths(mxCellVoList);

                        if (null != stopsPathsMap) {
                            // Need to delete the path
                            // Key is from and to (that is, the pageId of stop) value is inport and outport
                            Map<String, String> pathsDelInfoMap = new HashMap<String, String>();

                            // Get out the PathsList stored in the database
                            List<Paths> pathsList = flow.getPathsList();
                            // Determine whether the list of lines in the database is empty, do not empty to continue the judgment operation below, or directly add
                            if (null != pathsList && pathsList.size() > 0) {
                                // Take the line of mxCellVoList
                                List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) stopsPathsMap.get("paths");
                                // Key is the PageId of PathsVo (the id in the artboard), and the value is Paths
                                Map<String, MxCellVo> objectPathsMap = new HashMap<String, MxCellVo>();

                                // The pathsList to be modified
                                List<Paths> updatePaths = new ArrayList<Paths>();

                                //Determine if the objectStops passed from the page is empty.
                                if (null != objectPaths) {
                                    for (MxCellVo mxCellVo : objectPaths) {
                                        if (null != mxCellVo) {
                                            objectPathsMap.put(mxCellVo.getPageId(), mxCellVo);
                                        }
                                    }
                                }

                                // The data pathsList of the loop database is retrieved by using the pageId in the stops to convert the map to the value of the page passed by the map.
                                for (Paths paths : pathsList) {
                                    if (null != paths) {
                                        String pageId = paths.getPageId();
                                        MxCellVo mxCellVo = objectPathsMap.get(pageId);
                                        // If you get it, you need to modify it. Otherwise, it is to be deleted.
                                        if (null != mxCellVo) {
                                            //There is no operation to modify the stop information when you operate the artboard. You can modify the properties, but save it immediately. No operation is required here.
                                        } else {
                                            paths.setEnableFlag(false);
                                            paths.setLastUpdateUser(username);
                                            paths.setLastUpdateDttm(new Date());
                                            paths.setFlow(flow);
                                            updatePaths.add(paths);
                                            // Put the port information that needs to be logically deleted into the map of pathsDelInfoMap
                                            pathsDelInfoMap.put("in" + paths.getTo(), paths.getInport());
                                            pathsDelInfoMap.put("out" + paths.getFrom(), paths.getOutport());
                                        }
                                    }
                                }
                                // Modify save updateStops
                                for (Paths paths : updatePaths) {
                                    if (null != paths) {
                                        pathsMapper.updatePaths(paths);
                                    }
                                }
                            } else {
                                //The stopsList in the database is empty and the modification failed.
                                logger.info("The pathsList in the database is empty");
                            }


                            // Get out the Stopslist stored in the database
                            List<Stops> stopsList = flow.getStopsList();

                            // If the list of Stops in the database is empty, the modification fails because this method only processes the modification and is not responsible for adding
                            if (null != stopsList && stopsList.size() > 0) {
                                // Take the mxCellVoList of stops
                                List<MxCellVo> objectStops = (ArrayList<MxCellVo>) stopsPathsMap.get("elements");
                                // Key is the PageId of mxCellVo (the id in the artboard), and the value is mxCellVo
                                Map<String, MxCellVo> objectStopsMap = new HashMap<String, MxCellVo>();

                                // Stopslist to be modified
                                List<Stops> updateStops = new ArrayList<Stops>();

                                // Determine if the objectStops passed from the page is empty.
                                if (null != objectStops) {
                                    // Loop converts objectStops (page data) to objectStopsMap
                                    for (MxCellVo mxCellVo : objectStops) {
                                        if (null != mxCellVo) {
                                            objectStopsMap.put(mxCellVo.getPageId(), mxCellVo);
                                        }
                                    }
                                }
                                // The data stopsList of the loop database, using the pageId in the stops to convert to the map of the value of the page after the map is fetched,
                                for (Stops stops : stopsList) {
                                    if (null != stops) {
                                        String pageId = stops.getPageId();
                                        MxCellVo mxCellVo = objectStopsMap.get(pageId);
                                        // If you get it, you need to modify it. Otherwise, it is to be deleted.
                                        if (null != mxCellVo) {
                                            //When you operate the artboard, when you delete the line, you need to determine whether there is an Any type stop at both ends of the line. If there is any corresponding port information in the attribute, delete the corresponding port information.
                                            // Determine if the stops have an Any port
                                            if (stops.getInPortType() == PortType.ANY || stops.getOutPortType() == PortType.ANY) {
                                                // Value in the pathsDelInfoMap according to the pageId
                                                String inprot = pathsDelInfoMap.get("in" + stops.getPageId());
                                                String outprot = pathsDelInfoMap.get("out" + stops.getPageId());
                                                // If inprot or outprot has a value, the loop attribute finds the attribute of the corresponding storage port and modifies it.
                                                if (StringUtils.isNotBlank(inprot) || StringUtils.isNotBlank(outprot)) {
                                                    // Take out the attribute list
                                                    List<Property> properties = stops.getProperties();
                                                    // Judge
                                                    if (null != properties && properties.size() > 0) {
                                                        Property inportProperty = null;
                                                        Property outportProperty = null;
                                                        List<Property> propertiesNew = new ArrayList<Property>();
                                                        // Loop attribute
                                                        for (Property property : properties) {
                                                            property.setStops(stops);
                                                            if ("inports".equals(property.getName())) {
                                                                inportProperty = property;
                                                                continue;
                                                            } else if ("outports".equals(property.getName())) {
                                                                outportProperty = property;
                                                                continue;
                                                            }
                                                            propertiesNew.add(property);
                                                        }
                                                        boolean isUpdate = false;
                                                        if (null != inportProperty && StringUtils.isNotBlank(inprot)) {
                                                            inportProperty = replaceProtValue(inprot, inportProperty);
                                                            propertiesNew.add(inportProperty);
                                                            isUpdate = true;
                                                        }
                                                        if (null != outportProperty && StringUtils.isNotBlank(outprot)) {
                                                            outportProperty = replaceProtValue(outprot, outportProperty);
                                                            propertiesNew.add(outportProperty);
                                                            isUpdate = true;
                                                        }
                                                        stops.setProperties(propertiesNew);
                                                        if (isUpdate) {
                                                            stops.setLastUpdateDttm(new Date());//Last update time
                                                            stops.setLastUpdateUser(username);//Last update user
                                                            stops.setProperties(propertiesNew);//Attribute list
                                                            stops.setFlow(flow);
                                                            updateStops.add(stops);
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            stops.setEnableFlag(false);//Tombstone ID
                                            stops.setLastUpdateDttm(new Date());//
                                            stops.setLastUpdateUser(username);//Last update user
                                            //stops property
                                            List<Property> properties = stops.getProperties();
                                            // Whether the judgment is empty
                                            if (null != properties) {
                                                List<Property> propertyList = new ArrayList<Property>();
                                                // Loop tombstone properties
                                                for (Property property : properties) {
                                                    if (null != property) {
                                                        property.setEnableFlag(false);//Tombstone ID
                                                        property.setLastUpdateDttm(new Date());//Last update time
                                                        property.setLastUpdateUser(username);//Last update user
                                                        propertyList.add(property);
                                                    }
                                                }
                                                stops.setProperties(propertyList);
                                            }
                                            updateStops.add(stops);
                                        }
                                    }
                                }
                                // Modify save updateStops
                                for (Stops stops : updateStops) {
                                    if (null != stops) {
                                        int updateStopsNum = stopsMapper.updateStops(stops);
                                        if (updateStopsNum > 0) {
                                            List<Property> properties = stops.getProperties();
                                            for (Property property : properties) {
                                                propertyMapper.updateStopsProperty(property);
                                            }
                                        }
                                    }
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
                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Modify save failed flow");
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("The flowId cannot find the corresponding flow, and the modification fails.");
        }
        return statefulRtnBase;
    }

    /**
     * Replace port properties
     *
     * @param prot
     * @param property
     * @return
     */
    private Property replaceProtValue(String prot, Property property) {
        String customValue = property.getCustomValue();
        if (null != customValue) {
            if (customValue.contains(prot + ",")) {
                customValue = customValue.replace(prot + ",", "");
            } else if (customValue.contains("," + prot)) {
                customValue = customValue.replace("," + prot, "");
            } else if (customValue.contains(prot)) {
                customValue = customValue.replace(prot, "");
            }
            property.setCustomValue(customValue);
        }
        return property;
    }

    /**
     * Generate a list of 'stops' based on the contents of 'MxCellVoList'
     *
     * @param objectStops
     * @param flow
     * @return
     */
    private List<Stops> mxCellVoListToStopsList(List<MxCellVo> objectStops, Flow flow) {
        List<Stops> stopsList = null;
        if (null != objectStops && objectStops.size() > 0) {
            stopsList = new ArrayList<Stops>();
            // loop objectStops
            for (MxCellVo mxCellVo : objectStops) {
                Stops stops = this.stopsTemplateToStops(mxCellVo);
                if (null != stops) {
                    String stopByNameAndFlowId = stopsMapper.getStopByNameAndFlowId(flow.getId(), stops.getName());
                    if (StringUtils.isNotBlank(stopByNameAndFlowId)) {
                        stops.setName(stops.getName() + stops.getPageId());
                    }
                    stops.setFlow(flow);
                    stopsList.add(stops);
                }
            }
        }
        return stopsList;
    }

    /**
     * mxCellVo to stops
     *
     * @param mxCellVo
     * @return
     */
    private Stops stopsTemplateToStops(MxCellVo mxCellVo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Stops stops = null;
        if (null != mxCellVo) {
            // Take out the style attribute (the name of the stop in the attribute)
            // Example of the style attribute of stops
            // (image;html=1;labelBackgroundColor=#ffffff;image=/grapheditor/stencils/clipart/test_stops_1_128x128.png)
            // What we need is "test_stops_1"
            String style = mxCellVo.getStyle();
            if (StringUtils.isNotBlank(style)) {
                // Determine whether it is a stop by intercepting keywords
                String[] split = style.split("_128x128.");
                // Think of stops when there is one and only one keyword ("_128x128.")
                if (null != split && split.length == 2) {
                    // Take the first bit of the array and continue to intercept
                    String string = split[0];
                    String[] split2 = string.split("/");
                    // Empty, take the last bit of the array (the name of the stops)
                    if (null != split2 && split2.length > 0) {
                        // Get the name of the stops
                        String stopsName = split2[split2.length - 1];
                        // Query the stops template according to the name of the stops
                        StopsTemplate stopsTemplate = this.getStopsTemplate(stopsName);
                        // Whether to judge whether the template is empty
                        if (null != stopsTemplate) {
                            stops = new Stops();
                            BeanUtils.copyProperties(stopsTemplate, stops);
                            stops.setCrtDttm(new Date());
                            stops.setCrtUser(username);
                            stops.setLastUpdateDttm(new Date());
                            stops.setLastUpdateUser(username);
                            stops.setEnableFlag(true);
                            stops.setId(SqlUtils.getUUID32());
                            stops.setPageId(mxCellVo.getPageId());
                            List<Property> propertiesList = null;
                            List<PropertyTemplate> propertiesTemplateList = stopsTemplate.getProperties();
                            if (null != propertiesTemplateList && propertiesTemplateList.size() > 0) {
                                propertiesList = new ArrayList<Property>();
                                for (PropertyTemplate propertyTemplate : propertiesTemplateList) {
                                    Property property = new Property();
                                    BeanUtils.copyProperties(propertyTemplate, property);
                                    property.setId(SqlUtils.getUUID32());
                                    property.setCrtDttm(new Date());
                                    property.setCrtUser(username);
                                    property.setLastUpdateDttm(new Date());
                                    property.setLastUpdateUser(username);
                                    property.setEnableFlag(true);
                                    property.setStops(stops);
                                    property.setCustomValue(propertyTemplate.getDefaultValue());
                                    //Indicates "select"
                                    if (propertyTemplate.getAllowableValues().contains(",") && propertyTemplate.getAllowableValues().length() > 4) {
                                        property.setIsSelect(true);
                                        //Determine if there is a default value in "select"
                                        if (!propertyTemplate.getAllowableValues().contains(propertyTemplate.getDefaultValue())) {
                                            //Default value if not present
                                            property.setCustomValue("");
                                        }
                                    } else {
                                        property.setIsSelect(false);
                                    }
                                    propertiesList.add(property);
                                }
                            }
                            stops.setProperties(propertiesList);
                        }
                    }
                }
            }
        }
        return stops;
    }

    /**
     * Query 'stops' according to 'stopsName'
     *
     * @param stopsName
     * @return
     */
    private StopsTemplate getStopsTemplate(String stopsName) {
        StopsTemplate stopsTemplate = null;
        // Query the stops template according to the name of the stops
        List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsName);
        if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
            stopsTemplate = stopsTemplateList.get(0);
        }
        return stopsTemplate;
    }

    /**
     * Save and process mxCellList
     *
     * @param mxCellVoList
     * @param mxCellList
     * @return
     */
    private StatefulRtnBase updateMxCellList(List<MxCellVo> mxCellVoList, List<MxCell> mxCellList) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();

        // If the mxCellList is empty, the modification fails because this method only processes the modifications and is not responsible for adding
        if (null != mxCellList && mxCellList.size() > 0) {

            // Including modified and tombstoned
            List<MxCell> updateMxCellList = new ArrayList<MxCell>();
            // Convert the list passed to the page to map key for pageId
            Map<String, MxCellVo> mxCellVoMap = new HashMap<String, MxCellVo>();
            // Determine if it is empty
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
                        // mxCell basic properties
                        mxCell.setEnableFlag(true);// Tombstone ID
                        mxCell.setLastUpdateUser(username);// last update user
                        mxCell.setLastUpdateDttm(new Date());// last update time

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
                                mxGeometry.setLastUpdateUser(username);// last update user
                                mxGeometry.setLastUpdateDttm(new Date());// last update time
                                mxGeometry.setEnableFlag(true);// Tombstone ID
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
                        // save mxCell
                        int updateMxCell = mxCellMapper.updateMxCell(mxCell);
                        if (updateMxCell > 0) {
                            MxGeometry mxGeometry = mxCell.getMxGeometry();
                            if (null != mxGeometry) {
                                // Take the modified save mxGeometry
                                int updateMxGeometry = mxGeometryMapper.updateMxGeometry(mxGeometry);
                                if (updateMxGeometry > 0) {
                                    logger.info("mxGeometry saves the success ID:" + mxGeometry.getId());
                                } else {
                                    logger.error("mxGeometryVo save failed：The corresponding mxCell's pageId(" + mxCell.getPageId() + "),mxCellValue(" + mxCell.getValue() + ")", new Exception("\"mxGeometryVo save failed"));
                                }
                            }
                        } else {
                            logger.error("mxGeometryVo save failed：pageId(" + mxCell.getPageId() + "),name(" + mxCell.getValue() + ")", new Exception("mxGeometryVo save failed"));
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
     * Save addStopsList to the database
     *
     * @param addStopsList
     */
    private void addStopList(List<Stops> addStopsList) {
        // Determine if the stopsList is empty
        if (null == addStopsList || addStopsList.size() <= 0) {
            logger.info("addStopsList is empty,do not save");
            return;
        }
        // save stopsList
        int addStopsListNum = stopsMapper.addStopsList(addStopsList);
        if (addStopsListNum <= 0) {
            logger.error("New save failed addStopsList", new Exception("New save failed addStopsList"));
            return;
        }
        for (Stops stops : addStopsList) {
            // Determine if the propertyList is empty
            List<Property> propertyList = stops.getProperties();
            // Determine if the propertyList is empty
            if (null == propertyList || propertyList.size() <= 0) {
                logger.info("addPropertyList is empty,do not save");
                continue;
            }
            // save propertyList
            int addPropertyList = propertyMapper.addPropertyList(propertyList);
            if (addPropertyList <= 0) {
                logger.error("Property save failed：stopsId(" + stops.getId() + "),stopsName(" + stops.getName() + ")", new Exception("Property save failed"));
            }
        }
    }

    @Override
    public String getMaxStopPageId(String flowId) {
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
    public String getFlowListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
            Page<Flow> page = PageHelper.startPage(offset, limit);
            flowMapper.getFlowListParam(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
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
    public String runFlow(String flowId, String runMode) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "illegal user");
            logger.info("illegal user");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isBlank(flowId)) {
            rtnMap.put("errorMsg", "FlowId is null");
            logger.warn("FlowId is null");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // find flow by flowId
        Flow flowById = this.getFlowById(flowId);
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null == flowById) {
            rtnMap.put("errorMsg", "Flow with FlowId" + flowId + "was not queried");
            logger.warn("Flow with FlowId" + flowId + "was not queried");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        Process process = ProcessUtils.flowToProcess(flowById, currentUser);
        if (null == process) {
            rtnMap.put("errorMsg", "Conversion failed");
            logger.warn("Conversion failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        RunModeType runModeType = RunModeType.RUN;
        if (StringUtils.isNotBlank(runMode)) {
            runModeType = RunModeType.selectGender(runMode);
        }
        process.setRunModeType(runModeType);
        int addProcess = processTransaction.addProcess(process);
        if (addProcess <= 0) {
            rtnMap.put("errorMsg", "Save failed, transform failed");
            logger.warn("Save failed, transform failed");
            return JsonUtils.toJsonNoException(rtnMap);
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
            processTransaction.updateProcessEnableFlag(process.getId(), currentUser);
            String errorMsg = (String) stringObjectMap.get("errorMsg");
            rtnMap.put("errorMsg", errorMsg);
            logger.warn(errorMsg);
            return JsonUtils.toJsonNoException(rtnMap);
        }
        Process processById = processTransaction.getProcessById(process.getId());
        processById.setAppId((String) stringObjectMap.get("appId"));
        processById.setProcessId((String) stringObjectMap.get("appId"));
        processById.setState(ProcessState.STARTED);
        processById.setLastUpdateUser(currentUser.getUsername());
        processById.setLastUpdateDttm(new Date());
        int updateProcess = processTransaction.updateProcess(processById);
        if (updateProcess <= 0) {
            rtnMap.put("errorMsg", "save process failed,update failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        rtnMap.put("code", 200);
        rtnMap.put("processId", process.getId());
        rtnMap.put("errorMsg", "save process success,update success");
        logger.info("save process success,update success");
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String updateFlowBaseInfo(FlowVo flowVo) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "illegal user");
            logger.info("illegal user");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (null == flowVo || StringUtils.isBlank(flowVo.getId())) {
            rtnMap.put("errorMsg", "Parameter passed error");
            logger.info("Parameter passed error");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        Flow flowById = flowDomain.getFlowById(flowVo.getId());
        if (null == flowById) {
            rtnMap.put("errorMsg", "Database save failed");
            logger.info("Database save failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        flowById.setDescription(flowVo.getDescription());
        flowById.setDriverMemory(flowVo.getDriverMemory());
        flowById.setExecutorCores(flowVo.getExecutorCores());
        flowById.setExecutorMemory(flowVo.getExecutorMemory());
        flowById.setExecutorNumber(flowVo.getExecutorNumber());
        flowById.setLastUpdateDttm(new Date());
        flowById.setLastUpdateUser(currentUser.getUsername());
        flowDomain.saveOrUpdate(flowById);
        rtnMap.put("code", 200);
        rtnMap.put("flowVo", flowVo);
        rtnMap.put("errorMsg", "successfully saved");
        logger.info("The 'Flow' attribute was successfully modified.");
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String updateFlowNameById(String id, String flowGroupId, String flowName, String pageId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        UserVo user = SessionUserUtil.getCurrentUser();
        if (null == user) {
            rtnMap.put("errorMsg", "illegal user");
            logger.info("illegal user");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        if (StringUtils.isAnyEmpty(id, flowName, flowGroupId, pageId)) {
            rtnMap.put("errorMsg", "The incoming parameter is empty");
            logger.info("The incoming parameter is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroupById) {
            rtnMap.put("errorMsg", "flow information is empty");
            logger.info("Flow query is null,flowGroupId:" + flowGroupId);
            return JsonUtils.toJsonNoException(rtnMap);
        }
        MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
        if (null == mxGraphModel) {
            rtnMap.put("errorMsg", "No flow information,update failed ");
            logger.info(flowGroupById.getId() + "No flow information,update failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        List<MxCell> root = mxGraphModel.getRoot();
        if (null == root || root.size() <= 0) {
            rtnMap.put("errorMsg", "No flow information,update failed ");
            logger.info(flowGroupById.getId() + "No flow information,update failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        //Check if name is the same name
        String checkResult = flowDomain.getFlowIdByNameAndFlowGroupId(flowGroupId, flowName);
        if (StringUtils.isNotBlank(checkResult)) {
            rtnMap.put("errorMsg", "Name already exists");
            logger.info(flowName + "The name has been repeated and the save failed.");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        boolean updateStopsNameById = this.updateFlowNameById(id, flowName);
        if (!updateStopsNameById) {
            logger.info("Modify flowName failed");
            rtnMap.put("errorMsg", "Modify flowName failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String username = user.getUsername();
        for (MxCell mxCell : root) {
            if (null != mxCell) {
                if (mxCell.getPageId().equals(pageId)) {
                    mxCell.setValue(flowName);
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
    public Boolean updateFlowNameById(String id, String flowName) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(flowName)) {
            Flow flowById = flowDomain.getFlowById(id);
            if (null != flowById) {
                flowById.setLastUpdateUser(username);
                flowById.setLastUpdateDttm(new Date());
                flowById.setName(flowName);
                flowDomain.saveOrUpdate(flowById);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMaxFlowPageIdByFlowGroupId(String flowGroupId) {
        return flowMapper.getMaxFlowPageIdByFlowGroupId(flowGroupId);
    }
}
