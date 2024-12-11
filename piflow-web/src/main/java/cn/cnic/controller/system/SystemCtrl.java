package cn.cnic.controller.system;

import cn.cnic.component.system.service.ISystemInfoService;
import cn.cnic.component.system.vo.SystemInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Api(value = "sys api", tags = "sys api")
@Controller
@RequestMapping("/sys")
public class SystemCtrl {

    private final ISystemInfoService systemInfoServiceImpl;

    @Autowired
    public SystemCtrl(ISystemInfoService systemInfoServiceImpl) {
        this.systemInfoServiceImpl = systemInfoServiceImpl;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "update", notes = "更新系统简介")
    public String update(SystemInfoVo systemInfoVo) {
        return systemInfoServiceImpl.update(systemInfoVo);
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "select", notes = "获取系统简介")
    public String select() {
        return systemInfoServiceImpl.select();
    }

}
