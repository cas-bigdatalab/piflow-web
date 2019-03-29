package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.sysUser.model.SysUser;
import com.nature.component.sysUser.service.ISysUserService;
import com.nature.component.sysUser.vo.SysUserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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

    @Resource
    ISysUserService sysUserServiceImpl;

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @RequestMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        UserVo user = SessionUserUtil.getCurrentUser();
        if (null != user) {
            logger.info("已经登陆过了，跳转主页");
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
            logger.info("用户" + user.getUsername() + "登陆成功跳转主页");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/register")
    @ResponseBody
    public String register(HttpServletRequest request) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
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
                rtnMap.put("code", "1");
                rtnMap.put("errMsg", "Congratulations, registration is successful");
            } else {
                rtnMap.put("errMsg", "Registration failed, save failed");
                logger.info("注册失败，保存失败");
            }
        } else {
            rtnMap.put("errMsg", "Registration failed, username or password is empty");
            logger.info("注册失败，用户名或密码为空");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping(value = "/checkUserName")
    @ResponseBody
    public String checkUserName(HttpServletRequest request) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        String username = request.getParameter("userName");
        if (StringUtils.isNotBlank(username)) {
            SysUser addUser = sysUserServiceImpl.findByUsername(username);
            if (null != addUser) {
                rtnMap.put("errMsg", "Username is already taken");
                logger.info("用户名被占用");
            } else {
                rtnMap.put("code", "1");
                rtnMap.put("errMsg", "Username is available");
            }
        } else {
            rtnMap.put("errMsg", "Username can not be empty");
            logger.info("用户名不能为空");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
