package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import org.slf4j.Logger;
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

    @RequestMapping("/flowList")
    public ModelAndView flowList() {
        return setBaseModeAndView(new ModelAndView(), "flowList");
    }

    @RequestMapping("/flowGroupList")
    public ModelAndView flowGroupList() {
        return setBaseModeAndView(new ModelAndView(), "flowGroupList");
    }

    @RequestMapping("/processList")
    public ModelAndView processList() {
        return setBaseModeAndView(new ModelAndView(), "processList");
    }

    @RequestMapping("/processGroupList")
    public ModelAndView processGroupList() {
        return setBaseModeAndView(new ModelAndView(), "processGroupList");
    }

    @RequestMapping("/groupTypeProcessList")
    public ModelAndView groupTypeProcessList() {
        return setBaseModeAndView(new ModelAndView(), "groupTypeProcessList");
    }

    @RequestMapping("/processAndProcessGroup")
    public ModelAndView processAndProcessGroup() {
        return setBaseModeAndView(new ModelAndView(), "processAndProcessGroup");
    }

    @RequestMapping("/template")
    public ModelAndView template() {
        return setBaseModeAndView(new ModelAndView(), "template");
    }

    @RequestMapping("/flowGroupTemplateList")
    public ModelAndView flowGroupTemplateList() {
        return setBaseModeAndView(new ModelAndView(), "flowGroupTemplateList");
    }

    @RequestMapping("/flowTemplateList")
    public ModelAndView flowTemplateList() {
        return setBaseModeAndView(new ModelAndView(), "flowTemplateList");
    }

    @RequestMapping("/dataSources")
    public ModelAndView dataSources() {
        return setBaseModeAndView(new ModelAndView(), "dataSources");
    }

    @RequestMapping("/instructionalVideo")
    public ModelAndView instructionalVideo() {
        return setBaseModeAndView(new ModelAndView(), "instructionalVideo");
    }

    @RequestMapping("/sysScheduleList")
    public ModelAndView sysScheduleList() {
        return setBaseModeAndView(new ModelAndView(), "sysScheduleList");
    }

    private ModelAndView setBaseModeAndView(ModelAndView modelAndView, String rightPage) {
        if (null == modelAndView) {
            modelAndView = new ModelAndView();
        }
        modelAndView.setViewName("indexNew");
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("accessPath", rightPage);
        return modelAndView;
    }

}
