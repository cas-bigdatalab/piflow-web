package cn.cnic.component.flow.service.impl;

import cn.cnic.base.utils.HdfsUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowStopsPublishingDomain;
import cn.cnic.component.flow.entity.*;
import cn.cnic.component.flow.service.IFlowStopsPublishingService;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.flow.utils.FlowStopsPublishingUtils;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.flow.vo.FlowStopsPublishingVo;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.mxGraph.utils.MxGraphUtils;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.process.utils.ProcessPathUtils;
import cn.cnic.component.process.utils.ProcessStopUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.controller.requestVo.RunStopsVo;
import cn.cnic.third.service.IFlow;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FlowStopsPublishingServiceImpl implements IFlowStopsPublishingService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private FlowStopsPublishingDomain flowStopsPublishingDomain;

    @Override
    public String addFlowStopsPublishing(String username, String name, String stopsIds) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(stopsIds)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopsIds"));
        }
        List<String> stopsIdList = Arrays.asList(stopsIds.split(","));;
        if (null == stopsIdList || stopsIdList.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopsIds"));
        }
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("name"));
        }
        FlowStopsPublishing flowStopsPublishing = FlowStopsPublishingUtils.flowStopsPublishingNewNoId(username);
        flowStopsPublishing.setPublishingId(UUIDUtils.getUUID32());
        flowStopsPublishing.setName(name);
        flowStopsPublishing.setStopsIds(stopsIdList);
        try {
            int affectedRows = flowStopsPublishingDomain.addFlowStopsPublishing(flowStopsPublishing);
            if (affectedRows <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.ADD_SUCCEEDED_MSG());
    }

    @Override
    public String updateFlowStopsPublishing(boolean isAdmin, String username, String publishingId, String name, List<String> stopsIds) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(publishingId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("publishingId"));
        }
        if (null == stopsIds || stopsIds.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopsIds"));
        }
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("name"));
        }
        try {
            int affectedRows = flowStopsPublishingDomain.updateFlowStopsPublishing(isAdmin, username, publishingId, name, stopsIds);
            if (affectedRows <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
    }

    @Override
    public String getFlowStopsPublishingVo(String publishingId) {
        if (StringUtils.isBlank(publishingId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("publishingId"));
        }
        List<FlowStopsPublishingVo> flowStopsPublishingVoList = flowStopsPublishingDomain.getFlowStopsPublishingVoByPublishingId(publishingId);
        if (null == flowStopsPublishingVoList || flowStopsPublishingVoList.size() <=0){
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        FlowStopsPublishingVo flowStopsPublishingVo = flowStopsPublishingVoList.get(0);
        if (null == flowStopsPublishingVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Map<String, Object> rtnData = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        rtnData.put("publishingId", flowStopsPublishingVo.getPublishingId());
        rtnData.put("name", flowStopsPublishingVo.getName());
        List<Object> stopsDataList = new ArrayList<>();
        for (FlowStopsPublishingVo flowStopsPublishingVoI : flowStopsPublishingVoList) {
            if (null == flowStopsPublishingVo || null == flowStopsPublishingVoI.getStopsVo()) {
                continue;
            }
            stopsDataList.add(flowStopsPublishingVoI.getStopsVo());
        }
        rtnData.put("stopsDataList", stopsDataList);
        return ReturnMapUtils.toFormatJson(rtnData);
    }

    @Override
    public String getFlowStopsPublishingList(String username, String flowId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(flowId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(flowId));
        }
        List<FlowStopsPublishing> flowStopsPublishingList = flowStopsPublishingDomain.getFlowStopsPublishingListByFlowId(username, flowId);
        if (null == flowStopsPublishingList || flowStopsPublishingList.size() <=0){
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Map<String, Object> rtnData = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        List<Map<String, String>> publishingDataList = new ArrayList<>();
        Map<String, String> publishingData;
        for (FlowStopsPublishing flowStopsPublishing : flowStopsPublishingList) {
            if (null == flowStopsPublishing || null == flowStopsPublishing.getPublishingId()) {
                continue;
            }
            publishingData = new HashMap<>();
            publishingData.put("name", flowStopsPublishing.getName());
            publishingData.put("publishingId", flowStopsPublishing.getPublishingId());
            publishingDataList.add(publishingData);
        }
        rtnData.put("publishingDataList", publishingDataList);
        return ReturnMapUtils.toFormatJson(rtnData);
    }
}
