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
            @Result(column = "id", property = "dataSourcePropertyList", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper.getDataSourcePropertyListByDataSourceId", fetchType = FetchType.LAZY))
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
            @Result(column = "id", property = "dataSourcePropertyList", many = @Many(select = "cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper.getDataSourcePropertyListByDataSourceId", fetchType = FetchType.LAZY))
    })
    DataSource adminGetDataSourceById(@Param("id") String id);

    @UpdateProvider(type = DataSourceMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("username") String username, @Param("id") String id);


}
