package cn.cnic.controller.api.admin;


import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/log")
public class AdminLogCtrl {

    private final AdminLogService adminLogServiceImpl;

    @Autowired
    public AdminLogCtrl(AdminLogService adminLogServiceImpl) {
        this.adminLogServiceImpl = adminLogServiceImpl;
    }

    @RequestMapping(value = "/getLogListPage", method = RequestMethod.GET)
    @ResponseBody
    public String getLogListPage(Integer page, Integer limit, String param) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return adminLogServiceImpl.getLogListPage(username, isAdmin, page, limit, param);
    }

}
