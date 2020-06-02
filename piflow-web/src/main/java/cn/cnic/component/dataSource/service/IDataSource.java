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
    public String saveOrUpdate(boolean isAdmin, String username, DataSourceVo dataSourceVo);

    /**
     * Query DataSourceVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public DataSourceVo dataSourceVoById(boolean isAdmin, String username, String id);

    /**
     * Query DataSourceVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public String getDataSourceVoById(boolean isAdmin, String username, String id);

    /**
     * getDataSourceVoList
     *
     * @param isTemplate
     * @return
     */
    public String getDataSourceVoList(boolean isAdmin, String username);

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
    public String getDataSourceVoListPage(boolean isAdmin, String username, Integer offset, Integer limit, String param);

    /**
     * delete DataSource By Id
     *
     * @param id
     * @return
     */
    public String deleteDataSourceById(boolean isAdmin, String username,String id);


}
