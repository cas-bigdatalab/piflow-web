package com.nature.mapper.flow;

import com.nature.component.flow.model.Property;
import com.nature.component.group.model.PropertyTemplate;
import com.nature.provider.flow.PropertyTemplateMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PropertyTemplateMapper {

	/**
	 * Query all the attributes of the corresponding stops according to the stopsId
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
	@Select("select fsp.id, fsp.name, fsp.description,fsp.display_name,fsp.custom_value,fsp.version,fsp.allowable_values,fsp.property_required,fsp.is_select,fsp.is_locked "
		  + " from flow_stops_property fsp where fsp.fk_stops_id = #{id}  ORDER BY fsp.property_sort desc")
	@Results({
		@Result(id = true, column = "id", property = "id"),
        @Result(column = "property_required", property = "required"),
        @Result(column = "property_sensitive", property = "sensitive"),
		@Result(column = "is_select", property = "isSelect") })
	List<Property> getPropertyBySotpsId(String id);
	
	/**
	 * Add more than one FLOW_STOPS_PROPERTY_TEMPLATE List.
	 * @param propertyTemplateList
	 * @return
	 */
	@InsertProvider(type = PropertyTemplateMapperProvider.class, method = "insertPropertyTemplate")
	public int insertPropertyTemplate(@Param("propertyTemplateList")List<PropertyTemplate> propertyTemplateList);

}
