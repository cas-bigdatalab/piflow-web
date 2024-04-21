package cn.cnic.controller.api.other;

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

@Api(value = "sysUser api", tags = "sysUser api")
@Controller
@RequestMapping("/sysUser")
public class SystemUserCtrl {

    private final ISysUserService sysUserServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public SystemUserCtrl(ISysUserService sysUserServiceImpl, ILogHelperService logHelperServiceImpl) {
        this.sysUserServiceImpl = sysUserServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "updatePassword", notes = "update password")
    public String updatePassword(String oldPassword, String password) {
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("update passwd", username);
        return sysUserServiceImpl.updatePassword(username, oldPassword, password);
    }

    /**
     * @param :
     * @return String
     * @author tianyao
     * @description 获取个人基本信息
     * @date 2024/4/19 16:50
     */
    @RequestMapping(value = "/getByUsername", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getByUsername", notes = "获取个人基本信息")
    public String getByUsername() {
        return sysUserServiceImpl.getByUsername();
    }

    /**
     * @param sysUserVo:
     * @return String
     * @author tianyao
     * @description 更新个人基本信息
     * @date 2024/4/20 17:05
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "update", notes = "更新个人基本信息")
    public String update(@RequestBody SysUserVo sysUserVo) {
        return sysUserServiceImpl.updateInfo(sysUserVo);
    }


}
