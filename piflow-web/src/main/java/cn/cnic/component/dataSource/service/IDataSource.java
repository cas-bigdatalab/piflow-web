package cn.cnic.component.dataSource.service;

import cn.cnic.component.dataSource.vo.DataSourceVo;

import java.util.List;

public interface IDataSource {

    /**
     * save or update DataSource
     *
     * @param dataSourceVo
     * @return
     */
    public String saveOrUpdate(String username, boolean isAdmin, DataSourceVo dataSourceVo, boolean isSynchronize);

    /**
     * Query DataSourceVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public DataSourceVo dataSourceVoById(String username, boolean isAdmin, String id);

    /**
     * Query DataSourceVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public String getDataSourceVoById(String username, boolean isAdmin, String id);

    /**
     * getDataSourceVoList
     *
     * @param isAdmin
     * @param username
     * @return
     */
    public String getDataSourceVoList(String username, boolean isAdmin);

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
    public String getDataSourceVoListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * delete DataSource By Id
     *
     * @param id
     * @return
     */
    public String deleteDataSourceById(String username, boolean isAdmin, String id);

    public String getDataSourceInputPageData(String username, boolean isAdmin, String dataSourceId);
    
    /**
     * checkLinked
     * 
     * @param datasourceId
     * @return
     */
    public String checkLinked(String datasourceId);


}
