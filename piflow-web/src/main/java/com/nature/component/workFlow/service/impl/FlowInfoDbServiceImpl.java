package com.nature.component.workFlow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.component.workFlow.service.IFlowInfoDbService;
import com.nature.mapper.FlowInfoDbMapper;

@Service
public class FlowInfoDbServiceImpl implements IFlowInfoDbService {

	
	@Autowired
	FlowInfoDbMapper appMapper;
	
	@Override
	public List<Flow> findAppList() {
		return appMapper.findAppList();
	}

	@Override
	public List<FlowInfoDb> getFlowInfoByIds(List<String> ids) {
		return appMapper.getFlowInfoByIds(ids);
	}

	@Override
	public int deleteFlowInfoById(String id) {
		return appMapper.deleteFlowInfoById(id);
	}
	 
}
