package cn.cnic.base.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Defining interceptors
 */
@Component
public class ConfigInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String contextPath = (null == SysParamsCache.SYS_CONTEXT_PATH ? "" : SysParamsCache.SYS_CONTEXT_PATH);
        //String requestURI = request.getRequestURI();
        return true;
    }
}
