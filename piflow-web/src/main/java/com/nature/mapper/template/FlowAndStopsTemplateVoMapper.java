package com.nature.mapper.template;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.template.vo.FlowTemplateVo;
import com.nature.component.template.vo.StopTemplateVo;
import com.nature.component.workFlow.vo.PropertyVo;
import com.nature.provider.FlowAndStopsTemplateVoMapperProvider;

/**
 * stop组建表
 * 
 * @author Nature
 *
 */
@Mapper
public interface FlowAndStopsTemplateVoMapper {

	/**
	 * 添加单个stops
	 * 
	 * @param stops
	 * @return
	 */
	@InsertProvider(type = FlowAndStopsTemplateVoMapperProvider.class, method = "addStops")
	public int addStops(StopTemplateVo stops);
	
	/**
	 * 新增Flow
	 * 
	 * @param flow
	 * @return
	 */
	@InsertProvider(type = FlowAndStopsTemplateVoMapperProvider.class, method = "addFlow")
	public int addFlow(FlowTemplateVo flow);
	
	/**
	 * 插入list<PropertyVo> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param propertyList (内容： 键为propertyList,值为List<PropertyVo>)
	 * @return
	 */
	@InsertProvider(type = FlowAndStopsTemplateVoMapperProvider.class, method = "addPropertyList")
	public int addPropertyList(@Param("propertyList") List<PropertyVo> propertyList);

	/**
	 * 根据templateId修改无效或删除stop
	 * @param templateId
	 * @return
	 */
	@Update("update stops_template set enable_flag = 0 where fk_template_id = #{id} ")
	public int deleteStopTemByTemplateId(@Param("id") String templateId);
	
	/**
	 * 根据stopId修改无效或删除stop属性信息
	 * @param stopId
	 * @return
	 */
	@Update("update property_template set enable_flag = 0 where fk_stops_id = #{id} ")
	public int deleteStopPropertyTemByStopId(@Param("id") String stopId);
	
	/**
	 * 根据模板id查询所有stop信息
	 * @param templateId
	 * @return
	 */
	@Select("select * from stops_template where enable_flag = 1  and fk_template_id = #{templateId};")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.template.FlowAndStopsTemplateVoMapper.getPropertyListByStopsId", fetchType = FetchType.EAGER))

	})
	public List<StopTemplateVo> getStopsListByTemPlateId(@Param("templateId") String templateId);
	
	
	/**
	 * 根据stopId查询所有stop属性信息
	 * @param templateId
	 * @return
	 */
	@Select("select * from property_template where fk_stops_id = #{stopsId} and enable_flag = 1 ")
	@Results({
		@Result(id = true, column = "id", property = "id"),
		@Result(column = "property_required", property = "required"),
		@Result(column = "property_sensitive", property = "sensitive") })
	public List<PropertyVo> getPropertyListByStopsId(String stopsId);

}
