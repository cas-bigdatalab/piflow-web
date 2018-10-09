package com.nature.component.workFlow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.Stops;
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
	public Stops queryAll(String fid,String id) {
		return propertyMapper.getStopGroupList(fid,id);
	}

	@Override
	public int updateStops(String content,String display_name,String custom_value,String description,String version,String id) {
		return propertyMapper.updateStops(content,display_name,custom_value,description,version,id);
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
