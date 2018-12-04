package com.nature.third.inf;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;

public interface IGetFlowInfo {

    public ThirdFlowInfoVo getFlowInfo(String appid);

    public FlowInfoDb AddFlowInfo(String appId, Flow flow);
}
