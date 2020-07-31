package cn.cnic.base.util;

import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.entity.SysRole;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    public static boolean isAdmin() {
        boolean isAdmin = false;
        UserVo currentUser = getCurrentUser();
        List<SysRole> roles = currentUser.getRoles();
        for (SysRole sysRole : roles) {
            if (null != sysRole) {
                SysRoleType role = sysRole.getRole();
                if (SysRoleType.ADMIN == role) {
                    isAdmin = true;
                    break;
                }
            }
        }
        return isAdmin;
    }

    public static String getCurrentUsername() {
        UserVo currentUser = getCurrentUser();
        if (null == currentUser) {
            return null;
        }
        return currentUser.getUsername();
    }

    public static String getUsername(HttpServletRequest request) {
        return getCurrentUsername();
    }

    //"X-Forwarded-For" is empty, take "X-Real-IP", "X-Real-IP" is empty, take "remoteAddress"
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        logger.info("X-Forwarded-For:" + ip);
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //After multiple reverse proxy, there will be multiple ip values, the first ip is true ip
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
        logger.info("If you do not find the ip of \"X-Real-IP\", you will get \"RemoteAddr\":" + request.getRemoteAddr());
        return request.getRemoteAddr();
    }

}
