package com.nature.mapper;

import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.provider.PropertyMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
			@Result(column = "is_checkpoint", property = "checkpoint"),
			@Result(property = "properties", column = "id", many = @Many(select = "com.nature.mapper.PropertyTemplateMapper.getPropertyBySotpsId")) })
	public Stops getStopGroupList(@Param("fid") String fid, @Param("id") String id);

	/**
	 * Modify stops attribute information
	 */
	@UpdateProvider(type = PropertyMapperProvider.class, method = "updatePropertyCustomValue")
	public int updatePropertyCustomValue(String content, String id);
	
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
	@UpdateProvider(type = PropertyMapperProvider.class, method = "updateEnableFlagByStopId")
	public int updateEnableFlagByStopId(String id);
}
