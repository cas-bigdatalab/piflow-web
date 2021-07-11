package cn.cnic.base.config.jwt;

import cn.cnic.common.Eunm.ResultCode;
import lombok.extern.slf4j.Slf4j;
import cn.cnic.base.config.jwt.common.ResultJson;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Authentication failure processing class, return 401
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //It will enter this method if the verification is not logged in state, the authentication is wrong
        if (!"OPTIONS".equals(request.getMethod()) && !"options".equals(request.getMethod())) {
            log.warn("Authentication failed：" + authException.getMessage());
        }
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = ResultJson.failure(ResultCode.UNAUTHORIZED, authException.getMessage()).toString();
        printWriter.write(body);
        printWriter.flush();
    }
}