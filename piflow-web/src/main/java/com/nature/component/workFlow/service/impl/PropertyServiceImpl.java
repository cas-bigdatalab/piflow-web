package com.nature.component.workFlow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.PropertyService;
import com.nature.mapper.PropertyMapper;

@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	PropertyMapper propertyMapper;
	
	@Override
	public Stops queryAll(String fid,String id) {
		return propertyMapper.getStopGroupList(fid,id);
	}

	@Override
	public int updateStops(String content,String id) {
		return propertyMapper.updateStops(content,id);
	}

	@Override
	public List<Property> getStopsPropertyList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteStopsProperty(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
