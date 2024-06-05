package cn.cnic.controller.api.process;

import cn.cnic.component.process.service.IErrorLogMappingService;
import cn.cnic.component.process.vo.ErrorLogMappingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "error log mapping api", tags = "error log mapping api")
@Controller
@RequestMapping("/errorLogMapping")
public class ErrorLogMappingCtrl {

    private final IErrorLogMappingService errorLogMappingServiceImpl;


    @Autowired
    public ErrorLogMappingCtrl(IErrorLogMappingService errorLogMappingServiceImpl) {
        this.errorLogMappingServiceImpl = errorLogMappingServiceImpl;
    }


    /**
     * @param errorLogMappingVo:
     * @return String
     * @author tianyao
     * @description 分页查询错误日志映射列表
     * @date 2024/6/5 17:04
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getByPage", notes = "分页查询错误日志映射列表")
    public String getByPage(@RequestBody ErrorLogMappingVo errorLogMappingVo) {
        return errorLogMappingServiceImpl.getByPage(errorLogMappingVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 获取某一个用户错误日志映射详情
     * @date 2024/6/5 17:46
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getById", notes = "获取某一个用户错误日志映射详情")
    public String getById(String id) {
        return errorLogMappingServiceImpl.getById(id);
    }

    /**
     * @param errorLogMappingVo:
     * @return String
     * @author tianyao
     * @description 新增或编辑用户错误日志映射
     * @date 2024/6/5 17:49
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "save", notes = "新增或编辑用户错误日志映射")
    public String save(@RequestBody ErrorLogMappingVo errorLogMappingVo) {
        return errorLogMappingServiceImpl.save(errorLogMappingVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 删除某一个用户错误日志映射
     * @date 2024/6/5 17:49
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "delete", notes = "删除某一个用户错误日志映射")
    public String delete(String id) {
        return errorLogMappingServiceImpl.delete(id);
    }


}
