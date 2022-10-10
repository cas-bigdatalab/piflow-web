package cn.cnic.component.dataSource.mapper;

import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.mapper.provider.DataSourceMapperProvider;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface DataSourceMapper {
    /**
     * add DataSource
     *
     * @param dataSource
     * @return
     */
    @InsertProvider(type = DataSourceMapperProvider.class, method = "addDataSource")
    public int addDataSource(DataSource dataSource);

    /**
     * update DataSource
     *
     * @param dataSource
     * @return
     */
    @UpdateProvider(type = DataSourceMapperProvider.class, method = "updateDataSource")
    public int updateDataSource(DataSource dataSource);

    /**
     * query all DataSource
     *
     * @return
     */
    @SelectProvider(type = DataSourceMapperProvider.class, method = "getDataSourceList")
    public List<DataSource> getDataSourceList(@Param("username") String username, @Param("isAdmin") boolean isAdmin);

    @SelectProvider(type = DataSourceMapperProvider.class, method = "getDataSourceListParam")
    public List<DataSource> getDataSourceListParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, String param);

    @SelectProvider(type = DataSourceMapperProvider.class, method = "getDataSourceListParam")
    public List<DataSourceVo> getDataSourceVoListParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, String param);

    /**
     * query all TemplateDataSource
     *
     * @return
     */
    @SelectProvider(type = DataSourceMapperProvider.class, method = "getDataSourceTemplateList")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "stops_template_bundle", property = "stopsTemplateBundle"),
            @Result(column = "id", property = "dataSourcePropertyList", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper.getDataSourcePropertyListByDataSourceId", fetchType = FetchType.LAZY))
    })
    public List<DataSource> getDataSourceTemplateList();

    /**
     * query DataSource by DataSourceId
     *
     * @param id
     * @return
     */
    @SelectProvider(type = DataSourceMapperProvider.class, method = "getDataSourceById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "stops_template_bundle", property = "stopsTemplateBundle"),
            @Result(column = "id", property = "dataSourcePropertyList", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper.getDataSourcePropertyListByDataSourceId", fetchType = FetchType.LAZY)),
            @Result(column = "stops_template_bundle",property = "stopsComponent",many = @Many(select = "cn.cnic.component.stopsComponent.mapper.StopsComponentMapper.getDataSourceStopsComponentByBundle", fetchType = FetchType.LAZY))
    })
    DataSource getDataSourceByIdAndUser(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("id") String id);


    /**
     * query DataSource by DataSourceId
     *
     * @param id
     * @return
     */
    @SelectProvider(type = DataSourceMapperProvider.class, method = "getDataSourceById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "stops_template_bundle", property = "stopsTemplateBundle"),
            @Result(column = "id", property = "dataSourcePropertyList", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper.getDataSourcePropertyListByDataSourceId", fetchType = FetchType.LAZY))
    })
    DataSource getDataSourceById(@Param("id") String id);

    /**
     * query DataSource by DataSourceId
     *
     * @param id
     * @return
     */
    @SelectProvider(type = DataSourceMapperProvider.class, method = "adminGetDataSourceById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "stops_template_bundle", property = "stopsTemplateBundle"),
            @Result(column = "id", property = "dataSourcePropertyList", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper.getDataSourcePropertyListByDataSourceId", fetchType = FetchType.LAZY))
    })
    DataSource adminGetDataSourceById(@Param("id") String id);

    @UpdateProvider(type = DataSourceMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("username") String username, @Param("id") String id);


    @SelectProvider(type = DataSourceMapperProvider.class, method = "getStopDataSourceForFlowPage")
    @Results({
        @Result(column = "stops_template_bundle", property = "stopsTemplateBundle"),
        @Result(column = "name",property = "stopsName")
    })
    public List<DataSourceVo> getStopDataSourceForFlowPage(@Param("username") String username, @Param("isAdmin") boolean isAdmin);

    /**
     * Query "stoptemplatebundle" of all "stop" data sources
     * @return
     */
    @Select("select DISTINCT(stops_template_bundle) from data_source where  stops_template_bundle is not null")
    public List<String> getAllStopDataSourceBundle();

    /**
     * Change 'datasource' to available / unavailable
	 * 
     * @param bundle stop的bundle
     * @param isAvailable 1:available;0:unavailable
     * @return
     * @author leilei
     * @date 2022-05-23
     */
    @Update("update data_source set is_available = #{isAvailable} where stops_template_bundle = #{bundle}")
    public int updateDataSourceIsAvailableByBundle(@Param("bundle") String bundle, @Param("isAvailable") int isAvailable);

    /**
     * Modify 'imageurl' of 'datasource'
	 * 
     * @param bundle
     * @param imageUrl
     * @return
     * @author leilei
     * @date 2022-06-14
     */
    @Update("update data_source set image_url = #{imageUrl} where stops_template_bundle = #{bundle}")
    public int updateDataSourceImageUrlByBundle(@Param("bundle") String bundle, @Param("imageUrl") String imageUrl);

    /**
     * getDataSource Id by dataSourceName
     * @param dataSourceName
     * @return
     */
    @Select("select id from data_source where data_source_name = #{dataSourceName} and id !=#{id} and is_template = 0 and enable_flag = 1 ")
    public List<String> getDataSourceByDataSourceName(@Param("dataSourceName")String dataSourceName, @Param("id")String id);

    /**
     * query DataSource by DataSourceId
     *
     * @param id
     * @return
     */
    @SelectProvider(type = DataSourceMapperProvider.class, method = "getDataSourceById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "stops_template_bundle", property = "stopsTemplateBundle"),
            @Result(column = "id", property = "dataSourcePropertyList", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper.getDataSourcePropertyVoListByDataSourceId", fetchType = FetchType.LAZY))
    })
    DataSourceVo getDataSourceVoById(@Param("id") String id);

}
