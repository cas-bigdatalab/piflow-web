package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class LoginCtrl {

    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @RequestMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        User user = SessionUserUtil.getCurrentUser();
        if (null != user) {
            logger.info("已经登陆过了，跳转主页");
            modelAndView.setViewName("index");
        }else{
            modelAndView.setViewName("login");
        }
        modelAndView.addObject("now", "say hello spring boot !!!!!");
        return modelAndView;
    }

    @RequestMapping("/")
    public ModelAndView indexHome(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        User user = SessionUserUtil.getCurrentUser();
        if (null != user) {
            logger.info("用户" + user.getUsername() + "登陆成功跳转主页");
        }
        return modelAndView;
    }
}
