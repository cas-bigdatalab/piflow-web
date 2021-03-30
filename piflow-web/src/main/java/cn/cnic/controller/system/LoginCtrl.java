package cn.cnic.controller.system;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.SysUserVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class LoginCtrl {

    @Autowired
    private ISysUserService sysUserServiceImpl;

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @RequestMapping("/login")
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

    @PostMapping(value = "/jwtLogin")
    @ResponseBody
    public String login(String username,String password){
        return sysUserServiceImpl.jwtLogin(username, password);
    }

    @RequestMapping(value = "/register")
    @ResponseBody
    public String register(HttpServletRequest request) {
        String username = request.getParameter("username");
        String pw = request.getParameter("pw");
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUsername(username);
        sysUserVo.setPassword(pw);
        sysUserVo.setSex(sex);
        sysUserVo.setName(name);
        return sysUserServiceImpl.registerUser(sysUserVo);
    }

    @RequestMapping(value = "/checkUserName")
    @ResponseBody
    public String checkUserName(String username) {
        return sysUserServiceImpl.checkUserName(username);
    }
}
