package cn.cnic.controller.api.admin;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.ILogHelperService;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.SysUserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "system user manage api", tags = "system user manage api")
@Controller
@RequestMapping("/user")
public class AdminUserCtrl {

    private final ISysUserService sysUserServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public AdminUserCtrl(ISysUserService sysUserServiceImpl, ILogHelperService logHelperServiceImpl) {
        this.sysUserServiceImpl = sysUserServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    @RequestMapping(value = "/getUserListPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getUserListPage", notes="user list")
    public String getUserListPage(Integer page, Integer limit, String param) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return sysUserServiceImpl.getUserListPage(username, isAdmin, page, limit, param);
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="updateUserInfo", notes="update user info")
    public String updateUserInfo(SysUserVo sysUserVo) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("update user",username);
        return  sysUserServiceImpl.update(isAdmin,username,sysUserVo);
    }

    @RequestMapping(value = "/getUserById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getUserById", notes="get user info by id")
    public String getScheduleById(String userId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return sysUserServiceImpl.getUserById( isAdmin,username, userId);
    }

    @RequestMapping(value = "/delUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="delUser", notes="delete user info by id")
    public String delUser(String sysUserId) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("del user" + sysUserId,username);
        return  sysUserServiceImpl.delUser(isAdmin,username,sysUserId);
    }

    @RequestMapping(value = "/bindDeveloperAccessKey", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="bindDeveloperAccessKey", notes="bind Developer Access Key")
    public String bindDeveloperAccessKey(String accessKey) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("Bind Developer " + accessKey, username);
        return  sysUserServiceImpl.bindDeveloperAccessKey(isAdmin, username, accessKey);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(@RequestBody SysUserVo sysUserVo) {
        return sysUserServiceImpl.addUser(sysUserVo);
    }
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    @ResponseBody
    public String updateRole(@RequestBody SysUserVo sysUserVo) {
        return  sysUserServiceImpl.updateRole(sysUserVo);
    }

    @RequestMapping(value = "/getAllRole", method = RequestMethod.GET)
    @ResponseBody
    public String getAllRole() {
        return  sysUserServiceImpl.getAllRole();
    }

}
