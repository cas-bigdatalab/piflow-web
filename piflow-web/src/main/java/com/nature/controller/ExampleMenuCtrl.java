package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.Flow;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exampleMenu")
public class ExampleMenuCtrl {
  /** 引入日志，注意都是"org.slf4j"包下 */
  Logger logger = LoggerUtil.getLogger();

  @RequestMapping("/instructionalVideo")
  public ModelAndView instructionalVideo() {
    return new ModelAndView();
  }

  @RequestMapping("/exampleDetails")
  public ModelAndView exampleDetails() {
    return new ModelAndView("example/exampleDetails");
  }

    @RequestMapping("/exampleUrlList")
    @ResponseBody
    public String exampleUrlList() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 0);
        List<Flow> flowExampleList = new ArrayList<>();
        Flow flow1 = new Flow();
        Flow flow2 = new Flow();
        flow1.setId("none1");
        flow1.setName("Example1");
        flow2.setId("none2");
        flow2.setName("Example2");
        flowExampleList.add(flow1);
        flowExampleList.add(flow2);
        rtnMap.put("code", 1);
        rtnMap.put("flowExampleList", flowExampleList);
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
