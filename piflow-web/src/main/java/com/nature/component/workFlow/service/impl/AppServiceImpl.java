package com.nature.component.workFlow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.service.AppService;
import com.nature.mapper.FlowInfoDbMapper;

@Service
public class AppServiceImpl implements AppService {

	
	@Autowired
	FlowInfoDbMapper appMapper;
	
	@Override
	public List<Flow> findAppList() {
		return appMapper.findAppList();
	}
	 
}
