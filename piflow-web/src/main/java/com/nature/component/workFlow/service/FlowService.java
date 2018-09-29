package com.nature.component.workFlow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.Flow;
import com.nature.mapper.FlowMapper;

@Service
public class FlowService {

	@Autowired
	private FlowMapper flowMapper;

	public int addFlow(Flow flow) {
		return flowMapper.addFlow(flow);
	}

	public Flow getFlowById(String id) {
		return flowMapper.getFlowById(id);
	}
}
