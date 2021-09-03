package cn.cnic.controller.system;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.SysUserVo;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public class LoginCtrl {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ISysUserService sysUserServiceImpl;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(ModelAndView modelAndView) {
        UserVo user = SessionUserUtil.getCurrentUser();
        if (null != user) {
            logger.info("Already logged in, jump homepage");
            modelAndView.setViewName("jump");
        } else {
            modelAndView.setViewName("login");
        }
        modelAndView.addObject("now", "say hello spring boot !!!!!");
        return modelAndView;
    }

    @RequestMapping(value = "/jwtLogin", method = RequestMethod.POST)
    @ResponseBody
    public String login(String username,String password){
        return sysUserServiceImpl.jwtLogin(username, password);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(String username, String pw, String name, String sex) {
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUsername(username);
        sysUserVo.setPassword(pw);
        sysUserVo.setSex(sex);
        sysUserVo.setName(name);
        return sysUserServiceImpl.registerUser(sysUserVo);
    }

    @RequestMapping(value = "/checkUserName", method = RequestMethod.POST)
    @ResponseBody
    public String checkUserName(String username) {
        return sysUserServiceImpl.checkUserName(username);
    }
}
