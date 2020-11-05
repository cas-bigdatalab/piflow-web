package cn.cnic.component.stopsComponent.utils;

import cn.cnic.base.util.ImageUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.stopsComponent.model.StopsComponentProperty;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class StopsComponentUtils {

    public static StopsComponent stopsComponentNewNoId(String username) {

        StopsComponent stopsComponent = new StopsComponent();
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

    public static StopsComponent initStopsComponentBasicPropertiesNoId(StopsComponent stopsComponent, String username) {
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

    public static StopsComponent thirdStopsComponentVoToStopsTemplate(String username, ThirdStopsComponentVo thirdStopsComponentVo, List<StopsComponentGroup> stopGroupByName) {
        if (null == thirdStopsComponentVo) {
            return null;
        }
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (null == stopGroupByName || stopGroupByName.size() <= 0) {
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
        StopsComponent stopsComponent = stopsComponentNewNoId(username);
        stopsComponent.setId(UUIDUtils.getUUID32());
        stopsComponent.setBundel(thirdStopsComponentVo.getBundle());
        stopsComponent.setDescription(thirdStopsComponentVo.getDescription());
        stopsComponent.setGroups(thirdStopsComponentVo.getGroups());
        stopsComponent.setName(thirdStopsComponentVo.getName());
        stopsComponent.setInports(inports);
        stopsComponent.setInPortType(inPortType);
        stopsComponent.setOutports(outports);
        stopsComponent.setOutPortType(outPortType);
        stopsComponent.setOwner(thirdStopsComponentVo.getOwner());
        stopsComponent.setIsCustomized(thirdStopsComponentVo.isCustomized());
        stopsComponent.setStopGroupList(stopGroupByName);
        stopsComponent.setVisualizationType(thirdStopsComponentVo.getVisualizationType());
        List<StopsComponentProperty> listStopsComponentProperty = StopsComponentPropertyUtils.thirdStopsComponentPropertyVoListToStopsComponentProperty(username, thirdStopsComponentVo.getProperties(), stopsComponent);
        stopsComponent.setProperties(listStopsComponentProperty);
        return stopsComponent;
    }

}
