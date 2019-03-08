package com.nature.base.util;

import com.github.pagehelper.PageInfo;
import com.nature.component.process.vo.ProcessVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageHelperUtils {

  static Logger logger = LoggerUtil.getLogger();

  public static ModelAndView setDataTableParam(ModelAndView modelAndView, List dataList) {
    PageInfo<ProcessVo> pageInfo = new PageInfo<>(dataList);
    modelAndView.addObject("sEcho", 0);
    modelAndView.addObject("iTotalRecords", pageInfo.getTotal());
    modelAndView.addObject("iTotalDisplayRecords", pageInfo.getTotal());
    modelAndView.addObject("pageData", pageInfo.getList());
    //      map.put("sEcho", sEcho);
    //      map.put("iTotalRecords", pageInfo.getTotal());
    //      map.put("iTotalDisplayRecords", pageInfo.getTotal());
    //      map.put("data", pageInfo.getList());
    return modelAndView;
  }

  public static Map setDataTableParam(List dataList, Map<String, Object> rtnMap) {
    if (CollectionUtils.isNotEmpty(dataList) && null != rtnMap) {
      PageInfo<ProcessVo> pageInfo = new PageInfo<>(dataList);
      rtnMap.put("total", pageInfo.getTotal());
      rtnMap.put("dataLength", pageInfo.getPages());
      rtnMap.put("rowDatas", pageInfo.getList());
    }
    return rtnMap;
  }
}
