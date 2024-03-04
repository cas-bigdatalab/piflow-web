package cn.cnic.controller.system;

import cn.cnic.component.system.service.IFileService;
import cn.cnic.component.system.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Api(value = "role api", tags = "role api")
@Controller
@RequestMapping("/role")
public class RoleCtrl {

    private final IRoleService roleServiceImpl;

    @Autowired
    public RoleCtrl(IRoleService roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    /**
     * @param :
     * @return String
     * @author tianyao
     * @description 获取用户角色信息
     * @date 2024/2/29 9:38
     */
    @RequestMapping(value = "/getRoleInfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getRoleInfo", notes = "获取用户角色信息")
    public String getRoleInfo() {
        return roleServiceImpl.getRoleInfo();
    }

}
