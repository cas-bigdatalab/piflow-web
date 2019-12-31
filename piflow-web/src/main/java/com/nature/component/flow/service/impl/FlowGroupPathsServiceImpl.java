package com.nature.component.flow.service.impl;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.model.FlowGroupPaths;
import com.nature.component.flow.service.IFlowGroupPathsService;
import com.nature.component.flow.vo.FlowGroupPathsVo;
import com.nature.component.flow.vo.FlowGroupVo;
import com.nature.mapper.flow.FlowGroupPathsMapper;
import com.nature.mapper.flow.FlowMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowGroupPathsServiceImpl implements IFlowGroupPathsService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowGroupPathsMapper flowGroupPathsMapper;
    @Resource
    private FlowMapper flowMapper;


    @Override
    public String queryPathInfoFlowGroup(String flowGroupId, String pageId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        if (StringUtils.isBlank(flowGroupId) || StringUtils.isBlank(pageId)) {
            rtnMap.put("errorMsg", "The parameter'fid'or'id' is empty");
            logger.warn("The parameter'fid'or'id' is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }

        List<FlowGroupPaths> flowGroupPathsList = flowGroupPathsMapper.getFlowGroupPaths(flowGroupId, pageId, null, null);
        if (null == flowGroupPathsList || flowGroupPathsList.size() <= 0 || null == flowGroupPathsList.get(0)) {
            rtnMap.put("errorMsg", "No'paths'information was queried");
            logger.warn("No'paths'information was queried");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroupPaths flowGroupPaths = flowGroupPathsList.get(0);
        Flow flowFrom = null;
        Flow flowTo = null;
        if (StringUtils.isNotBlank(flowGroupPaths.getFrom()) && StringUtils.isNotBlank(flowGroupPaths.getTo())) {
            flowFrom = flowMapper.getFlowByPageId(flowGroupId, flowGroupPaths.getFrom());
            flowTo = flowMapper.getFlowByPageId(flowGroupId, flowGroupPaths.getTo());
        }
        FlowGroupPathsVo flowGroupPathsVo = new FlowGroupPathsVo();
        BeanUtils.copyProperties(flowGroupPaths, flowGroupPathsVo);
        FlowGroup flowGroup = flowGroupPaths.getFlowGroup();
        if (null != flowGroup) {
            FlowGroupVo flowGroupVo = new FlowGroupVo();
            BeanUtils.copyProperties(flowGroup, flowGroupVo);
            flowGroupPathsVo.setFlowGroupVo(flowGroupVo);
        }
        if (null != flowFrom) {
            flowGroupPathsVo.setFlowFrom(flowFrom.getName());
        }
        if (null != flowTo) {
            flowGroupPathsVo.setFlowTo(flowTo.getName());
        }
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
