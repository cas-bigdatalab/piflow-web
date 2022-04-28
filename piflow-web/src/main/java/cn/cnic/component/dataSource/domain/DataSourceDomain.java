package cn.cnic.component.dataSource.domain;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import cn.cnic.component.dataSource.mapper.DataSourceMapper;
import cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class DataSourceDomain {

    private Logger logger = LoggerUtil.getLogger();

    private final DataSourceMapper dataSourceMapper;
    private final DataSourcePropertyMapper dataSourcePropertyMapper;

    @Autowired
    public DataSourceDomain(DataSourceMapper dataSourceMapper,
                            DataSourcePropertyMapper dataSourcePropertyMapper) {
        this.dataSourceMapper = dataSourceMapper;
        this.dataSourcePropertyMapper = dataSourcePropertyMapper;
    }

    /**
     * saveOrUpdate
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    public DataSource saveOrUpdate(DataSource dataSource) throws Exception {
        if (null == dataSource) {
            return null;
        }
        if (StringUtils.isBlank(dataSource.getId())) {
            return insertDataSource(dataSource);
        } else {
            return updateDataSource(dataSource);
        }
    }

    /**
     * Insert DataSource
     *
     * @param dataSource
     * @return
     */
    public DataSource insertDataSource(DataSource dataSource) throws Exception {
        if (null == dataSource) {
            return null;
        }
        int addDataSource = dataSourceMapper.addDataSource(dataSource);
        if (addDataSource <= 0) {
            throw new Exception("dataSource insert failed");
        }
        List<DataSourceProperty> dataSourcePropertyList = dataSource.getDataSourcePropertyList();
        if (null == dataSourcePropertyList || dataSourcePropertyList.size() <= 0) {
            return dataSource;
        }
        for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
            if (null == dataSourceProperty) {
                continue;
            }
            dataSourceProperty.setDataSource(dataSource);
        }
        insertDataSourcePropertyList(dataSourcePropertyList);
        return dataSource;
    }

    /**
     * insertDataSourcePropertyList
     *
     * @param dataSourcePropertyList
     * @return
     * @throws Exception
     */
    public int insertDataSourcePropertyList(List<DataSourceProperty> dataSourcePropertyList) throws Exception {
        if (null == dataSourcePropertyList || dataSourcePropertyList.size() <= 0) {
            return 0;
        }
        int affectedRows = dataSourcePropertyMapper.addDataSourcePropertyList(dataSourcePropertyList);
        if (affectedRows <= 0) {
            throw new Exception("dataSourcePropertyList insert failed");
        }
        logger.debug("insertDataSourcePropertyList Affected Rows : " + affectedRows);
        return affectedRows;
    }

    /**
     * insertDataSourceProperty
     *
     * @param dataSourceProperty
     * @return
     * @throws Exception
     */
    public DataSourceProperty insertDataSourceProperty(DataSourceProperty dataSourceProperty) throws Exception {
        int affectedRows = dataSourcePropertyMapper.addDataSourceProperty(dataSourceProperty);
        if (affectedRows <= 0) {
            throw new Exception("dataSourceProperty insert failed");
        }
        logger.debug("insertDataSourceProperty Affected Rows : " + affectedRows);
        return dataSourceProperty;
    }

    /**
     * update DataSource
     *
     * @param dataSource
     * @return
     */
    public DataSource updateDataSource(DataSource dataSource) throws Exception {
        if (null == dataSource) {
            return null;
        }
        int updateDataSource = dataSourceMapper.updateDataSource(dataSource);
        if (updateDataSource <= 0) {
            return null;
        }
        List<DataSourceProperty> dataSourcePropertyList = dataSource.getDataSourcePropertyList();
        if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                if (null == dataSourceProperty) {
                    continue;
                }
                dataSourceProperty.setDataSource(dataSource);
                saveOrUpdateDataSourceProperty(dataSourceProperty);
            }
        }
        return dataSource;
    }

    /**
     * saveOrUpdateDataSourceProperty
     *
     * @param dataSourceProperty
     * @return
     * @throws Exception
     */
    public DataSourceProperty saveOrUpdateDataSourceProperty(DataSourceProperty dataSourceProperty) throws Exception {
        if (null == dataSourceProperty) {
            return null;
        }
        int affectedRows = 0;
        if (StringUtils.isBlank(dataSourceProperty.getId())) {
            dataSourceProperty.setId(UUIDUtils.getUUID32());
            affectedRows = dataSourcePropertyMapper.addDataSourceProperty(dataSourceProperty);
            if (affectedRows <= 0) {
                throw new Exception("datasource insert failed");
            }
        } else {
            affectedRows = dataSourcePropertyMapper.updateDataSourceProperty(dataSourceProperty);
            logger.debug("insertDataSourceProperty Affected Rows : " + affectedRows);
        }
        return dataSourceProperty;
    }

    /**
     * getDataSourceById
     *
     * @param username
     * @param isAdmin
     * @param id
     * @return
     */
    public DataSource getDataSourceById(String username, boolean isAdmin, String id) {
        return dataSourceMapper.getDataSourceByIdAndUser(username, isAdmin, id);
    }

    /**
     * getDataSourceTemplateList
     *
     * @return
     */
    public List<DataSource> getDataSourceTemplateList() {
        return dataSourceMapper.getDataSourceTemplateList();
    }

    /**
     * getDataSourceList
     *
     * @param username
     * @param isAdmin
     * @return
     */
    public List<DataSource> getDataSourceList(String username, boolean isAdmin) {
        return dataSourceMapper.getDataSourceList(username, isAdmin);
    }

    /**
     * getDataSourceVoListParam
     *
     * @param username
     * @param isAdmin
     * @param param
     * @return
     */
    public List<DataSourceVo> getDataSourceVoListParam(String username, boolean isAdmin, String param) {
        return dataSourceMapper.getDataSourceVoListParam(username, isAdmin, param);
    }

    /**
     *
     * @param dataSourceId
     * @return
     */
    public List<DataSourceProperty>getDataSourcePropertyListByDataSourceId(String dataSourceId){
        return dataSourcePropertyMapper.getDataSourcePropertyListByDataSourceId(dataSourceId);
    }

}
