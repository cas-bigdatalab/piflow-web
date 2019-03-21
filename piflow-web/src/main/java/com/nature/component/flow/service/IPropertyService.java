package com.nature.component.flow.service;

import java.util.List;

import com.nature.component.flow.model.Property;
import com.nature.component.flow.vo.StopsVo;


public interface IPropertyService {
	
	 /**
	  * Querying group attribute information based on ID
	  * @param id
	  * @return
	  */
	public StopsVo queryAll(String fid, String id);
	 
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
	public int updateProperty(String content,String id);
	
	/**
	 * query All StopsProperty List;
	 * @return
	 */
	public List<Property> getStopsPropertyList();

    /**
     * delete StopsProperty according to ID;
     * @return
     */
	public int deleteStopsPropertyById(String id);
	
	/**
     * delete StopsProperty according to StopId;
     * @return
     */
	public int deleteStopsPropertyByStopId(String id);
	
	
	/**
	 * check stops template 
	 * @param stops
	 */
	public void checkStopTemplateUpdate(String stopsId);

}
