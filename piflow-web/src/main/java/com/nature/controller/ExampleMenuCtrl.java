package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.service.IFlowService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/exampleMenu")
public class ExampleMenuCtrl {
    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IFlowService flowServiceImpl;

    @RequestMapping("/instructionalVideo")
    public ModelAndView instructionalVideo() {
        return new ModelAndView();
    }

    @RequestMapping("/exampleUrlList")
    @ResponseBody
    public String exampleUrlList() {
        return flowServiceImpl.getFlowExampleList();
    }

    @RequestMapping("/judgeExample")
    @ResponseBody
    public String judgeExample() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 200);
        rtnMap.put("flag", true);
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
