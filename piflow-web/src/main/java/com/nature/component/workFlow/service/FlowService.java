package com.nature.component.workFlow.service;

import com.nature.component.workFlow.model.Flow;

public interface FlowService {

	public int addFlow(Flow flow);

	public Flow getFlowById(String id);
}
