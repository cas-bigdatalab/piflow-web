package com.nature.component.workFlow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.service.IStopsService;
import com.nature.mapper.StopsMapper;

@Service
public class StopsServiceImpl implements IStopsService {
	
	@Autowired
	private StopsMapper stopsMapper;

	@Override
	public int deleteStopsByFlowId(String id) {
		return stopsMapper.deleteStopsByFlowId(id);
	}

}
