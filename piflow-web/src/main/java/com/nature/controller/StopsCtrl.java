package com.nature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.service.StopsTemplateService;

@RestController
@RequestMapping("/stops")
public class StopsCtrl {
	
	@Autowired
	StopsTemplateService stopsTemplateService;

	@RequestMapping("/queryIdInfo")
	public List<Property> getStopGroup(String id) {
		List<Property> queryInfo = stopsTemplateService.queryAll(id);
		if (queryInfo.size()>0 && !queryInfo.isEmpty()) {
			return queryInfo;
		}
		return null;
	}
	
	@RequestMapping("/updateStops")
	public Integer updateStops(String content,String id){
		int updateStops = stopsTemplateService.updateStops(content,id);
		return updateStops;
		
	}

}
