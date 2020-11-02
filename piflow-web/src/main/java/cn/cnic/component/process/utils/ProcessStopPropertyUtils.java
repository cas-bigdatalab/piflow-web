package cn.cnic.component.process.utils;

import cn.cnic.component.process.entity.ProcessStopProperty;

import java.util.Date;

public class ProcessStopPropertyUtils {

    public static ProcessStopProperty processStopPropertyNewNoId(String username) {
        ProcessStopProperty processStopProperty = new ProcessStopProperty();
        // basic properties (required when creating)
        processStopProperty.setCrtDttm(new Date());
        processStopProperty.setCrtUser(username);
        // basic properties
        processStopProperty.setEnableFlag(true);
        processStopProperty.setLastUpdateUser(username);
        processStopProperty.setLastUpdateDttm(new Date());
        processStopProperty.setVersion(0L);
        return processStopProperty;
    }

    public static ProcessStopProperty initProcessStopPropertyBasicPropertiesNoId(ProcessStopProperty processStopProperty, String username) {
        if (null == processStopProperty) {
            return processStopPropertyNewNoId(username);
        }
        // basic properties (required when creating)
        processStopProperty.setCrtDttm(new Date());
        processStopProperty.setCrtUser(username);
        // basic properties
        processStopProperty.setEnableFlag(true);
        processStopProperty.setLastUpdateUser(username);
        processStopProperty.setLastUpdateDttm(new Date());
        processStopProperty.setVersion(0L);
        return processStopProperty;
    }

}
