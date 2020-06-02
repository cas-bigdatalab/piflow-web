package cn.cnic.base.util;

import org.springframework.context.ApplicationContext;

public class SpringContextUtil {

    private static ApplicationContext applicationContext;

    //Get context
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //Set context
    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    //Get the "bean" in the context by name
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    //Get the "bean" in the context by type
    public static Object getBean(Class<?> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}
