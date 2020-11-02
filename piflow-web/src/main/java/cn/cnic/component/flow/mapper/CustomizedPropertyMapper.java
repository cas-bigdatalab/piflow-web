package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.CustomizedProperty;
import cn.cnic.component.flow.mapper.provider.CustomizedPropertyMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface CustomizedPropertyMapper {

    /**
     * Insert "list<CustomizedProperty>" Note that the method of spelling "sql" must use "map" to connect the "Param" content to the key value.
     *
     * @param customizedPropertyList (Content: "customizedPropertyList" with a value of "List<CustomizedProperty>")
     * @return
     */
    @InsertProvider(type = CustomizedPropertyMapperProvider.class, method = "addCustomizedPropertyList")
    public int addCustomizedPropertyList(@Param("customizedPropertyList") List<CustomizedProperty> customizedPropertyList);

    @InsertProvider(type = CustomizedPropertyMapperProvider.class, method = "addCustomizedProperty")
    public int addCustomizedProperty(@Param("customizedProperty") CustomizedProperty customizedProperty);

    @Select("select * from flow_stops_customized_property where id = #{id} and enable_flag = 1 ")
    @Results({
            @Result(column = "fk_stops_id", property = "stops", many = @Many(select = "cn.cnic.component.flow.mapper.StopsMapper.getStopsById", fetchType = FetchType.LAZY))
    })
    public CustomizedProperty getCustomizedPropertyById(String id);

    @Select("select * from flow_stops_customized_property where fk_stops_id = #{stopsId} and enable_flag = 1 ")
    public List<CustomizedProperty> getCustomizedPropertyListByStopsId(String stopsId);

    @Select("select * from flow_stops_customized_property where fk_stops_id = #{stopsId} and name = #{name} and enable_flag = 1 ")
    public List<CustomizedProperty> getCustomizedPropertyListByStopsIdAndName(String stopsId, String name);

    @UpdateProvider(type = CustomizedPropertyMapperProvider.class, method = "updateStopsCustomizedProperty")
    public int updateStopsCustomizedProperty(@Param("customizedProperty") CustomizedProperty customizedProperty);

    @UpdateProvider(type = CustomizedPropertyMapperProvider.class, method = "updateEnableFlagByStopId")
    public int updateEnableFlagByStopId(@Param("username") String username, @Param("id") String id);

    @UpdateProvider(type = CustomizedPropertyMapperProvider.class, method = "updateCustomizedPropertyCustomValue")
    public int updateCustomizedPropertyCustomValue(String username, String content, String id);


}
