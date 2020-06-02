package cn.cnic.mapper.dataSource;

import cn.cnic.component.dataSource.model.DataSourceProperty;
import cn.cnic.provider.dataSource.DataSourcePropertyMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DataSourcePropertyMapper {
    /**
     * Add a single DataSourceProperty
     *
     * @param dataSourceProperty
     * @return
     */
    @InsertProvider(type = DataSourcePropertyMapperProvider.class, method = "addDataSourceProperty")
    public int addDataSourceProperty(DataSourceProperty dataSourceProperty);

    /**
     * Insert list<datasourceproperty> note that the way to spell SQL must use a map to connect Param content as a key value</datasourceproperty>
     *
     * @param dataSourcePropertyList
     * @return
     */
    @InsertProvider(type = DataSourcePropertyMapperProvider.class, method = "addDataSourcePropertyList")
    public int addDataSourcePropertyList(@Param("dataSourcePropertyList") List<DataSourceProperty> dataSourcePropertyList);

    /**
     * update dataSourceProperty
     *
     * @param dataSourceProperty
     * @return
     */
    @UpdateProvider(type = DataSourcePropertyMapperProvider.class, method = "updateDataSourceProperty")
    public int updateDataSourceProperty(DataSourceProperty dataSourceProperty);

    @SelectProvider(type = DataSourcePropertyMapperProvider.class, method = "getDataSourcePropertyListByDataSourceId")
    public List<DataSourceProperty> getDataSourcePropertyListByDataSourceId(String dataSourceId);

    /**
     * Delete dataSourceProperty according to Id logic
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = DataSourcePropertyMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(String id);

    /**
     * Delete the dataSourceProperty according to the datasourceId logic
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = DataSourcePropertyMapperProvider.class, method = "updateEnableFlagByDatasourceId")
    public int updateEnableFlagByDatasourceId(String id);

}
