package cn.cnic.component.stopsComponent.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.model.StopsComponentProperty;
import cn.cnic.third.vo.stop.ThirdStopsComponentPropertyVo;

public class StopsComponentPropertyUtils {

    public static StopsComponentProperty stopsComponentPropertyNewNoId(String username) {

        StopsComponentProperty stopsComponentProperty = new StopsComponentProperty();
        // basic properties (required when creating)
        stopsComponentProperty.setCrtDttm(new Date());
        stopsComponentProperty.setCrtUser(username);
        // basic properties
        stopsComponentProperty.setEnableFlag(true);
        stopsComponentProperty.setLastUpdateUser(username);
        stopsComponentProperty.setLastUpdateDttm(new Date());
        stopsComponentProperty.setVersion(0L);
        return stopsComponentProperty;
    }

    public static StopsComponentProperty initStopsComponentPropertyBasicPropertiesNoId(StopsComponentProperty stopsComponentProperty, String username) {
        if (null == stopsComponentProperty) {
            return stopsComponentPropertyNewNoId(username);
        }
        // basic properties (required when creating)
        stopsComponentProperty.setCrtDttm(new Date());
        stopsComponentProperty.setCrtUser(username);
        // basic properties
        stopsComponentProperty.setEnableFlag(true);
        stopsComponentProperty.setLastUpdateUser(username);
        stopsComponentProperty.setLastUpdateDttm(new Date());
        stopsComponentProperty.setVersion(0L);
        return stopsComponentProperty;
    }

    public static List<StopsComponentProperty> thirdStopsComponentPropertyVoListToStopsComponentProperty(String username, List<ThirdStopsComponentPropertyVo> properties, StopsComponent stopsComponent) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (null == properties || properties.size() <= 0) {
            return null;
        }
        List<StopsComponentProperty> stopsComponentPropertyList = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            StopsComponentProperty stopsComponentProperty = thirdStopsComponentPropertyVoToStopsComponentProperty(username, properties.get(i), stopsComponent);
            if (null == stopsComponentProperty) {
                continue;
            }
            stopsComponentProperty.setPropertySort((long) i);
            stopsComponentPropertyList.add(stopsComponentProperty);
        }
        return stopsComponentPropertyList;
    }

    public static StopsComponentProperty thirdStopsComponentPropertyVoToStopsComponentProperty(String username, ThirdStopsComponentPropertyVo thirdStopsComponentPropertyVo, StopsComponent stopsComponent) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (null == thirdStopsComponentPropertyVo) {
            return null;
        }
        String stopsTemplateId = (null != stopsComponent) ? stopsComponent.getId() : null;
        StopsComponentProperty stopsComponentProperty = stopsComponentPropertyNewNoId(username);
        stopsComponentProperty.setId(UUIDUtils.getUUID32());
        stopsComponentProperty.setDefaultValue(thirdStopsComponentPropertyVo.getDefaultValue());
        stopsComponentProperty.setAllowableValues(thirdStopsComponentPropertyVo.getAllowableValues());
        stopsComponentProperty.setDescription(thirdStopsComponentPropertyVo.getDescription());
        stopsComponentProperty.setDisplayName(thirdStopsComponentPropertyVo.getDisplayName());
        stopsComponentProperty.setName(thirdStopsComponentPropertyVo.getName());
        stopsComponentProperty.setRequired(thirdStopsComponentPropertyVo.getRequired().equals("true"));
        stopsComponentProperty.setSensitive(thirdStopsComponentPropertyVo.isSensitive());
        stopsComponentProperty.setExample(thirdStopsComponentPropertyVo.getExample());
        stopsComponentProperty.setLanguage(thirdStopsComponentPropertyVo.getLanguage());
        stopsComponentProperty.setStopsTemplate(stopsTemplateId);
        return stopsComponentProperty;
    }

}
