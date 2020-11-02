package cn.cnic.base.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PageHelperUtils {

    static Logger logger = LoggerUtil.getLogger();


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
        rtnMap.put("data", info.getList());//Data collection
        logger.debug("success");
        return rtnMap;
    }
}
