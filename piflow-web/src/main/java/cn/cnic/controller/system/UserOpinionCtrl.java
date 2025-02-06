package cn.cnic.controller.system;

import cn.cnic.base.utils.*;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.service.IUserOpinionService;
import cn.cnic.component.system.vo.UserOpiniontVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "user opinion api", tags = "user opinion api")
@Controller
@RequestMapping("/userOpinion")
public class UserOpinionCtrl {

    private static Logger logger = LoggerUtil.getLogger();

    private final IUserOpinionService userOpinionServiceImpl;

    private final SysUserDomain sysUserDomain;


    @Autowired
    public UserOpinionCtrl(IUserOpinionService userOpinionServiceImpl, SysUserDomain sysUserDomain){
        this.userOpinionServiceImpl = userOpinionServiceImpl;
        this.sysUserDomain = sysUserDomain;
    }


    /**
     * 分页查询
     * 中心管理员查询全部的，非中心管理员查询个人的
     * @param userOpiniontVo
     * @return
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getByPage", notes = "分页查询")
    public String getByPage(@RequestBody UserOpiniontVo userOpiniontVo) {
        return userOpinionServiceImpl.getByPage(userOpiniontVo);
    }


    /**
     * 新增或编辑用户反馈，表单输入
     * @param userOpinionVo
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "save", notes = "新增或编辑用户反馈")
    public String save(UserOpiniontVo userOpinionVo) {
        return userOpinionServiceImpl.save(userOpinionVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "delete", notes = "删除")
    public String delete(String id) {
        return userOpinionServiceImpl.delete(id);
    }


    /**
     * 中心管理员功能
     * @param
     * @return
     */
    @RequestMapping(value = "/updateState", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "updateState", notes = "更新用户反馈状态")
    public String updateState(@RequestBody UserOpiniontVo userOpinionVo) {
        return userOpinionServiceImpl.updateState(userOpinionVo);
    }
}
