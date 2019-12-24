package com.nature.mapper.flow;

import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.provider.flow.PropertyMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface PropertyMapper {

    /**
     * Insert list<Property> Note that the method of spelling sql must use Map to connect Param content to key value.
     *
     * @param propertyList (Content: The key is propertyList and the value is List<Property>)
     * @return
     */
    @InsertProvider(type = PropertyMapperProvider.class, method = "addPropertyList")
    public int addPropertyList(@Param("propertyList") List<Property> propertyList);

    /**
     * uerying group attribute information based on ID
     */
    @Select("select fs.* from flow_stops fs left join flow f on f.id = fs.fk_flow_id where f.id = #{fid} and fs.page_id = #{stopPageId} and fs.enable_flag = 1  limit 1 ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(property = "dataSource", column = "fk_data_source_id", many = @Many(select = "com.nature.mapper.dataSource.DataSourceMapper.getDataSourceById", fetchType = FetchType.LAZY)),
            @Result(property = "properties", column = "id", many = @Many(select = "com.nature.mapper.flow.PropertyTemplateMapper.getPropertyBySotpsId", fetchType = FetchType.LAZY)),
            @Result(property = "customizedPropertyList", column = "id", many = @Many(select = "com.nature.mapper.flow.CustomizedPropertyMapper.getCustomizedPropertyListByStopsId", fetchType = FetchType.LAZY))})
    public Stops getStopGroupList(@Param("fid") String fid, @Param("stopPageId") String stopPageId);

    /**
     * Modify stops attribute information
     */
    @UpdateProvider(type = PropertyMapperProvider.class, method = "updatePropertyCustomValue")
    public int updatePropertyCustomValue(String content, String id);

    /**
     * update property Method
     *
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
            @Result(column = "property_sensitive", property = "sensitive")})
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
