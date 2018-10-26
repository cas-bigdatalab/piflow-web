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
	public Integer updateStops(String[] content,String id){
		int updateStops = 0;
		for (String string : content) {
			System.out.println(string);
			String[] split = string.split(",");
			String updateContent = split[0];
			String updateId = split[1];
			updateStops = PropertyService.updateStops(updateContent,updateId);
		}
		return updateStops;
	}
	
	@RequestMapping("/updateStopsOne")
	public Integer updateStops(String content,String id){
		int updateStops = PropertyService.updateStops(content,id);
		return updateStops;
		
	}

}
