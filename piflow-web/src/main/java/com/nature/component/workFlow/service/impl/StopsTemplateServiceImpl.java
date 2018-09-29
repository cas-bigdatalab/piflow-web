package com.nature.component.workFlow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.component.workFlow.service.StopsTemplateService;
import com.nature.mapper.PropertyMapper;
import com.nature.mapper.StopsTemplateMapper;

@Service
public class StopsTemplateServiceImpl implements StopsTemplateService {

	@Autowired
	PropertyMapper propertyMapper;

	@Autowired
	StopsTemplateMapper stopsTemplateMapper;

	@Override
	public List<Property> queryAll(String id) {
		return propertyMapper.getStopGroupList(id);
	}

	@Override
	public int updateStops(String content, String id) {
		return propertyMapper.updateStops(content, id);
	}

	@Override
	public StopsTemplate getStopsTemplateById(String id) {
		return stopsTemplateMapper.getStopsTemplateById(id);
	}

	@Override
	public StopsTemplate getStopsPropertyById(String id) {
		return stopsTemplateMapper.getStopsPropertyById(id);
	}

}
