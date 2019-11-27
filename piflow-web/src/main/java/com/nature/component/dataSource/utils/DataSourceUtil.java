package com.nature.component.dataSource.utils;

import com.nature.component.dataSource.model.DataSource;
import com.nature.component.dataSource.model.DataSourceProperty;
import com.nature.component.dataSource.vo.DataSourcePropertyVo;
import com.nature.component.dataSource.vo.DataSourceVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class DataSourceUtil {
    /**
     * dataSource Po To Vo
     *
     * @param dataSource
     * @param isToAll
     * @return
     */
    public static DataSourceVo dataSourcePoToVo(DataSource dataSource, boolean isToAll) {
        DataSourceVo dataSourceVo = null;
        if (null != dataSource) {
            dataSourceVo = new DataSourceVo();
            BeanUtils.copyProperties(dataSource, dataSourceVo);
            if (isToAll) {
                List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourcePropertyListPoToVo(dataSource.getDataSourcePropertyList());
                if (null != dataSourcePropertyVoList) {
                    dataSourceVo.setDataSourcePropertyVoList(dataSourcePropertyVoList);
                }
            }
        }
        return dataSourceVo;
    }

    /**
     * dataSourcePropertyList Po To Vo
     *
     * @param dataSourcePropertyList
     * @return
     */
    public static List<DataSourcePropertyVo> dataSourcePropertyListPoToVo(List<DataSourceProperty> dataSourcePropertyList) {
        List<DataSourcePropertyVo> dataSourcePropertyVoList = null;
        DataSourcePropertyVo dataSourcePropertyVo = null;
        if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
            dataSourcePropertyVoList = new ArrayList<>();
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                dataSourcePropertyVo = new DataSourcePropertyVo();
                BeanUtils.copyProperties(dataSourceProperty, dataSourcePropertyVo);
                dataSourcePropertyVoList.add(dataSourcePropertyVo);
            }
        }
        return dataSourcePropertyVoList;
    }

    /**
     * dataSourceList Po To Vo
     *
     * @param dataSourceList
     * @return
     */
    public static List<DataSourceVo> dataSourceListPoToVo(List<DataSource> dataSourceList, boolean isToAll) {
        List<DataSourceVo> dataSourceVoList = null;
        if (null != dataSourceList && dataSourceList.size() > 0) {
            dataSourceVoList = new ArrayList<>();
            DataSourceVo dataSourceVo = null;
            for (DataSource dataSource : dataSourceList) {
                dataSourceVo = dataSourcePoToVo(dataSource, isToAll);
                if (null != dataSource) {
                    dataSourceVoList.add(dataSourceVo);
                }
            }
        }
        return dataSourceVoList;
    }
}
