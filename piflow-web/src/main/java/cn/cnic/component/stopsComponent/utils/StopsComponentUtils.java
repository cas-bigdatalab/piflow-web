package cn.cnic.component.stopsComponent.utils;

import cn.cnic.base.util.ImageUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.stopsComponent.model.PropertyTemplate;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.component.stopsComponent.model.StopsTemplate;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class StopsComponentUtils {

    public static StopsTemplate stopsComponentNewNoId(String username) {

        StopsTemplate stopsComponent = new StopsTemplate();
        // basic properties (required when creating)
        stopsComponent.setCrtDttm(new Date());
        stopsComponent.setCrtUser(username);
        // basic properties
        stopsComponent.setEnableFlag(true);
        stopsComponent.setLastUpdateUser(username);
        stopsComponent.setLastUpdateDttm(new Date());
        stopsComponent.setVersion(0L);
        return stopsComponent;
    }

    public static StopsTemplate initStopsComponentBasicPropertiesNoId(StopsTemplate stopsComponent, String username) {
        if (null == stopsComponent) {
            return stopsComponentNewNoId(username);
        }
        // basic properties (required when creating)
        stopsComponent.setCrtDttm(new Date());
        stopsComponent.setCrtUser(username);
        // basic properties
        stopsComponent.setEnableFlag(true);
        stopsComponent.setLastUpdateUser(username);
        stopsComponent.setLastUpdateDttm(new Date());
        stopsComponent.setVersion(0L);
        return stopsComponent;
    }

    public static StopsTemplate thirdStopsComponentVoToStopsTemplate(String username, ThirdStopsComponentVo thirdStopsComponentVo, List<StopsComponentGroup> stopGroupByName) {
        if (null == thirdStopsComponentVo) {
            return null;
        }
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (null == stopGroupByName || stopGroupByName.size() > 0) {
            return null;
        }
        String inports = thirdStopsComponentVo.getInports();
        PortType inPortType = null;
        if (StringUtils.isNotBlank(inports)) {
            for (PortType value : PortType.values()) {
                if (inports.equalsIgnoreCase(value.getValue())) {
                    inPortType = value;
                }
            }
            if (null == inPortType) {
                inPortType = PortType.USER_DEFAULT;
            }
        }
        PortType.selectGenderByValue(inports);
        String outports = thirdStopsComponentVo.getOutports();
        PortType outPortType = null;
        if (StringUtils.isNotBlank(outports)) {
            for (PortType value : PortType.values()) {
                if (outports.equalsIgnoreCase(value.getValue())) {
                    outPortType = value;
                }
            }
            if (null == outPortType) {
                outPortType = PortType.USER_DEFAULT;
            }
        }
        String icon = thirdStopsComponentVo.getIcon();
        if (StringUtils.isNotBlank(icon)) {
            ImageUtils.generateImage(icon, thirdStopsComponentVo.getName() + "_128x128", "png", SysParamsCache.IMAGES_PATH);
        }
        StopsTemplate stopsTemplate = stopsComponentNewNoId(username);
        stopsTemplate.setId(SqlUtils.getUUID32());
        stopsTemplate.setBundel(thirdStopsComponentVo.getBundle());
        stopsTemplate.setDescription(thirdStopsComponentVo.getDescription());
        stopsTemplate.setGroups(thirdStopsComponentVo.getGroups());
        stopsTemplate.setName(thirdStopsComponentVo.getName());
        stopsTemplate.setInports(inports);
        stopsTemplate.setInPortType(inPortType);
        stopsTemplate.setOutports(outports);
        stopsTemplate.setOutPortType(outPortType);
        stopsTemplate.setOwner(thirdStopsComponentVo.getOwner());
        stopsTemplate.setIsCustomized(thirdStopsComponentVo.isCustomized());
        stopsTemplate.setStopGroupList(stopGroupByName);
        List<PropertyTemplate> listPropertyTemplate = StopsComponentPropertyVoUtils.thirdStopsComponentPropertyVoListToStopsComponentProperty(username, thirdStopsComponentVo.getProperties(), stopsTemplate);
        stopsTemplate.setProperties(listPropertyTemplate);
        return stopsTemplate;
    }

}
