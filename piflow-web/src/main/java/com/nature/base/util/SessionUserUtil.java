package com.nature.base.util;

import com.nature.base.config.vo.UserVo;
import com.nature.component.sysUser.model.SysUser;
import com.nature.component.sysUser.vo.SysUserVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public class SessionUserUtil {

    static Logger logger = LoggerUtil.getLogger();

    public static UserVo getCurrentUser() {
        UserVo user = null;
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            user = (UserVo) auth.getPrincipal();
        }
        return user;
    }

    //X-Forwarded-For为空，取X-Real-IP，X-Real-IP为空，取remoteAddress
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        logger.info("X-Forwarded-For:" + ip);
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            logger.info("X-Forwarded-For first , index:" + index);
            if (index != -1) {
                String subIp = ip.substring(0, index);
                logger.info("X-Forwarded-For find ip,sub ip :" + subIp);
                return subIp;
            } else {
                logger.info("origin ip:" + ip);
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        logger.info("X-Real-IP:" + ip);
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        logger.info("没有发现X-Real-IP的ip，将获取RemoteAddr：" + request.getRemoteAddr());
        return request.getRemoteAddr();
    }

}
