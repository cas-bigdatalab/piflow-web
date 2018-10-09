package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.PropertyTemplate;

@Mapper
public interface PropertyTemplateMapper {

	/**
	 * 根據stops模板id查詢對應的stops的所有屬性
	 * 
	 * @param stopsId
	 * @return
	 */
	@Select("select fspt.* from flow_stops_property_template fspt where fspt.fk_stops_id = #{stopsId}")
	List<PropertyTemplate> getPropertyTemplateBySotpsId(String stopsId);
	
	@Select("select fsp.id, fsp.name, fsp.description,fsp.display_name,fsp.custom_value,fsp.version from flow_stops_property fsp where fsp.fk_stops_id = #{id}")
	List<Property> getPropertyBySotpsId(String id);
}
