package cn.cnic.base.config;

import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.system.entity.SysInitRecords;
import cn.cnic.component.system.jpa.domain.SysInitRecordsDomain;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${sysParam.datasource.type}")
    private String profilesType;

    @Resource
    private SysInitRecordsDomain sysInitRecordsDomain;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath = (null == SysParamsCache.SYS_CONTEXT_PATH ? "" : SysParamsCache.SYS_CONTEXT_PATH);
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(contextPath + "/error")) {
            return true;
        }
        if (requestURI.startsWith(contextPath + "/login")) {
            return true;
        }
        if (requestURI.startsWith(contextPath + "/jwtLogin")) {
            return true;
        }
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
                    response.sendRedirect(contextPath + "/page/bootPage/index"); // Redirect to the boot page
                    return false;
                }
                log.info("No initialization, enter the boot page");
            }
        } else if (requestURI.startsWith(contextPath + "/bootPage")) {
            response.sendRedirect(contextPath); // Redirect to the boot page
            return false;
        }
        return true;
    }
}
