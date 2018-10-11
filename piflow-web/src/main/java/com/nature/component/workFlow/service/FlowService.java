package com.nature.component.workFlow.service;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.workFlow.model.Flow;

public interface FlowService {

	public StatefulRtnBase addFlow(MxGraphModel mxGraphModel, String flowId);

	public Flow getFlowById(String id);
}
