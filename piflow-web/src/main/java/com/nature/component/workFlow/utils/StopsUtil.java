package com.nature.component.workFlow.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.vo.PropertyVo;
import com.nature.component.workFlow.vo.StopsVo;

public class StopsUtil {
    /**
     * stopsList实体转Vo
     *
     * @param stopsList
     * @return
     */
    public static List<StopsVo> stopsListPoToVo(List<Stops> stopsList){
        List<StopsVo> stopsVoList = null;
        if(null!=stopsList&&stopsList.size()>0){
            stopsVoList = new ArrayList<StopsVo>();
            for (Stops stop:stopsList){
                StopsVo stopsVo = stopPoToVo(stop);
                if(null!=stopsVo){
                    stopsVoList.add(stopsVo);
                }
            }
        }
        return stopsVoList;
    }

    /**
     * stop实体转Vo
     *
     * @param stop
     * @return
     */
    public static StopsVo stopPoToVo(Stops stop){
        StopsVo stopsVo = null;
        if (null != stop) {
            stopsVo = new StopsVo();
            BeanUtils.copyProperties(stop, stopsVo);
            List<PropertyVo> propertyVos = popertyListPoToVo(stop.getProperties());
            stopsVo.setPropertiesVo(propertyVos);
        }
        return stopsVo;
    }

    /**
     * PropertyList(properties)实体转Vo
     *
     * @param properties
     * @return
     */
    public static List<PropertyVo> popertyListPoToVo(List<Property> properties){
        List<PropertyVo> propertiesVo = null;
        if(null!=properties&&properties.size()>0){
            propertiesVo = new ArrayList<PropertyVo>();
            for(Property property:properties){
                if(null!=property){
                    PropertyVo propertyVo = new PropertyVo();
                    BeanUtils.copyProperties(property, propertyVo);
                    propertiesVo.add(propertyVo);
                }
            }
        }
        return propertiesVo;
    }

}
