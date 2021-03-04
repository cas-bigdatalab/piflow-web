package cn.cnic.base;

import java.util.Date;

public class BaseHibernateModelNoIdUtils {

    public static BaseHibernateModelNoId newBaseHibernateModelNoId(String username) {

        BaseHibernateModelNoId baseHibernateModelNoId = new BaseHibernateModelNoId();
        // basic properties (required when creating)
        baseHibernateModelNoId.setCrtDttm(new Date());
        baseHibernateModelNoId.setCrtUser(username);
        // basic properties
        baseHibernateModelNoId.setEnableFlag(true);
        baseHibernateModelNoId.setLastUpdateUser(username);
        baseHibernateModelNoId.setLastUpdateDttm(new Date());
        baseHibernateModelNoId.setVersion(0L);
        return baseHibernateModelNoId;
    }
}
