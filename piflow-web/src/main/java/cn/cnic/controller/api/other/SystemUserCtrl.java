package cn.cnic.controller.api.other;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.ILogHelperService;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(String oldPassword, String password) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return sysUserServiceImpl.updatePassword(username, oldPassword, password);
    }

}
