package cn.cnic.component.stopsComponent.mapper;

import cn.cnic.component.stopsComponent.model.StopsComponentProperty;
import cn.cnic.component.stopsComponent.mapper.provider.StopsComponentPropertyMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StopsComponentPropertyMapper {

    /**
     * Query all the attributes of the corresponding stops according to the stopsId
     *
     * @param stopsId
     * @return
     */
    @SelectProvider(type = StopsComponentPropertyMapperProvider.class, method = "getStopsComponentPropertyByStopsId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "property_required", property = "required"),
            @Result(column = "property_sensitive", property = "sensitive")})
    List<StopsComponentProperty> getStopsComponentPropertyByStopsId(String stopsId);

    /**
     * Add more than one FLOW_STOPS_PROPERTY_TEMPLATE List.
     *
     * @param stopsComponentPropertyList
     * @return
     */
    @InsertProvider(type = StopsComponentPropertyMapperProvider.class, method = "insertStopsComponentProperty")
    public int insertStopsComponentProperty(@Param("stopsComponentPropertyList") List<StopsComponentProperty> stopsComponentPropertyList);

    @Delete("delete from flow_stops_property_template")
    int deleteStopsComponentProperty();

    @Delete("delete from flow_stops_property_template where fk_stops_id = #{fk_stops_id}")
    int deleteStopsComponentPropertyByStopId(@Param("fk_stops_id") String stopId);

}
