package com.nature.transaction.workFlow;

import com.nature.component.workFlow.model.Flow;
import com.nature.mapper.FlowMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FlowTransaction {

    @Resource
    private FlowMapper flowMapper;

    @Transient
    public Flow getFlowById(String id) {
        Flow flowById = null;
        if (StringUtils.isNotBlank(id)) {
            flowById = flowMapper.getFlowById(id);
        }
        return flowById;
    }
}
