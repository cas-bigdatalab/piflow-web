package com.nature.mapper;

import java.util.List;

import com.nature.provider.StopsTemplateMapperProvider;
import org.apache.ibatis.annotations.*;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.PropertyTemplate;
import com.nature.provider.PropertyTemplateMapperProvider;

@Mapper
public interface PropertyTemplateMapper {

	/**
	 * 根据stopsId查询对应的stops的所有属性
	 * 
	 * @param stopsId
	 * @return
	 */
	@SelectProvider(type = PropertyTemplateMapperProvider.class, method = "getPropertyTemplateBySotpsId")
	@Results({
			@Result(id = true, column = "id", property = "id"),
			@Result(column = "property_required", property = "required"),
			@Result(column = "property_sensitive", property = "sensitive") })
	List<PropertyTemplate> getPropertyTemplateBySotpsId(String stopsId);

	/**
	 * Query through ID flow_stops_property.
	 *
	 * @param id
	 * @return
	 */
	@Select("select fsp.id, fsp.name, fsp.description,fsp.display_name,fsp.custom_value,fsp.version,fsp.allowable_values,fsp.property_required "
		  + " from flow_stops_property fsp where fsp.fk_stops_id = #{id}  ORDER BY fsp.display_name ")
	@Results({
		@Result(id = true, column = "id", property = "id"),
        @Result(column = "property_required", property = "required"),
        @Result(column = "property_sensitive", property = "sensitive") })
	List<Property> getPropertyBySotpsId(String id);
	
	/**
	 * Add more than one FLOW_STOPS_PROPERTY_TEMPLATE List.
	 * @param propertyTemplateList
	 * @return
	 */
	@InsertProvider(type = PropertyTemplateMapperProvider.class, method = "insertPropertyTemplate")
	public int insertPropertyTemplate(@Param("propertyTemplateList")List<PropertyTemplate > propertyTemplateList);

}
