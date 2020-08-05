package cn.cnic.base.config;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.system.entity.SysInitRecords;
import cn.cnic.component.system.jpa.domain.SysInitRecordsDomain;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Defining interceptors
 */
@Component
@Log4j
public class ConfigInterceptor implements HandlerInterceptor {

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
            response.sendRedirect(contextPath + "/page/error/errorPage.html"); // Redirect to the boot page
            return false;
        }
        if (requestURI.startsWith(contextPath + "/login")) {
            UserVo user = SessionUserUtil.getCurrentUser();
            if (null != user) {
                log.info("Already logged in, jump homepage");
                response.sendRedirect(contextPath + "/page/index.html"); // Redirect to the boot page
            } else {
                response.sendRedirect(contextPath + "/page/login.html"); // Redirect to the boot page
            }
            return false;
        }
        if (requestURI.startsWith(contextPath + "/page/login")) {
            UserVo user = SessionUserUtil.getCurrentUser();
            if (null != user) {
                log.info("Already logged in, jump homepage");
                response.sendRedirect(contextPath + "/page/index.html"); // Redirect to the boot page
                return false;
            }
            return true;
        }
        /*
        if (requestURI.startsWith(contextPath + "/jwtLogin")) {
            return true;
        }
        */
        // Determine if the boot flag is true
        if (!SysParamsCache.IS_BOOT_COMPLETE) {
            // Query is boot record
            SysInitRecords sysInitRecordsLastNew = sysInitRecordsDomain.getSysInitRecordsLastNew(1);
            if (null != sysInitRecordsLastNew && sysInitRecordsLastNew.getIsSucceed()) {
                SysParamsCache.setIsBootComplete(true);
                if (requestURI.startsWith(contextPath + "/bootPage")) {
                    response.sendRedirect(contextPath); // Redirect to the boot page
                    return false;
                }
            } else {
                if (!requestURI.startsWith(contextPath + "/bootPage")) {
                    response.sendRedirect(contextPath + "/page/bootPage/bootPage.html"); // Redirect to the boot page
                    return false;
                }
                log.info("No initialization, enter the boot page");
            }
        } else if (requestURI.startsWith(contextPath + "/bootPage")) {
            response.sendRedirect(contextPath + "/page/index.html"); // Redirect to the boot page
            return false;
        }
        return true;
    }
}
