package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/exampleMenu")
public class ExampleMenuCtrl {
    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @RequestMapping("/instructionalVideo")
    public ModelAndView instructionalVideo(){
       return new ModelAndView();
    };
}
