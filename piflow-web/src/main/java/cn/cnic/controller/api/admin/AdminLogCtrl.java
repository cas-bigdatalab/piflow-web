package cn.cnic.controller;


import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.AdminLogService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/log")
public class AdminLogCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final AdminLogService adminLogServiceImpl;

    @Autowired
    public AdminLogCtrl(AdminLogService adminLogServiceImpl) {
        this.adminLogServiceImpl = adminLogServiceImpl;
    }

    @RequestMapping("/getLogListPage")
    @ResponseBody
    public String getLogListPage(Integer page, Integer limit, String param) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return adminLogServiceImpl.getLogListPage(username, isAdmin, page, limit, param);
    }

}
