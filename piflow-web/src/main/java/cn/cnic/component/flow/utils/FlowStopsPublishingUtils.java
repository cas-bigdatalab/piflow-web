package cn.cnic.component.flow.utils;

import cn.cnic.component.flow.entity.FlowStopsPublishing;

import java.util.*;

public class FlowStopsPublishingUtils {

    public static FlowStopsPublishing flowStopsPublishingNewNoId(String username) {

        FlowStopsPublishing flowStopsPublishing = new FlowStopsPublishing();
        // basic properties (required when creating)
        flowStopsPublishing.setCrtDttm(new Date());
        flowStopsPublishing.setCrtUser(username);
        // basic properties
        flowStopsPublishing.setEnableFlag(true);
        flowStopsPublishing.setLastUpdateUser(username);
        flowStopsPublishing.setLastUpdateDttm(new Date());
        flowStopsPublishing.setVersion(0L);
        return flowStopsPublishing;
    }

    public static FlowStopsPublishing initFlowStopsPublishingBasicPropertiesNoId(FlowStopsPublishing flowStopsPublishing, String username) {
        if (null == flowStopsPublishing) {
            return flowStopsPublishingNewNoId(username);
        }
        // basic properties (required when creating)
        flowStopsPublishing.setCrtDttm(new Date());
        flowStopsPublishing.setCrtUser(username);
        // basic properties
        flowStopsPublishing.setEnableFlag(true);
        flowStopsPublishing.setLastUpdateUser(username);
        flowStopsPublishing.setLastUpdateDttm(new Date());
        flowStopsPublishing.setVersion(0L);
        return flowStopsPublishing;
    }

}
