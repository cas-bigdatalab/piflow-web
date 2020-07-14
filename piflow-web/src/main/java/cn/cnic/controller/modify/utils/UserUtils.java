package cn.cnic.controller.modify.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import cn.cnic.base.util.LoggerUtil;

public class UserUtils {

    static Logger logger = LoggerUtil.getLogger();

    public static String getUsername(HttpServletRequest request) {
        return "admin";
    }

}
