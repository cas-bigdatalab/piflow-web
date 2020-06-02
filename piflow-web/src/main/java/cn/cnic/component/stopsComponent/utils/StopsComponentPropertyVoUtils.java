package cn.cnic.component.stopsComponent.utils;

import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.PropertyTemplate;
import cn.cnic.component.stopsComponent.model.StopsTemplate;
import cn.cnic.third.vo.stop.ThirdStopsComponentPropertyVo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StopsComponentPropertyVoUtils {

    public static PropertyTemplate stopsComponentPropertyNewNoId(String username) {

        PropertyTemplate stopsComponentProperty = new PropertyTemplate();
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

    public static PropertyTemplate initStopsComponentPropertyBasicPropertiesNoId(PropertyTemplate stopsComponentProperty, String username) {
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

    public static List<PropertyTemplate> thirdStopsComponentPropertyVoListToStopsComponentProperty(String username, List<ThirdStopsComponentPropertyVo> properties, StopsTemplate stopsTemplate) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (null == properties || properties.size() > 0) {
            return null;
        }
        List<PropertyTemplate> stopsComponentPropertyList = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            PropertyTemplate stopsComponentProperty = thirdStopsComponentPropertyVoToStopsComponentProperty(username, properties.get(i), stopsTemplate);
            if (null == stopsComponentProperty) {
                continue;
            }
            stopsComponentProperty.setPropertySort((long) i);
            stopsComponentPropertyList.add(stopsComponentProperty);
        }
        return stopsComponentPropertyList;
    }

    public static PropertyTemplate thirdStopsComponentPropertyVoToStopsComponentProperty(String username, ThirdStopsComponentPropertyVo thirdStopsComponentPropertyVo, StopsTemplate stopsTemplate) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (null == thirdStopsComponentPropertyVo) {
            return null;
        }
        String stopsTemplateId = (null != stopsTemplate) ? stopsTemplate.getId() : null;
        String[] allowableValues = thirdStopsComponentPropertyVo.getAllowableValues();
        for (int i = 0; i < allowableValues.length; i++) {
            allowableValues[i] = "\"" + allowableValues[i] + "\"";
        }
        PropertyTemplate stopsComponentProperty = stopsComponentPropertyNewNoId(username);
        stopsComponentProperty.setId(SqlUtils.getUUID32());
        stopsComponentProperty.setDefaultValue(thirdStopsComponentPropertyVo.getDefaultValue());
        stopsComponentProperty.setAllowableValues(Arrays.toString(allowableValues));
        stopsComponentProperty.setDescription(thirdStopsComponentPropertyVo.getDescription());
        stopsComponentProperty.setDisplayName(thirdStopsComponentPropertyVo.getDisplayName());
        stopsComponentProperty.setName(thirdStopsComponentPropertyVo.getName());
        stopsComponentProperty.setRequired(thirdStopsComponentPropertyVo.getRequired().equals("true"));
        stopsComponentProperty.setSensitive(thirdStopsComponentPropertyVo.isSensitive());
        stopsComponentProperty.setStopsTemplate(stopsTemplateId);
        return stopsComponentProperty;
    }

}
