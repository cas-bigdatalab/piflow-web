package com.nature.component.workFlow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.mapper.StopsTemplateMapper;

@Service
public class StopsTemplateService {

	@Autowired
	private StopsTemplateMapper stopsTemplateMapper;

	public StopsTemplate getStopsTemplateById(String id) {
		return stopsTemplateMapper.getStopsTemplateById(id);
	}

	public StopsTemplate getStopsPropertyById(String id) {
		return stopsTemplateMapper.getStopsPropertyById(id);
	}

}
