package com.nature.third.inf;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowInfoDb;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;

public interface IGetFlowInfo {

    public ThirdFlowInfoVo getFlowInfo(String appid);

    public void getProcessInfoAndSave(String appid);

    public FlowInfoDb AddFlowInfo(String appId, Flow flow);
}
