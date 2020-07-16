package cn.cnic.component.dataSource.domain;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.dataSource.model.DataSource;
import cn.cnic.component.dataSource.model.DataSourceProperty;
import cn.cnic.component.process.model.Process;
import cn.cnic.component.process.model.ProcessPath;
import cn.cnic.component.process.model.ProcessStop;
import cn.cnic.component.process.model.ProcessStopProperty;
import cn.cnic.mapper.dataSource.DataSourceMapper;
import cn.cnic.mapper.dataSource.DataSourcePropertyMapper;
import cn.cnic.mapper.process.ProcessMapper;
import cn.cnic.mapper.process.ProcessPathMapper;
import cn.cnic.mapper.process.ProcessStopMapper;
import cn.cnic.mapper.process.ProcessStopPropertyMapper;
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
            int addDataSourcePropertyList = 0;
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                if (null == dataSourceProperty) {
                    continue;
                }
                dataSourceProperty.setDataSource(dataSource);
                if (StringUtils.isBlank(dataSource.getId())) {
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

}
