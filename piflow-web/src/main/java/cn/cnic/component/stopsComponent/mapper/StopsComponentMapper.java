package cn.cnic.component.stopsComponent.mapper;

import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.vo.StopsComponentVo;
import cn.cnic.component.stopsComponent.mapper.provider.StopsComponentMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface StopsComponentMapper {
    /**
     * Query all stops templates
     *
     * @return
     */
    @SelectProvider(type = StopsComponentMapperProvider.class, method = "getStopsComponentList")
    public List<StopsComponent> getStopsComponentList();

    /**
     * Query template based on the stops template
     *
     * @param id
     * @return
     */
    @SelectProvider(type = StopsComponentMapperProvider.class, method = "getStopsComponentById")
    public StopsComponent getStopsComponentById(String id);

    /**
     * Query the stops template based on the id of the stops template (including the attribute list)
     *
     * @param id
     * @return
     */
    @SelectProvider(type = StopsComponentMapperProvider.class, method = "getStopsComponentById")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(property = "properties", column = "id", many = @Many(select = "cn.cnic.component.stopsComponent.mapper.StopsComponentPropertyMapper.getStopsComponentPropertyByStopsId"))})
    public StopsComponent getStopsComponentAndPropertyById(String id);

    /**
     * Query the stops template according to the id of the stops group
     *
     * @param groupId
     * @return
     */
    @SelectProvider(type = StopsComponentMapperProvider.class, method = "getStopsComponentListByGroupId")
    public List<StopsComponent> getStopsComponentListByGroupId(String groupId);
    
    /**
     * Query the stops template according to the id of the stops group
     *
     * @param groupId
     * @return
     */
    @SelectProvider(type = StopsComponentMapperProvider.class, method = "getManageStopsComponentListByGroupId")
    public List<StopsComponentVo> getManageStopsComponentListByGroupId(String groupId);

    /**
     * Query the stops template according to the id of the stops group...
     *
     * @param stopsName
     * @return
     */
    @SelectProvider(type = StopsComponentMapperProvider.class, method = "getStopsComponentByName")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.stopsComponent.mapper.StopsComponentPropertyMapper.getStopsComponentPropertyByStopsId", fetchType = FetchType.LAZY))

    })
    public List<StopsComponent> getStopsComponentByName(String stopsName);

    /**
     * Add more than one FLOW_STOPS_TEMPLATE.
     *
     * @param stopsComponent
     * @return
     */
    @InsertProvider(type = StopsComponentMapperProvider.class, method = "insertStopsComponent")
    public int insertStopsComponent(@Param("stopsComponent") StopsComponent stopsComponent);

    /**
     * getStopsComponentByBundle
     *
     * @param bundle
     * @return
     */
    @Select("select fst.* from flow_stops_template fst where fst.bundel=#{bundle} and enable_flag=1")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.stopsComponent.mapper.StopsComponentPropertyMapper.getStopsComponentPropertyByStopsId", fetchType = FetchType.LAZY))

    })
    public StopsComponent getStopsComponentByBundle(@Param("bundle") String bundle);

    @Delete("delete from flow_stops_template")
    int deleteStopsComponent();


    @Delete("delete from flow_stops_template where id = #{id}")
    int deleteStopsComponentById(String id);

}
