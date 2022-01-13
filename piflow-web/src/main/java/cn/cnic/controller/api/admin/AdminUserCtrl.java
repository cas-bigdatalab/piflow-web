package cn.cnic.controller;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.vo.SysUserVo;
import cn.cnic.component.user.service.AdminUserService;
import cn.cnic.component.user.service.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class AdminUserCtrl {

    @Autowired
    AdminUserService adminUserServiceImpl;

    @Autowired
    LogHelper logHelper;

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
        logHelper.logAuthSucceed("update user",username);
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
        logHelper.logAuthSucceed("del user" + sysUserId,username);
        return  adminUserServiceImpl.delUser(isAdmin,username,sysUserId);
    }




}
