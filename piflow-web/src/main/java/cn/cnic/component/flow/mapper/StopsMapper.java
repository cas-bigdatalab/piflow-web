package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.provider.StopsMapperProvider;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Map;

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
            @Result(column = "is_data_source",property = "isDataSource"),
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
            @Result(column = "is_data_source",property = "isDataSource"),
            @Result(property = "dataSource", column = "fk_data_source_id", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourceMapper.adminGetDataSourceById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "customizedPropertyList", many = @Many(select = "cn.cnic.component.flow.mapper.CustomizedPropertyMapper.getCustomizedPropertyListByStopsId", fetchType = FetchType.LAZY))

    })
    public List<Stops> getStopsListByFlowId(String flowId);

    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsListByFlowIdAndPageIds")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "is_data_source",property = "isDataSource"),
            @Result(property = "dataSource", column = "fk_data_source_id", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourceMapper.getDataSourceById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "customizedPropertyList", many = @Many(select = "cn.cnic.component.flow.mapper.CustomizedPropertyMapper.getCustomizedPropertyListByStopsId", fetchType = FetchType.LAZY))

    })
    public List<Stops> getStopsListByFlowIdAndPageIds(@Param("flowId") String flowId, @Param("pageIds") String[] pageIds);

    @UpdateProvider(type = StopsMapperProvider.class, method = "updateStopEnableFlagByFlowId")
    public int updateStopEnableFlagByFlowId(String username, String id);

    /**
     * Query stop and attribute information based on stopsId
     *
     * @param Id
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsById")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "is_data_source",property = "isDataSource"),
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

    @Select("select * from flow_stops where enable_flag=1 and fk_flow_id=#{fid} and page_id=#{stopPageId} order by crt_dttm asc limit 1")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "oldProperties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getOldPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "customizedPropertyList", many = @Many(select = "cn.cnic.component.flow.mapper.CustomizedPropertyMapper.getCustomizedPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "fk_flow_id", property = "flow", many = @Many(select = "cn.cnic.component.flow.mapper.FlowMapper.getFlowById", fetchType = FetchType.LAZY)),
            @Result(column = "is_data_source",property = "isDataSource"),
            @Result(column = "fk_data_source_id", property = "dataSource", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourceMapper.getDataSourceById", fetchType = FetchType.LAZY))

    })
    public Stops getStopsByPageId(String fid, String stopPageId);

    @Select("SELECT name FROM flow_stops WHERE enable_flag=1 and fk_data_source_id=#{datasourceId}")
    public List<String> getStopsNamesByDatasourceId(@Param(value = "datasourceId") String datasourceId);
    
    /**
     * Query all stops data by datasource id
     *
     * @return
     */
    @Select("SELECT * FROM flow_stops WHERE enable_flag=1 and fk_data_source_id=#{datasourceId}")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "is_data_source",property = "isDataSource")
    })
    public List<Stops> getStopsListByDatasourceId(@Param(value = "datasourceId") String datasourceId);

    /**
     * Get StopsDisabledPages List By FlowId
     *
     * @return
     */
    @Select("SELECT page_id FROM flow_stops WHERE enable_flag=1 and is_disabled=1 and fk_flow_id=#{flowId}")
    public List<String> getStopsDisabledPagesListByFlowId(@Param(value = "flowId") String flowId);

    /**
     * Query stop and attribute information based on stopsId
     *
     * @param Id
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsById")
    @Results({
            @Result(column = "id", property = "propertiesVo", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyVoListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "oldPropertiesVo", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getOldPropertyVoListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "stopsCustomizedPropertyVoList", many = @Many(select = "cn.cnic.component.flow.mapper.CustomizedPropertyMapper.getCustomizedPropertyVoListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "fk_flow_id", property = "flowVo", many = @Many(select = "cn.cnic.component.flow.mapper.FlowMapper.getFlowVoById", fetchType = FetchType.LAZY)),
            @Result(column = "is_data_source",property = "isDataSource"),
            @Result(column = "fk_data_source_id", property = "dataSourceVo", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourceMapper.getDataSourceVoById", fetchType = FetchType.LAZY))
    })
    public StopsVo getStopsVoById(String Id);

    @Select("SELECT fs.id, fs.name FROM flow_stops fs\n" +
            "LEFT JOIN flow f ON f.id=fs.fk_flow_id\n" +
            "WHERE fs.enable_flag=1 AND f.is_example <> 1 AND (fs.is_disabled=0 or fs.is_disabled is null) AND fs.fk_flow_id=#{flowId}")
    public List<Map<String, String>> getStopsIdAndNameListByFlowId(String flowId);

    /**
     * Query stop and attribute information based on stopsId
     *
     * @param Ids
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getDisabledStopsNameListByIds")
    public List<String> getDisabledStopsNameListByIds(@Param(value = "Ids") List<String> Ids);

    /**
     * Query cannot published Stops name list by ids
     *
     * @param Ids
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getCannotPublishedStopsNameByIds")
    public List<String> getCannotPublishedStopsNameByIds(@Param(value = "Ids") List<String> Ids);


    /**
     * Query stop and attribute information based on stopsId
     *
     * @param Ids
     * @return
     */
    @SelectProvider(type = StopsMapperProvider.class, method = "getStopsBindDatasourceByIds")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "is_data_source",property = "isDataSource"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY)),
            @Result(column = "fk_flow_id", property = "flow", many = @Many(select = "cn.cnic.component.flow.mapper.FlowMapper.getFlowById", fetchType = FetchType.LAZY))
    })
    public List<Stops> getStopsBindDatasourceByIds(@Param(value = "Ids") List<String> Ids);

}
