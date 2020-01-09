package com.nature.base.util;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ReturnMapUtils {

    private static Logger logger = LoggerUtil.getLogger();

    /**
     * set Failure information
     *
     * @param errorMsg
     * @return
     */
    public static Map<String, Object> setFailedMsg(String errorMsg) {
        logger.warn(errorMsg);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        rtnMap.put("errorMsg", errorMsg);
        return rtnMap;
    }

    /**
     * set Success message
     *
     * @param succeededMsg
     * @return
     */
    public static Map<String, Object> setSucceededMsg(String succeededMsg) {
        logger.info(succeededMsg);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        rtnMap.put("errorMsg", succeededMsg);
        return rtnMap;
    }

    /**
     * set Success message
     *
     * @param succeededMsg
     * @return
     */
    public static Map<String, Object> setCodeAndMsg(Integer code, String succeededMsg) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", code);
        rtnMap.put("errorMsg", succeededMsg);
        return rtnMap;
    }
}
