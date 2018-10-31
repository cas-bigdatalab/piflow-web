package com.nature.component.workFlow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.service.IPathsService;
import com.nature.mapper.PathsMapper;

@Service
public class PathsServiceImpl implements IPathsService {

	@Autowired
	private PathsMapper pathsMapper;
	
	@Override
	public int deletePathsByFlowId(String id) {
		return pathsMapper.deletePathsByFlowId(id);
	}

}
