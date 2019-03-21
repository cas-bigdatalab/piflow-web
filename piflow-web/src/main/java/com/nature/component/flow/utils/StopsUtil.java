package com.nature.component.flow.utils;

import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.vo.StopsPropertyVo;
import com.nature.component.flow.vo.StopsVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

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
            List<StopsPropertyVo> propertyVos = popertyListPoToVo(stop.getProperties());
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
    public static List<StopsPropertyVo> popertyListPoToVo(List<Property> properties){
        List<StopsPropertyVo> propertiesVo = null;
        if(null!=properties&&properties.size()>0){
            propertiesVo = new ArrayList<StopsPropertyVo>();
            for(Property property:properties){
                if(null!=property){
                    StopsPropertyVo propertyVo = new StopsPropertyVo();
                    BeanUtils.copyProperties(property, propertyVo);
                    propertiesVo.add(propertyVo);
                }
            }
        }
        return propertiesVo;
    }

}
