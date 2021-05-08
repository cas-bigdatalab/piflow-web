package cn.cnic.component.flow.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.flow.entity.CustomizedProperty;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.CustomizedPropertyMapper;
import cn.cnic.component.flow.mapper.PropertyMapper;
import cn.cnic.component.flow.mapper.StopsMapper;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class StopsDomainU {

    @Autowired
    private StopsMapper stopsMapper;

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private CustomizedPropertyMapper customizedPropertyMapper;
    
    public int saveOrUpdate(Stops stops) throws Exception {
        if (null == stops) {
            throw new Exception("save failed, stops is null");
        }
        if (StringUtils.isBlank(stops.getId())) {
            return addStops(stops);
        }
        return updateStops(stops);
    }

    public int addStops(Stops stops) throws Exception {
        if (null == stops) {
            throw new Exception("save failed");
        }
        String id = stops.getId();
        if (StringUtils.isBlank(id)) {
            stops.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = stopsMapper.addStops(stops);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        List<Property> properties = stops.getProperties();
        if (null != properties && properties.size() > 0) {
            for (Property property : properties) {
                if (null == property) {
                    throw new Exception("save failed");
                }
                property.setStops(stops);
                affectedRows += addProperty(property);
            }
        }
        List<Property> oldProperties = stops.getOldProperties();
        if (null != oldProperties && oldProperties.size() > 0) {
            for (Property property : oldProperties) {
                if (null == property) {
                    throw new Exception("save failed");
                }
                property.setStops(stops);
                affectedRows += addProperty(property);
            }
        }
        List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
        if (null != customizedPropertyList && customizedPropertyList.size() > 0) {
            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                if (null == customizedProperty) {
                    throw new Exception("save failed");
                }
                customizedProperty.setStops(stops);
                affectedRows += addCustomizedProperty(customizedProperty);
            }
        }
        return affectedRows;
    }

    public int addProperty(Property property) throws Exception {
        if (null == property) {
            throw new Exception("save failed");
        }
        int affectedRows = propertyMapper.addProperty(property);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int addCustomizedProperty(CustomizedProperty customizedProperty) throws Exception {
        if (null == customizedProperty) {
            throw new Exception("save failed");
        }
        int affectedRows = customizedPropertyMapper.addCustomizedProperty(customizedProperty);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    
    public int updateStops(Stops stops) throws Exception {
        if (null == stops) {
            return 0;
        }
        int affectedRows = stopsMapper.updateStops(stops);
        List<Property> properties = stops.getProperties();
        if (null != properties && properties.size() > 0) {
            for (Property property : properties) {
                if (null == property) {
                    return 0;
                }
                property.setStops(stops);
                if (StringUtils.isBlank(property.getId())) {
                    affectedRows += addProperty(property);
                    continue;
                }
                affectedRows += updateProperty(property);
            }
        }
        List<Property> oldProperties = stops.getOldProperties();
        if (null != oldProperties && oldProperties.size() > 0) {
            for (Property property : oldProperties) {
                if (null == property) {
                    return 0;
                }
                property.setStops(stops);
                if (StringUtils.isBlank(property.getId())) {
                    affectedRows += addProperty(property);
                    continue;
                }
                affectedRows += updateProperty(property);
            }
        }
        List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
        if (null != customizedPropertyList && customizedPropertyList.size() > 0) {
            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                if (null == customizedProperty) {
                    return 0;
                }
                customizedProperty.setStops(stops);
                if (StringUtils.isBlank(customizedProperty.getId())) {
                    affectedRows += addCustomizedProperty(customizedProperty);
                    continue;
                }
                affectedRows += updateCustomizedProperty(customizedProperty);
            }
        }
        return affectedRows;
    }

    public int updateProperty(Property property) {
        if (null == property) {
            return 0;
        }
        return propertyMapper.updateStopsProperty(property);
    }

    public int updateCustomizedProperty(CustomizedProperty customizedProperty) {
        if (null == customizedProperty) {
            return 0;
        }
        return customizedPropertyMapper.updateStopsCustomizedProperty(customizedProperty);
        
    }
    
    public Stops getStopsById(String id) {
        return stopsMapper.getStopsById(id);
    }

    public List<Stops> getStopsList() {
        return stopsMapper.getStopsList();
    }

    public Integer getMaxStopPageIdByFlowId(String flowId) {
        return stopsMapper.getMaxStopPageIdByFlowId(flowId);
    }

    public String[] getStopNamesByFlowId(String flowId) {
        return stopsMapper.getStopNamesByFlowId(flowId);
    }

    public int updateStopsProperty(Property property) {
        return propertyMapper.updateStopsProperty(property);
    }

    public int addPropertyList(List<Property> propertyList) {
        return propertyMapper.addPropertyList(propertyList);
    }

    public int updateEnableFlagByFlowId(String username, String id) {
        return stopsMapper.updateEnableFlagByFlowId(username, id);
    }
    
    /**
     * Verify that stopname is duplicated
     *
     * @param flowId
     * @param stopName
     * @return
     */
    public String getStopByNameAndFlowId(String flowId, String stopName) {
        return stopsMapper.getStopByNameAndFlowId(flowId, stopName);
    }
    
    public List<Stops> getStopsListByFlowIdAndPageIds(String flowId, String[] pageIds) {
        return stopsMapper.getStopsListByFlowIdAndPageIds(flowId,pageIds);
    }
    
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo) {
        return stopsMapper.updateStopsByFlowIdAndName(stopVo);
    }
    
    public List<String> getStopsNamesByDatasourceId(String datasourecId) {
        return stopsMapper.getStopsNamesByDatasourceId(datasourecId);
    }

    public List<Stops> getStopsListByDatasourceId(String datasourecId) {
        return stopsMapper.getStopsListByDatasourceId(datasourecId);
    }


}