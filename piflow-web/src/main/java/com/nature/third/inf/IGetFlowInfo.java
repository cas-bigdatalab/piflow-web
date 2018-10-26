package com.nature.third.inf;

import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.third.vo.flowInfo.FlowInfo;

public interface IGetFlowInfo {

	public FlowInfo getFlowInfo(String appid);
	
	public FlowInfoDb AddFlowInfo(String appId);
}
