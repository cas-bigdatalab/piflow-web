package cn.cnic.component.dataSource.transactional;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import cn.cnic.component.dataSource.mapper.DataSourceMapper;
import cn.cnic.component.dataSource.mapper.DataSourcePropertyMapper;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class DataSourceTransaction {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private DataSourcePropertyMapper dataSourcePropertyMapper;

    public DataSource saveOrUpdate(DataSource dataSource) throws Exception {
        if (null == dataSource) {
            return null;
        }
        if (StringUtils.isBlank(dataSource.getId())) {
            return insert(dataSource);
        } else {
            return update(dataSource);
        }
    }

    /**
     * Insert DataSource
     *
     * @param dataSource
     * @return
     */
    public DataSource insert(DataSource dataSource) throws Exception {
        if (null == dataSource) {
            return null;
        }
        int addDataSource = dataSourceMapper.addDataSource(dataSource);
        if (addDataSource <= 0) {
            return null;
        }
        List<DataSourceProperty> dataSourcePropertyList = dataSource.getDataSourcePropertyList();
        if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                if (null == dataSourceProperty) {
                    continue;
                }
                dataSourceProperty.setDataSource(dataSource);
            }
            int addDataSourcePropertyList = dataSourcePropertyMapper.addDataSourcePropertyList(dataSourcePropertyList);
            if (addDataSourcePropertyList <= 0) {
                throw new Exception("datasource insert failed");
            }
        }
        return dataSource;
    }

    /**
     * Insert DataSource
     *
     * @param dataSource
     * @return
     */
    public DataSource update(DataSource dataSource) throws Exception {
        if (null == dataSource) {
            return null;
        }
        int updateDataSource = dataSourceMapper.updateDataSource(dataSource);
        if (updateDataSource <= 0) {
            return null;
        }
        List<DataSourceProperty> dataSourcePropertyList = dataSource.getDataSourcePropertyList();
        if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
            int addDataSourcePropertyList = 0;
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                if (null == dataSourceProperty) {
                    continue;
                }
                dataSourceProperty.setDataSource(dataSource);
                if (StringUtils.isBlank(dataSourceProperty.getId())) {
                    dataSourceProperty.setId(UUIDUtils.getUUID32());
                    addDataSourcePropertyList = dataSourcePropertyMapper.addDataSourceProperty(dataSourceProperty);
                } else {
                    addDataSourcePropertyList = dataSourcePropertyMapper.updateDataSourceProperty(dataSourceProperty);
                }
                if (addDataSourcePropertyList <= 0) {
                    throw new Exception("datasource insert failed");
                }
            }
        }
        return dataSource;
    }

    public DataSource getDataSourceById(String username, boolean isAdmin, String id) {
        return dataSourceMapper.getDataSourceByIdAndUser(username, isAdmin, id);
    }

    public List<DataSource> getDataSourceTemplateList() {
        return dataSourceMapper.getDataSourceTemplateList();
    }

    public List<DataSource> getDataSourceList(String username, boolean isAdmin) {
        return dataSourceMapper.getDataSourceList(username, isAdmin);
    }

    public List<DataSourceVo> getDataSourceVoListParam(String username, boolean isAdmin, String param) {
        return dataSourceMapper.getDataSourceVoListParam(username, isAdmin, param);
    }

}
