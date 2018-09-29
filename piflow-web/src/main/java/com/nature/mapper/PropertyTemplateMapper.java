package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
