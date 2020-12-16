package cn.cnic.component.stopsComponent.utils;

import java.util.Date;

import cn.cnic.component.stopsComponent.model.StopsHub;

public class StopsHubUtils {

    public static StopsHub stopsHubNewNoId(String username) {

        StopsHub stopsHub = new StopsHub();
        // basic properties (required when creating)
        stopsHub.setCrtDttm(new Date());
        stopsHub.setCrtUser(username);
        // basic properties
        stopsHub.setEnableFlag(true);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHub.setVersion(0L);
        return stopsHub;
    }
}
