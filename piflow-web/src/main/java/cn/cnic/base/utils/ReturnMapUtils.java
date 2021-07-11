package cn.cnic.base.utils;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReturnMapUtils {

    public static Integer SUCCEEDED_CODE = 200;
    public static Integer ERROR_CODE = 500;
    public static String ERROR_MSG = "Failed";
    public static String SUCCEEDED_MSG = "Succeeded";
    public static String KEY_CODE = "code";
    public static String KEY_ERROR_MSG = "errorMsg";


    /**
     * set Failure information
     *
     * @param errorMsg Custom failure message
     * @return Map
     */
    public static Map<String, Object> setFailedMsg(String errorMsg) {
        errorMsg = (StringUtils.isNotBlank(errorMsg) ? errorMsg : ERROR_MSG);
        log.warn(errorMsg);
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

    /**
     * set Success message
     *
     * @param succeededMsg Custom success message
     * @return Map
     */
    public static Map<String, Object> setSucceededMsg(String succeededMsg) {
        succeededMsg = (StringUtils.isNotBlank(succeededMsg) ? succeededMsg : SUCCEEDED_MSG);
        log.info(succeededMsg);
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
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put(KEY_CODE, SUCCEEDED_CODE);
        rtnMap.put(KEY_ERROR_MSG, SUCCEEDED_MSG);
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
     * return jsonStr
     *
     * @param rtnMap
     * @return JsonStr
     */
    public static String mapToJson(Map<String, Object> rtnMap) {
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
