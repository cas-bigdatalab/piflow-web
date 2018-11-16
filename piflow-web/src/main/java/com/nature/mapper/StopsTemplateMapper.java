package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.provider.StopsTemplateMapperProvider;

@Mapper
public interface StopsTemplateMapper {
	/**
	 * 查询所有stops模板
	 * 
	 * @return
	 */
	@SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateList")
	public List<StopsTemplate> getStopsTemplateList();

	/**
	 * 根据stops模板查询模板
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateById")
	public StopsTemplate getStopsTemplateById(String id);

	/**
	 * 根据stops模板的id查询stops模板(包括属性list)
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateById")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(property = "properties", column = "id", many = @Many(select = "com.nature.mapper.PropertyTemplateMapper.getPropertyTemplateBySotpsId")) })
	public StopsTemplate getStopsTemplateAndPropertyById(String id);

	/**
	 * 根据stops组的id查询stops模板
	 * 
	 * @param groupId
	 * @return
	 */
	@Select("select * from flow_stops_template where id in (select agst.stops_template_id from association_groups_stops_template agst where agst.groups_id = #{groupId})")
	public List<StopsTemplate> getStopsTemplateListByGroupId(String groupId);

	/**
	 * 根据stopsName查询StopsTemplate
	 * 
	 * @param stopsName
	 * @return
	 */
	@SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateByName")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.PropertyTemplateMapper.getPropertyTemplateBySotpsId", fetchType = FetchType.EAGER))

	})
	public List<StopsTemplate> getStopsTemplateByName(String stopsName);

	/**
	 * Add more than one FLOW_STOPS_TEMPLATE List.
	 * 
	 * @param stopsTemplateList
	 * @return
	 */
	@InsertProvider(type = StopsTemplateMapperProvider.class, method = "insertStopsTemplate")
	public int insertStopsTemplate(@Param("stopsTemplateList")List<StopsTemplate> stopsTemplateList);
}
