package com.nature.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.PropertyService;

@RestController
@RequestMapping("/stops")
public class StopsCtrl {
	
	@Autowired
	PropertyService PropertyService;

	@RequestMapping("/queryIdInfo")
	public Stops getStopGroup(String fid,String id) {
		Stops queryInfo = PropertyService.queryAll(fid,id);
		return (Stops) queryInfo;
	}
	
	@RequestMapping("/updateStops")
	public Integer updateStops(String content,String display_name,String custom_value,String description,String version,String id){
		int updateStops = PropertyService.updateStops(content,display_name,custom_value,description,version,id);
		return updateStops;
		
	}

}
