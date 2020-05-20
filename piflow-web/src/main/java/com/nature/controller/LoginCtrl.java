package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.system.model.SysUser;
import com.nature.component.system.service.ISysUserService;
import com.nature.component.system.vo.SysUserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class LoginCtrl {

    @Autowired
    private ISysUserService sysUserServiceImpl;

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @RequestMapping("/")
    public ModelAndView indexHome(ModelAndView modelAndView) {
        modelAndView.setViewName("jump");
        UserVo user = SessionUserUtil.getCurrentUser();
        if (null != user) {
            logger.info("user " + user.getUsername() + " login successfully jump home page");
        }
        modelAndView.setViewName("indexNew");
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("accessPath", "indexHome");
        return modelAndView;
    }

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
    public String checkUserName(HttpServletRequest request) {
        String username = request.getParameter("userName");
        return sysUserServiceImpl.checkUserName(username);
    }
}
