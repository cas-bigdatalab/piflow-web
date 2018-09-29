package com.nature.component.workFlow.service;

import java.util.List;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.StopsTemplate;

public interface StopsTemplateService {
	
	 /**
	  * Querying group attribute information based on ID
	  * @param id
	  * @return
	  */
	public List<Property> queryAll(String id);
	 
	/**
	 * Modify stops attribute information
	 * @param id
	 * @param content
	 * @return
	 */
	public int updateStops(String id, String content);

	public StopsTemplate getStopsTemplateById(String id);

	public StopsTemplate getStopsPropertyById(String id);
}
