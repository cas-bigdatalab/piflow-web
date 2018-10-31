package com.nature.component.workFlow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.service.IStopGroupService;
import com.nature.mapper.StopGroupMapper;

@Service
public class StopGroupServiceImpl implements IStopGroupService {

	@Autowired
	StopGroupMapper stopGroupMapper;

	@Override
	public List<StopGroup> getStopGroupAll() {
		return stopGroupMapper.getStopGroupList();
	}

}
