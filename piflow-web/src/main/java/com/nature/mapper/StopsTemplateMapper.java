package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.nature.component.workFlow.model.StopsTemplate;

@Mapper
public interface StopsTemplateMapper {
	/**
	 * 查詢所有stops模板
	 * 
	 * @return
	 */
	@Select("select * from flow_stops_template")
	List<StopsTemplate> getStopsTemplateList();

	/**
	 * 根據stops模板id查詢模板
	 * 
	 * @param id
	 * @return
	 */
	@Select("select * from flow_stops_template where id = #{id}")
	StopsTemplate getStopsTemplateById(String id);

	/**
	 * 根據stops模板的id查詢stops模板(包括屬性list)
	 * 
	 * @param id
	 * @return
	 */
	@Select("select * from flow_stops_template where id = #{id}")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(property = "properties", column = "id", many = @Many(select = "com.nature.mapper.PropertyTemplateMapper.getPropertyTemplateBySotpsId")) })
	StopsTemplate getStopsPropertyById(String id);

	/**
	 * 根据stops组的id查询stops模板
	 * 
	 * @param groupId
	 * @return
	 */
	@Select("select * from flow_stops_template where id in (select agst.stops_template_id from association_groups_stops_template agst where agst.groups_id = #{groupId})")
	List<StopsTemplate> getStopsTemplateListByGroupId(String groupId);

}
