package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.provider.StopsMapperProvider;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * Stop component table
 */
@Mapper
public interface StopsMapper {

    /**
     * Add a single stops
     *
     * @param stops
     * @return
     */
    @InsertProvider(type = StopsMapperProvider.class, method = "addStops")
    public int addStops(Stops stops);

    /**
     * Insert list<Stops> Note that the method of spelling sql must use Map to connect Param content to key value.
     *
     * @param stopsList
     * @return
     */
    @InsertProvider(type = StopsMapperProvider.class, method = "addStopsList")
    public int addStopsList(@Param("stopsList") List<Stops> stopsList);

    /**
     * update stops
     *
     * @param stops
     * @return
     */
    @UpdateProvider(type = StopsMapperProvider.class, method = "updateStops")
    public int updateStops(Stops stops);

    /**
     * Query all stops data
     *
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsList")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "is_checkpoint", property = "isCheckpoint"),
            @Result(property = "dataSource", column = "fk_data_source_id", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourceMapper.getDataSourceById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "customizedPropertyList", many = @Many(select = "cn.cnic.component.flow.mapper.CustomizedPropertyMapper.getCustomizedPropertyListByStopsId", fetchType = FetchType.LAZY))

    })
    public List<Stops> getStopsList();

    /**
     * Query StopsList based on flowId
     *
     * @param flowId
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsListByFlowId")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "is_checkpoint", property = "isCheckpoint"),
            @Result(property = "dataSource", column = "fk_data_source_id", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourceMapper.adminGetDataSourceById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "customizedPropertyList", many = @Many(select = "cn.cnic.component.flow.mapper.CustomizedPropertyMapper.getCustomizedPropertyListByStopsId", fetchType = FetchType.LAZY))

    })
    public List<Stops> getStopsListByFlowId(String flowId);

    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsListByFlowIdAndPageIds")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(property = "dataSource", column = "fk_data_source_id", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourceMapper.getDataSourceById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "customizedPropertyList", many = @Many(select = "cn.cnic.component.flow.mapper.CustomizedPropertyMapper.getCustomizedPropertyListByStopsId", fetchType = FetchType.LAZY))

    })
    public List<Stops> getStopsListByFlowIdAndPageIds(@Param("flowId") String flowId, @Param("pageIds") String[] pageIds);

    @UpdateProvider(type = StopsMapperProvider.class, method = "updateEnableFlagByFlowId")
    public int updateEnableFlagByFlowId(String username, String id);

    /**
     * Query stop and attribute information based on stopsId
     *
     * @param Id
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsById")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "fk_flow_id", property = "flow", many = @Many(select = "cn.cnic.component.flow.mapper.FlowMapper.getFlowById", fetchType = FetchType.LAZY))

    })
    public Stops getStopsById(String Id);

    @Select("select * from flow_stops fs where fs.fk_flow_id = #{flowId} and fs.page_id = #{stopPageId} and fs.enable_flag = 1 ")
    public Stops getStopByFlowIdAndStopPageId(@Param("flowId") String flowId, @Param("stopPageId") String stopPageId);


    @UpdateProvider(type = StopsMapperProvider.class, method = "updateStopsByFlowIdAndName")
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo);


    /**
     * Verify that stopname is duplicated
     *
     * @param flowId
     * @param stopName
     * @return
     */
    @Select("select id from flow_stops where name = #{stopName} and fk_flow_id =#{flowId} and enable_flag = 1 ")
    public String getStopByNameAndFlowId(@Param(value = "flowId") String flowId, @Param(value = "stopName") String stopName);

    @Select("select MAX(page_id+0) from flow_stops where enable_flag=1 and fk_flow_id=#{flowId} ")
    public Integer getMaxStopPageIdByFlowId(String flowId);

    @Select("SELECT fs.name from flow_stops fs WHERE fs.enable_flag=1 and fs.fk_flow_id =#{flowId}")
    public String[] getStopNamesByFlowId(String flowId);

    @Select("select * from flow_stops where enable_flag=1 and fk_flow_id=#{fid} and page_id=#{stopPageId}  limit 1")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "fk_flow_id", property = "flow", many = @Many(select = "cn.cnic.component.flow.mapper.FlowMapper.getFlowById", fetchType = FetchType.LAZY))

    })
    public Stops getStopsByPageId(String fid, String stopPageId);

    @Select("SELECT name FROM flow_stops WHERE fk_data_source_id=#{datasourceId}")
    public List<String> getStopsNamesByDatasourceId(@Param(value = "datasourceId") String datasourceId);
    
    /**
     * Query all stops data by datasource id
     *
     * @return
     */
    @Select("SELECT * FROM flow_stops WHERE fk_data_source_id=#{datasourceId}")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
    })
    public List<Stops> getStopsListByDatasourceId(@Param(value = "datasourceId") String datasourceId);
    
}
