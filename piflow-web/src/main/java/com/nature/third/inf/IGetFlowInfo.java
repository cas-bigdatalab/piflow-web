package com.nature.third.inf;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.third.vo.flowInfo.ThirdFlowInfo;

public interface IGetFlowInfo {

	public ThirdFlowInfo getFlowInfo(String appid);
	
	public FlowInfoDb AddFlowInfo(String appId,Flow flow);
}
