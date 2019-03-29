package com.nature.component.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.flow.model.StopsTemplate;
import com.nature.component.flow.service.IStopsTemplateService;
import com.nature.mapper.flow.StopsTemplateMapper;

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
