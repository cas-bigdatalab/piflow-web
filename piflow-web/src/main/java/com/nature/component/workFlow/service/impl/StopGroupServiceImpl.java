package com.nature.component.workFlow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.service.StopGroupService;
import com.nature.mapper.StopGroupMapper;

@Service
public class StopGroupServiceImpl implements StopGroupService {

	@Autowired
	StopGroupMapper stopGroupMapper;

	@Override
	public List<StopGroup> getStopGroupAll() {
		return stopGroupMapper.getStopGroupList();
	}

}
