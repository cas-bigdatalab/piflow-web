package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.provider.PropertyMapperProvider;

@Mapper
public interface PropertyMapper {

	/**
	 * 插入list<Property> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param propertyList (内容： 键为propertyList,值为List<Property>)
	 * @return
	 */
	@InsertProvider(type = PropertyMapperProvider.class, method = "addPropertyList")
	public int addPropertyList(@Param("propertyList") List<Property> propertyList);

	/**
	 * uerying group attribute information based on ID
	 */
	@Select("select fs.* from flow_stops fs LEFT JOIN flow f on f.id = fs.fk_flow_id where f.id = #{fid} and fs.page_id = #{id} and fs.enable_flag = 1  LIMIT 1 ")
	@Results({
			@Result(id = true, column = "id", property = "id"),
			@Result(property = "properties", column = "id", many = @Many(select = "com.nature.mapper.PropertyTemplateMapper.getPropertyBySotpsId")) })
	public Stops getStopGroupList(@Param("fid") String fid, @Param("id") String id);

	/**
	 * Modify stops attribute information
	 */
	@Update("update flow_stops_property set custom_value = #{custom_value},version = version+1 where id = #{id}")
	public int updatePropertyCustomValue(@Param("custom_value") String content, @Param("id") String id);
	
	/**
	 * update property Method
	 * @param property
	 * @return
	 */
	@UpdateProvider(type = PropertyMapperProvider.class, method = "updateStopsProperty")
	public int updateStopsProperty(Property property);

	/**
	 * query All StopsProperty List;
	 *
	 * @return
	 */
	@Select("select * from flow_stops_property where enable_flag = 1 ")
	public List<Property> getStopsPropertyList();

	@Select("select * from flow_stops_property where fk_stops_id = #{stopsId} and enable_flag = 1 ")
	@Results({
		@Result(id = true, column = "id", property = "id"),
		@Result(column = "property_required", property = "required"),
		@Result(column = "property_sensitive", property = "sensitive") })
	public List<Property> getPropertyListByStopsId(String stopsId);

	/**
	 * delete StopsProperty according to ID;
	 * 
	 * @return
	 */
	@Delete("delete from flow_stops_property where id=#{id}")
	public int deleteStopsPropertyById(String id);

	/**
	 * delete StopsProperty according to StopId;
	 * 
	 * @return
	 */
	@Delete("delete from flow_stops_property where id=#{id}")
	public int deleteStopsPropertyByStopId(String id);
}
