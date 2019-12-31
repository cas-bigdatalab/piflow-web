package com.nature.component.flow.utils;

import com.nature.component.dataSource.model.DataSource;
import com.nature.component.dataSource.model.DataSourceProperty;
import com.nature.component.dataSource.vo.DataSourceVo;
import com.nature.component.flow.model.CustomizedProperty;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.vo.StopsCustomizedPropertyVo;
import com.nature.component.flow.vo.StopsPropertyVo;
import com.nature.component.flow.vo.StopsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class StopsUtil {
    /**
     * stopsList Po To Vo
     *
     * @param stopsList
     * @return
     */
    public static List<StopsVo> stopsListPoToVo(List<Stops> stopsList) {
        List<StopsVo> stopsVoList = null;
        if (null != stopsList && stopsList.size() > 0) {
            stopsVoList = new ArrayList<StopsVo>();
            for (Stops stop : stopsList) {
                StopsVo stopsVo = stopPoToVo(stop);
                if (null != stopsVo) {
                    stopsVoList.add(stopsVo);
                }
            }
        }
        return stopsVoList;
    }

    /**
     * stop Po To Vo
     *
     * @param stop
     * @return
     */
    public static StopsVo stopPoToVo(Stops stop) {
        StopsVo stopsVo = null;
        if (null != stop) {
            stopsVo = new StopsVo();
            BeanUtils.copyProperties(stop, stopsVo);
            // datasource Property Map(Key is the attribute name)
            Map<String, String> dataSourcePropertyMap = new HashMap<>();
            DataSource dataSource = stop.getDataSource();
            if (null != dataSource) {
                DataSourceVo dataSourceVo = new DataSourceVo();
                BeanUtils.copyProperties(dataSource, dataSourceVo);
                stopsVo.setDataSourceVo(dataSourceVo);
                List<DataSourceProperty> dataSourcePropertyList = dataSource.getDataSourcePropertyList();
                // Determine whether the Datasource attribute whose ID is "dataSourceId" is empty. Returns if it is empty, otherwise it is converted to Map.
                if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
                    // Loop "datasource" attribute to map
                    for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                        // "datasource" attribute name
                        String dataSourcePropertyName = dataSourceProperty.getName();
                        // Judge empty and lowercase
                        if (StringUtils.isNotBlank(dataSourcePropertyName)) {
                            dataSourcePropertyName = dataSourcePropertyName.toLowerCase();
                        }
                        dataSourcePropertyMap.put(dataSourcePropertyName, dataSourceProperty.getValue());
                    }
                }
            }
            List<StopsPropertyVo> propertyVos = popertyListPoToVo(stop.getProperties(), dataSourcePropertyMap);
            stopsVo.setPropertiesVo(propertyVos);
            List<StopsCustomizedPropertyVo> stopsCustomizedPropertyVoList = customizedPropertyListPoToVo(stop.getCustomizedPropertyList());
            stopsVo.setStopsCustomizedPropertyVoList(stopsCustomizedPropertyVoList);
        }
        return stopsVo;
    }

    /**
     * PropertyList(properties)Po To Vo
     *
     * @param properties
     * @param dataSourcePropertyMap
     * @return
     */
    public static List<StopsPropertyVo> popertyListPoToVo(List<Property> properties, Map<String, String> dataSourcePropertyMap) {
        List<StopsPropertyVo> propertiesVo = null;
        if (null != properties && properties.size() > 0) {
            propertiesVo = new ArrayList<StopsPropertyVo>();
            for (Property property : properties) {
                if (null != property) {
                    StopsPropertyVo propertyVo = new StopsPropertyVo();
                    BeanUtils.copyProperties(property, propertyVo);
                    // "stop" attribute isSelect
                    Boolean isLocked = propertyVo.getIsLocked();
                    // Judge empty
                    if (null != isLocked && isLocked) {
                        // "stop" attribute name
                        String name = property.getName();
                        // Judge empty
                        if (StringUtils.isNotBlank(name)) {
                            // Go to the map of the "datasource" attribute
                            String value = dataSourcePropertyMap.get(name.toLowerCase());
                            // Judge empty
                            if (StringUtils.isNotBlank(value)) {
                                propertyVo.setCustomValue(value);
                            }
                        }
                    }
                    propertiesVo.add(propertyVo);
                }
            }
        }
        return propertiesVo;
    }

    /**
     * CustomizedPropertyList(properties)Po To Vo
     *
     * @param customizedPropertyList
     * @return
     */
    public static List<StopsCustomizedPropertyVo> customizedPropertyListPoToVo(List<CustomizedProperty> customizedPropertyList) {
        List<StopsCustomizedPropertyVo> propertiesVo = null;
        if (null != customizedPropertyList && customizedPropertyList.size() > 0) {

            propertiesVo = new ArrayList<>();
            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                if (null != customizedProperty) {
                    StopsCustomizedPropertyVo customizedPropertyVo = new StopsCustomizedPropertyVo();
                    BeanUtils.copyProperties(customizedProperty, customizedPropertyVo);
                    propertiesVo.add(customizedPropertyVo);
                }
            }
        }
        return propertiesVo;
    }

}
