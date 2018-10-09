package com.nature.component.workFlow.service;

import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.StopsTemplate;

public interface StopsTemplateService {
	
	 /**
	  * Querying group attribute information based on ID
	  * @param id
	  * @return
	  */
	public Stops queryAll(String fid,String id);
	 
	/**
	 * Modify stops attribute information
	 * @param id
	 * @param content
	 * @param id2 
	 * @param version 
	 * @param description 
	 * @param custom_value 
	 * @return
	 */
	public int updateStops(String content,String display_name,String custom_value,String description,String version,String id);

	public StopsTemplate getStopsTemplateById(String id);

	public StopsTemplate getStopsPropertyById(String id);
}
