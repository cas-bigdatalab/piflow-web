package com.nature.component.dataSource.service;

import com.nature.component.dataSource.vo.DataSourceVo;

import java.util.List;

public interface IDataSource {

    /**
     * save or update DataSource
     *
     * @param dataSourceVo
     * @return
     */
    public String saveOrUpdate(DataSourceVo dataSourceVo);

    /**
     * Query DataSourceVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public DataSourceVo dataSourceVoById(String id);

    /**
     * Query DataSourceVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public String getDataSourceVoById(String id);

    /**
     * getDataSourceVoList
     *
     * @param isTemplate
     * @return
     */
    public String getDataSourceVoList(boolean isTemplate);

    /**
     * getDataSourceTemplateList
     *
     * @param
     * @return
     */
    public List<DataSourceVo> getDataSourceTemplateList();

    /**
     * Query dataSourceVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    public String getDataSourceVoListPage(Integer offset, Integer limit, String param);

    /**
     * delete DataSource By Id
     *
     * @param id
     * @return
     */
    public String deleteDataSourceById(String id);

    /**
     * fill datasource to stop
     *
     * @param dataSourceId
     * @param stopId
     * @return
     */
    public String fillDatasource(String dataSourceId, String stopId);


}
