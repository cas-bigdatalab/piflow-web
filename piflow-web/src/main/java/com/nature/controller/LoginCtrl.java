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

import javax.annotation.Resource;
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

    @RequestMapping("/")
    public ModelAndView indexHome(ModelAndView modelAndView) {
        modelAndView.setViewName("jump");
        UserVo user = SessionUserUtil.getCurrentUser();
        if (null != user) {
            logger.info("user " + user.getUsername() + " login successfully jump home page");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/register")
    @ResponseBody
    public String register(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String username = request.getParameter("username");
        String pw = request.getParameter("pw");
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        if (!StringUtils.isAnyEmpty(username, pw)) {
            SysUserVo sysUserVo = new SysUserVo();
            sysUserVo.setUsername(username);
            sysUserVo.setPassword(pw);
            sysUserVo.setSex(sex);
            sysUserVo.setName(name);
            int addUser = sysUserServiceImpl.registerUser(sysUserVo);
            if (addUser > 0) {
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "Congratulations, registration is successful");
            } else {
                rtnMap.put("errorMsg", "Registration failed, save failed");
                logger.info("Registration failed, save failed");
            }
        } else {
            rtnMap.put("errorMsg", "Registration failed, username or password is empty");
            logger.info("Registration failed, username or password is empty");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping(value = "/checkUserName")
    @ResponseBody
    public String checkUserName(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String username = request.getParameter("userName");
        if (StringUtils.isNotBlank(username)) {
            SysUser addUser = sysUserServiceImpl.findByUsername(username);
            if (null != addUser) {
                rtnMap.put("errorMsg", "Username is already taken");
                logger.info("Username is already taken");
            } else {
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "Username is available");
            }
        } else {
            rtnMap.put("errorMsg", "Username can not be empty");
            logger.info("Username can not be empty");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
