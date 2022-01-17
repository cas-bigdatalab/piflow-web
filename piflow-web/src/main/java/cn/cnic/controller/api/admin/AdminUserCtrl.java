package cn.cnic.controller.api.admin;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.ILogHelperService;
import cn.cnic.component.system.vo.SysUserVo;
import cn.cnic.component.user.service.AdminUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class AdminUserCtrl {

    private final AdminUserService adminUserServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public AdminUserCtrl(AdminUserService adminUserServiceImpl, ILogHelperService logHelperServiceImpl) {
        this.adminUserServiceImpl = adminUserServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    @RequestMapping("/getUserListPage")
    @ResponseBody
    public String getUserListPage(Integer page, Integer limit, String param) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return adminUserServiceImpl.getUserListPage(username, isAdmin, page, limit, param);
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public String updateUserInfo(SysUserVo sysUserVo) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("update user",username);
        return  adminUserServiceImpl.update(isAdmin,username,sysUserVo);
    }

    @RequestMapping("/getUserById")
    @ResponseBody
    public String getScheduleById(String userId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return adminUserServiceImpl.getUserById( isAdmin,username, userId);
    }

    @RequestMapping("/delUser")
    @ResponseBody
    public String delUser(String sysUserId) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("del user" + sysUserId,username);
        return  adminUserServiceImpl.delUser(isAdmin,username,sysUserId);
    }




}
