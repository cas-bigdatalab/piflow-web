package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.service.IFlowInfoDbService;
import com.nature.component.flow.service.IFlowService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web")
public class IndexCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    IFlowInfoDbService appService;

    @Autowired
    IFlowService flowServiceImpl;

    @Value("${syspara.videoName}")
    private String videoName;

    @RequestMapping("/flowList")
    public ModelAndView flowList() {
        return setBaseModeAndView(new ModelAndView(), "flowList");
    }

    @RequestMapping("/processList")
    public ModelAndView processList() {
        return setBaseModeAndView(new ModelAndView(), "getProcessList");
    }

    @RequestMapping("/template")
    public ModelAndView template() {
        return setBaseModeAndView(new ModelAndView(), "template");
    }

    @RequestMapping("/instructionalVideo")
    public ModelAndView instructionalVideo() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("videoName",videoName);
        return setBaseModeAndView(modelAndView, "instructionalVideo");
    }

    @RequestMapping("/errorPage")
    public ModelAndView error(ModelAndView modelAndView) {
        modelAndView.setViewName("errorPage");
        return modelAndView;
    }

    private ModelAndView setBaseModeAndView(ModelAndView modelAndView, String rightage) {
        if (null == modelAndView) {
            modelAndView = new ModelAndView();
        }
        modelAndView.setViewName("indexNew");
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("accessPath", rightage);
        return modelAndView;
    }

}
