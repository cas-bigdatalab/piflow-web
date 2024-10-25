package cn.cnic.base.utils;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class ReturnMapUtils {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private static Logger logger = LoggerUtil.getLogger();

    public static String KEY_CODE = "code";
    public static String KEY_ERROR_MSG = "errorMsg";
    public static Integer SUCCEEDED_CODE = 200;
    public static Integer ERROR_CODE = 500;

    /**
     * set Failure information
     *
     * @param errorMsg Custom failure message
     * @return Map
     */
    public static Map<String, Object> setFailedMsg(String errorMsg) {
        errorMsg = (StringUtils.isNotBlank(errorMsg) ? errorMsg : MessageConfig.ERROR_MSG());
        logger.warn(errorMsg);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put(KEY_CODE, ERROR_CODE);
        rtnMap.put(KEY_ERROR_MSG, errorMsg);
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

    public static String setSucceededCustomMap(Map<String, Object> map) {
        map.put(KEY_CODE, SUCCEEDED_CODE);
        map.put(KEY_ERROR_MSG, MessageConfig.SUCCEEDED_MSG());
        return JsonUtils.toJsonNoException(map);
    }

    /**
     * set Success message
     *
     * @param succeededMsg Custom success message
     * @return Map
     */
    public static Map<String, Object> setSucceededMsg(String succeededMsg) {
        succeededMsg = (StringUtils.isNotBlank(succeededMsg) ? succeededMsg : MessageConfig.SUCCEEDED_MSG());
        logger.info(succeededMsg);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put(KEY_CODE, SUCCEEDED_CODE);
        rtnMap.put(KEY_ERROR_MSG, succeededMsg);
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
     * set Success message
     *
     * @param key   Custom param
     * @param value Custom param
     * @return Map
     */
    public static Map<String, Object> setSucceededCustomParam(String key, Object value) {
        Map<String, Object> rtnMap = setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        rtnMap.put(key, value);
        return rtnMap;
    }

    /**
     * set Success message
     *
     * @param key   Custom param
     * @param value Custom param
     * @return JsonStr
     */
    public static String setSucceededCustomParamRtnJsonStr(String key, Object value) {
        return JsonUtils.toJsonNoException(setSucceededCustomParam(key, value));
    }

    /**
     * set code and message
     *
     * @param code status code
     * @param msg  Custom message
     * @return Map
     */
    public static Map<String, Object> setCodeAndMsg(Integer code, String msg) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put(KEY_CODE, code);
        rtnMap.put(KEY_ERROR_MSG, msg);
        return rtnMap;
    }

    /**
     * set code and message
     *
     * @param code status code
     * @param msg  Custom message
     * @return JsonStr
     */
    public static String setCodeAndMsgRtnJsonStr(Integer code, String msg) {
        Map<String, Object> stringObjectMap = setCodeAndMsg(code, msg);
        return JsonUtils.toJsonNoException(stringObjectMap);
    }
    
    /**
     * append Values return map
     *
     * @param rtnMap
     * @param key    Custom param
     * @param value  Custom param
     * @return Map
     */
    public static Map<String, Object> appendValues(Map<String, Object> rtnMap, String key, Object value) {
        if(null == rtnMap) {
            rtnMap = new HashMap<>();
        }
        rtnMap.put(key, value);
        return rtnMap;
    }

    /**
     * append Succeeded return map
     *
     * @param rtnMap
     * @return Map
     */
    public static Map<String, Object> appendSucceeded(Map<String, Object> rtnMap) {
        if(null == rtnMap) {
            rtnMap = new HashMap<>();
        }
        return setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
    }
    
    /**
     * append Values return jsonStr
     *
     * @param rtnMap
     * @param key    Custom param
     * @param value  Custom param
     * @return JsonStr
     */
    public static String appendValuesToJson(Map<String, Object> rtnMap, String key, Object value) {
        return JsonUtils.toJsonNoException(appendValues(rtnMap, key, value));
    }

    /**
     * append Succeeded return jsonStr
     *
     * @param rtnMap
     * @return JsonStr
     */
    public static String appendSucceededToJson(Map<String, Object> rtnMap) {
        return JsonUtils.toJsonNoException(appendSucceeded(rtnMap));
    }

    /**
     * return jsonStr
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> String toJson(T entity) {
        return JsonUtils.toJsonNoException(entity);
    }

    /**
     * return jsonStr
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> String toFormatJson(T entity) {
        return JsonUtils.toFormatJsonNoException(entity);
    }
}
