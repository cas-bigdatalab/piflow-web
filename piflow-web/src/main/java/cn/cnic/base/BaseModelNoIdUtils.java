package cn.cnic.base;

import java.util.Date;

public class BaseModelNoIdUtils {

    public static BaseModelNoId newBaseModelNoId(String username) {

        BaseModelNoId baseModelNoId = new BaseModelNoId();
        // basic properties (required when creating)
        baseModelNoId.setCrtDttm(new Date());
        baseModelNoId.setCrtUser(username);
        // basic properties
        baseModelNoId.setEnableFlag(true);
        baseModelNoId.setLastUpdateUser(username);
        baseModelNoId.setLastUpdateDttm(new Date());
        baseModelNoId.setVersion(0L);
        return baseModelNoId;
    }
}
