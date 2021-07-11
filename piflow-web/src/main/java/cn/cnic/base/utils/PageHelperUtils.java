package cn.cnic.base.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PageHelperUtils {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> setDataTableParam(Page page, Map<String, Object> rtnMap) {
        if (null == rtnMap) {
            rtnMap = new HashMap<>();
        }
        if (null != page) {
            PageInfo info = new PageInfo(page.getResult());
            rtnMap.put("iTotalDisplayRecords", info.getTotal());
            rtnMap.put("iTotalRecords", info.getTotal());
            rtnMap.put("pageData", info.getList());//Data collection
            log.debug("success");
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
        rtnMap.put("data", info.getList());//Data collection
        log.debug("success");
        return rtnMap;
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
        rtnMap.put(key2, info.getList());//Data collection
        log.debug("success");
        return rtnMap;
    }
}
