package cn.cnic.component.flow.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowGroupDomain;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.entity.FlowGroupPaths;
import cn.cnic.component.flow.service.IFlowGroupPathsService;
import cn.cnic.component.flow.vo.FlowGroupPathsVo;
import cn.cnic.component.flow.vo.FlowGroupVo;


@Service
public class FlowGroupPathsServiceImpl implements IFlowGroupPathsService {

    private Logger logger = LoggerUtil.getLogger();

    private final FlowGroupDomain flowGroupDomain;
    private final FlowDomain flowDomain;

    @Autowired
    public FlowGroupPathsServiceImpl(FlowGroupDomain flowGroupDomain,
                                     FlowDomain flowDomain) {
        this.flowGroupDomain = flowGroupDomain;
        this.flowDomain = flowDomain;
    }


    @Override
    public String queryPathInfoFlowGroup(String flowGroupId, String pageId) {
        if (StringUtils.isBlank(flowGroupId) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        List<FlowGroupPaths> flowGroupPathsList = flowGroupDomain.getFlowGroupPaths(flowGroupId, pageId, null, null);
        if (null == flowGroupPathsList || flowGroupPathsList.size() <= 0 || null == flowGroupPathsList.get(0)) {
            rtnMap.put("errorMsg", "No'paths'information was queried");
            logger.warn("No'paths'information was queried");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroupPaths flowGroupPaths = flowGroupPathsList.get(0);
        String fromName = null;
        String toName = null;
        if (StringUtils.isNotBlank(flowGroupPaths.getFrom()) && StringUtils.isNotBlank(flowGroupPaths.getTo())) {
            fromName = flowDomain.getFlowNameByPageId(flowGroupId, flowGroupPaths.getFrom());
            if (StringUtils.isBlank(fromName)) {
                fromName = flowGroupDomain.getFlowGroupNameByPageId(flowGroupId, flowGroupPaths.getFrom());
            }
            toName = flowDomain.getFlowNameByPageId(flowGroupId, flowGroupPaths.getTo());
            if (StringUtils.isBlank(toName)) {
                toName = flowGroupDomain.getFlowGroupNameByPageId(flowGroupId, flowGroupPaths.getTo());
            }
        }
        FlowGroupPathsVo flowGroupPathsVo = new FlowGroupPathsVo();
        BeanUtils.copyProperties(flowGroupPaths, flowGroupPathsVo);
        FlowGroup flowGroup = flowGroupPaths.getFlowGroup();
        if (null != flowGroup) {
            FlowGroupVo flowGroupVo = new FlowGroupVo();
            BeanUtils.copyProperties(flowGroup, flowGroupVo);
            flowGroupPathsVo.setFlowGroupVo(flowGroupVo);
        }
        flowGroupPathsVo.setFlowFrom(fromName);
        flowGroupPathsVo.setFlowTo(toName);
        if (StringUtils.isBlank(flowGroupPathsVo.getInport())) {
            flowGroupPathsVo.setInport("default");
        }
        if (StringUtils.isBlank(flowGroupPathsVo.getOutport())) {
            flowGroupPathsVo.setOutport("default");
        }
        rtnMap.put("code", 200);
        rtnMap.put("queryInfo", flowGroupPathsVo);
        rtnMap.put("errorMsg", "Success");
        return JsonUtils.toJsonNoException(rtnMap);

    }
}
