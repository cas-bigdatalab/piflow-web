package com.nature.controller;

import com.nature.base.util.SessionUserUtil;
import com.nature.component.system.service.ISysInitRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bootPage")
public class BootPageCtrl {

    @Autowired
    private ISysInitRecordsService sysInitRecordsServiceImpl;

    @RequestMapping("/index")
    public String index() {
        return "bootPage";
    }

    @RequestMapping("/initComponents")
    @ResponseBody
    public String initComponents() {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        return sysInitRecordsServiceImpl.initComponents(currentUsername);
    }

    @RequestMapping("/threadMonitoring")
    @ResponseBody
    public String threadMonitoring() {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        return sysInitRecordsServiceImpl.threadMonitoring(currentUsername);
    }


}
