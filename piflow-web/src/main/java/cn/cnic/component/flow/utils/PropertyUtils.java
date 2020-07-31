
package cn.cnic.component.flow.utils;

import cn.cnic.component.flow.entity.Property;

import java.util.Date;

public class PropertyUtils {

    public static Property propertyNewNoId(String username) {

        Property property = new Property();
        // basic properties (required when creating)
        property.setCrtDttm(new Date());
        property.setCrtUser(username);
        // basic properties
        property.setEnableFlag(true);
        property.setLastUpdateUser(username);
        property.setLastUpdateDttm(new Date());
        property.setVersion(0L);
        return property;
    }

}
