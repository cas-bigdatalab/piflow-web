package cn.cnic.component.stopsComponent.utils;

import java.util.Date;

import cn.cnic.component.stopsComponent.model.StopsComponentManage;

public class StopsComponentManageUtils {

    public static StopsComponentManage stopsComponentManageNewNoId(String username) {

        StopsComponentManage stopsComponentManage = new StopsComponentManage();
        // basic properties (required when creating)
        stopsComponentManage.setCrtDttm(new Date());
        stopsComponentManage.setCrtUser(username);
        // basic properties
        stopsComponentManage.setEnableFlag(true);
        stopsComponentManage.setLastUpdateUser(username);
        stopsComponentManage.setLastUpdateDttm(new Date());
        stopsComponentManage.setVersion(0L);
        return stopsComponentManage;
    }

}
