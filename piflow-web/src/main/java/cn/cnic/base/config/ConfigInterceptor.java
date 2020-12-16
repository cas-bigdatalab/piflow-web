package cn.cnic.base.config;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.system.jpa.domain.SysInitRecordsDomain;

/**
 * Defining interceptors
 */
@Component
public class ConfigInterceptor implements HandlerInterceptor {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysInitRecordsDomain sysInitRecordsDomain;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath = (null == SysParamsCache.SYS_CONTEXT_PATH ? "" : SysParamsCache.SYS_CONTEXT_PATH);
        String requestURI = request.getRequestURI();

        if (requestURI.equals(contextPath + "/")) {
            response.sendRedirect(contextPath + "/page/index.html"); // Redirect to the boot page
            return false;
        }
        if (requestURI.startsWith(contextPath + "/error")) {
            //response.sendRedirect(contextPath + "/page/error/errorPage.html"); // Redirect to the boot page
            return false;
        }
        if (requestURI.startsWith(contextPath + "/login")) {
            UserVo user = SessionUserUtil.getCurrentUser();
            if (null != user) {
                logger.info("Already logged in, jump homepage");
                response.sendRedirect(contextPath + "/page/index.html"); // Redirect to the boot page
            } else {
                response.sendRedirect(contextPath + "/page/login.html"); // Redirect to the boot page
            }
            return false;
        }
        if (requestURI.startsWith(contextPath + "/page/login")) {
            UserVo user = SessionUserUtil.getCurrentUser();
            if (null != user) {
                logger.info("Already logged in, jump homepage");
                response.sendRedirect(contextPath + "/page/index.html"); // Redirect to the boot page
                return false;
            }
            return true;
        }
        return true;
    }
}
