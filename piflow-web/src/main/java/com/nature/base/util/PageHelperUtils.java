package com.nature.base.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;

import java.util.Map;

public class PageHelperUtils {

  static Logger logger = LoggerUtil.getLogger();

  @SuppressWarnings("unchecked")
  public static Map setDataTableParam(Page page, Map<String, Object> rtnMap) {
    if (null !=page && null != rtnMap) {
      PageInfo info = new PageInfo(page.getResult());
      rtnMap.put("iTotalDisplayRecords", info.getTotal());
      rtnMap.put("iTotalRecords", info.getTotal());
      rtnMap.put("pageData", info.getList());//Data collection
      logger.debug("success");
    }
    return rtnMap;
  }
}
