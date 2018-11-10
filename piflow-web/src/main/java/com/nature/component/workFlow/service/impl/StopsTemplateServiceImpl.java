package com.nature.component.workFlow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.component.workFlow.service.IStopsTemplateService;
import com.nature.mapper.StopsTemplateMapper;

@Service
public class StopsTemplateServiceImpl implements IStopsTemplateService {

	@Autowired
	StopsTemplateMapper stopsTemplateMapper;

	@Override
	public StopsTemplate getStopsTemplateById(String id) {
		return stopsTemplateMapper.getStopsTemplateById(id);
	}

	@Override
	public StopsTemplate getStopsPropertyById(String id) {
		return stopsTemplateMapper.getStopsTemplateAndPropertyById(id);
	}

}
