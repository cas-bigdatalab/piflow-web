package com.nature.component.flow.service.impl;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.utils.PathsUtil;
import com.nature.component.flow.utils.StopsUtils;
import com.nature.component.flow.vo.FlowVo;
import com.nature.component.flow.vo.PathsVo;
import com.nature.component.flow.vo.StopsVo;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.utils.MxGraphModelUtils;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.process.model.Process;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.FlowGroupDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.process.ProcessDomain;
import com.nature.mapper.flow.FlowMapper;
import com.nature.mapper.flow.PathsMapper;
import com.nature.mapper.flow.PropertyMapper;
import com.nature.mapper.flow.StopsMapper;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.mapper.mxGraph.MxGeometryMapper;
import com.nature.mapper.mxGraph.MxGraphModelMapper;
import com.nature.mapper.process.ProcessMapper;
import com.nature.third.service.IFlow;
import com.nature.transaction.process.ProcessTransaction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
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
    private ProcessMapper processMapper;

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
    private ProcessTransaction processTransaction;

    @Resource
    private ProcessDomain processDomain;

    @Resource
    private IFlow flowImpl;

    @Resource
    private FlowDomain flowDomain;

    @Resource
    private FlowGroupDomain flowGroupDomain;

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
        }
        rtnMap.put("code", 200);
        rtnMap.put("flow", flowVo);
        return JsonUtils.toJsonNoException(rtnMap);
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
        Map<String, Object> rtnMap = new HashMap<>();
        if (null != offset && null != limit) {
            boolean admin = SessionUserUtil.isAdmin();
            String currentUsername = SessionUserUtil.getCurrentUsername();
            Page<Flow> flowListPage = null;
            if (admin) {
                flowListPage = flowDomain.getFlowListPage(offset - 1, limit, param);
            } else {
                flowListPage = flowDomain.getFlowListPageByUser(offset - 1, limit, param, currentUsername);
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
    public String runFlow(String flowId, String runMode) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        if (StringUtils.isBlank(currentUsername)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowId is null");
        }
        // find flow by flowId
        Flow flowById = this.getFlowById(flowId);
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow with FlowId" + flowId + "was not queried");
        }
        Process process = ProcessUtils.flowToProcess(flowById, currentUsername);
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
            process.setLastUpdateUser(currentUsername);
            processDomain.saveOrUpdate(process);
            return ReturnMapUtils.setFailedMsgRtnJsonStr((String) stringObjectMap.get("errorMsg"));
        }
        process.setAppId((String) stringObjectMap.get("appId"));
        process.setProcessId((String) stringObjectMap.get("appId"));
        process.setState(ProcessState.STARTED);
        process.setLastUpdateUser(currentUsername);
        process.setLastUpdateDttm(new Date());
        processDomain.saveOrUpdate(process);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("save process success,update success");
        rtnMap.put("processId", process.getId());
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
        boolean updateFlowNameById = this.updateFlowNameById(id, flowName);
        if (!updateFlowNameById) {
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
                    String loadXml = MxGraphUtils.mxGraphModelToMxGraphXml(mxGraphModelVo);
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
