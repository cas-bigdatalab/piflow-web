package com.nature.transaction.flow;

import com.nature.component.flow.model.Flow;
import com.nature.mapper.flow.FlowMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class FlowTransaction {

    @Autowired
    private FlowMapper flowMapper;

    @Transactional
    public Flow getFlowById(String id) {
        Flow flowById = null;
        if (StringUtils.isNotBlank(id)) {
            flowById = flowMapper.getFlowById(id);
        }
        return flowById;
    }
}
