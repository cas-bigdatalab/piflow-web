package cn.cnic.base.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PageHelperUtils {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private static Logger logger = LoggerUtil.getLogger();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> setDataTableParam(Page page, Map<String, Object> rtnMap) {
        if (null == rtnMap) {
            rtnMap = new HashMap<>();
        }
        if (null != page) {
            PageInfo info = new PageInfo(page.getResult());
            rtnMap.put("iTotalDisplayRecords", info.getTotal());
            rtnMap.put("iTotalRecords", info.getTotal());
            //Data collection
            rtnMap.put("pageData", info.getList());
            logger.debug("success");
        }
        return rtnMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> setLayTableParam(Page page, Map<String, Object> rtnMap) {
        if (null == rtnMap) {
            rtnMap = new HashMap<>();
        }
        if (null == page) {
            return rtnMap;
        }
        PageInfo info = new PageInfo(page.getResult());
        rtnMap.put("msg", "success");
        rtnMap.put("count", info.getTotal());
        //Data collection
        rtnMap.put("data", info.getList());
        logger.debug("success");
        return rtnMap;
    }

    public static String setLayTableParamRtnStr(Page page, Map<String, Object> rtnMap) {
        return JsonUtils.toJsonNoException(setLayTableParam(page, rtnMap));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> setCustomDataKey(Page page, String key1, String key2, Map<String, Object> rtnMap) {
        if (null == rtnMap) {
            rtnMap = new HashMap<>();
        }
        if (null == page) {
            return rtnMap;
        }
        if (StringUtils.isBlank(key1)) {
            key1 = "count";
        }
        if (StringUtils.isBlank(key2)) {
            key2 = "data";
        }
        PageInfo info = new PageInfo(page.getResult());
        if(null == rtnMap.get(key1)){
            rtnMap.put(key1, info.getTotal());
        }
        //Data collection
        rtnMap.put(key2, info.getList());
        logger.debug("success");
        return rtnMap;
    }
}
