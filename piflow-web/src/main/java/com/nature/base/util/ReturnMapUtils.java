package com.nature.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ReturnMapUtils {

    private static Logger logger = LoggerUtil.getLogger();

    public static Integer SUCCEEDED_CODE = 200;
    public static Integer ERROR_CODE = 500;
    public static String ERROR_MSG = "Failed";
    public static String SUCCEEDED_MSG = "Failed";

    /**
     * set Failure information
     *
     * @param errorMsg Custom failure message
     * @return Map
     */
    public static Map<String, Object> setFailedMsg(String errorMsg) {
        errorMsg = (StringUtils.isNotBlank(errorMsg) ? errorMsg : ERROR_MSG);
        logger.warn(errorMsg);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", ERROR_CODE);
        rtnMap.put("errorMsg", errorMsg);
        return rtnMap;
    }

    /**
     * set Failure information
     *
     * @param errorMsg Custom failure message
     * @return JsonStr
     */
    public static String setFailedMsgRtnJsonStr(String errorMsg) {
        Map<String, Object> stringObjectMap = setFailedMsg(errorMsg);
        return JsonUtils.toJsonNoException(stringObjectMap);
    }

    /**
     * set Success message
     *
     * @param succeededMsg Custom success message
     * @return Map
     */
    public static Map<String, Object> setSucceededMsg(String succeededMsg) {
        succeededMsg = (StringUtils.isNotBlank(succeededMsg) ? succeededMsg : SUCCEEDED_MSG);
        logger.info(succeededMsg);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", SUCCEEDED_CODE);
        rtnMap.put("errorMsg", succeededMsg);
        return rtnMap;
    }

    /**
     * set Success information
     *
     * @param succeededMsg Custom success message
     * @return JsonStr
     */
    public static String setSucceededMsgRtnJsonStr(String succeededMsg) {
        Map<String, Object> stringObjectMap = setSucceededMsg(succeededMsg);
        return JsonUtils.toJsonNoException(stringObjectMap);
    }

    /**
     * set code and message
     *
     * @param code status code
     * @param msg Custom message
     * @return Map
     */
    public static Map<String, Object> setCodeAndMsg(Integer code, String msg) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", code);
        rtnMap.put("errorMsg", msg);
        return rtnMap;
    }

    /**
     * set code and message
     *
     * @param code status code
     * @param msg Custom message
     * @return JsonStr
     */
    public static String setCodeAndMsgRtnJsonStr(Integer code, String msg) {
        Map<String, Object> stringObjectMap = setCodeAndMsg(code, msg);
        return JsonUtils.toJsonNoException(stringObjectMap);
    }
}
