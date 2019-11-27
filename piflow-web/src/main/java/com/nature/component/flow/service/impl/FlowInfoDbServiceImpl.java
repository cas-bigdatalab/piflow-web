package com.nature.component.flow.service.impl;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowInfoDb;
import com.nature.component.flow.service.IFlowInfoDbService;
import com.nature.mapper.flow.FlowInfoDbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowInfoDbServiceImpl implements IFlowInfoDbService {

	
	@Autowired
	private FlowInfoDbMapper appMapper;
	
	@Override
	public List<Flow> findAppList() {
		return appMapper.findAppList();
	}

	@Override
	public List<FlowInfoDb> getFlowInfoByIds(List<String> ids) {
		return appMapper.getFlowInfoByIds(ids);
	}

	@Override
	public FlowInfoDb getFlowInfoById(String id) {
		return appMapper.flowInfoDb(id);
	}

	@Override
	public int deleteFlowInfoById(String id) {
		return appMapper.updateEnableFlagById(id);
	}

	@Override
	public List<FlowInfoDb> getAppListByFlowId(String flowId) {
		return appMapper.getAppListByFlowId(flowId);
	}
	 
}
