package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;

@Mapper
public interface PropertyMapper {

	public int addPropertyList(List<Property> propertyList);

	/**
	 * uerying group attribute information based on ID
	 */
	@Select("select fs.* from flow_stops fs LEFT JOIN flow f on f.id = fs.fk_flow_id where f.id = #{fid} and fs.page_id = #{id}")
	@Results({
			@Result(property = "properties", column = "id", many = @Many(select = "com.nature.mapper.PropertyTemplateMapper.getPropertyBySotpsId")) })
	public Stops getStopGroupList(@Param("fid") String fid, @Param("id") String id);

	/**
	 * Modify stops attribute information
	 */
	@Update("update flow_stops_property set name = #{name},display_name = #{display_name},custom_value = #{custom_value},description = #{description},version = #{version} where id = #{id}")
	public int updateStops(

			@Param("name") String content, @Param("display_name") String display_name,
			@Param("custom_value") String custom_value, @Param("description") String description,
			@Param("version") String version, @Param("id") String id

	);

	/**
	 * query All StopsProperty List;
	 * 
	 * @return
	 */
    @Select("select * from flow_stops_property")
	public List<Property> getStopsPropertyList();
	
	@Select("select * from flow_stops_property where fk_stops_id = #{stopsId}")
	public List<Property> getPropertyListByStopsId(String stopsId);

	/**
	 * delete StopsProperty according to ID;
	 * 
	 * @return
	 */
	@Delete("delete from flow_stops_property where id=#{id}")
	public int deleteStopsProperty(String id);

}
