package cn.cnic.base.config.jwt;

import cn.cnic.base.config.jwt.common.JwtUtils;
import cn.cnic.base.vo.UserVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * token校验,引用的stackoverflow一个答案里的处理方式
 * 添加jwt token 过期自动续期的逻辑  参考至https://blog.csdn.net/qq_40058629/article/details/112845280
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String token_header;

    private final JwtUtils jwtUtils;

    public static HashMap<String, String> TOKEN = new HashMap<>();

    @Autowired
    public JwtAuthenticationTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Credentials", "false");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,X-CAF-Authorization-Token,sessionToken,X-TOKEN,Authorization");
        String auth_token = request.getHeader(this.token_header);

        //如果是没有请求头，直接放行
        if (auth_token == null || auth_token.contains("Bearer undefined") || auth_token.contains("Bearer false")) {
            chain.doFilter(request, response);
            return;
        } else {
            final String auth_token_start = "Bearer ";
            auth_token = auth_token.substring(auth_token_start.length());
        }
        //如果有token,则进行解析，并且设置认证信息
//        final String auth_token_start = "Bearer ";
//        if (StringUtils.isNotEmpty(auth_token) && auth_token.startsWith(auth_token_start)) {
//            auth_token = auth_token.substring(auth_token_start.length());
//        } else {
//            // 不按规范,不允许通过验证
//            auth_token = null;
//        }

        String username = jwtUtils.getUsernameFromToken(auth_token);

        logger.debug(String.format("Checking authentication for user %s.", username));

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean flag = true;
            if (jwtUtils.isTwoTimesTokenExpired(auth_token)) {
                //超过两倍
                if (ObjectUtils.isEmpty(TOKEN.get(username))) {
                    flag = false;
                } else {
                    String token = TOKEN.get(username);
                    if (jwtUtils.isTwoTimesTokenExpired(token)) {
                        TOKEN.put(username, jwtUtils.refreshToken(auth_token));
                    } else {
                        flag = false;
                    }
                }
            } else {
                //未超过两倍,加一个缓存
                TOKEN.put(username, jwtUtils.refreshToken(auth_token));
            }
            if (flag) {
                UserVo userVo = jwtUtils.getUserFromToken(auth_token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userVo, null, userVo.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.debug(String.format("Authenticated user %s, setting security context", username));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
//
//            UserVo userVo = jwtUtils.getUserFromToken(auth_token);
//            if (jwtUtils.validateToken(auth_token, userVo) && jwtUtils.containToken(username, auth_token)) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userVo, null, userVo.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                logger.debug(String.format("Authenticated user %s, setting security context", username));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
        }
        chain.doFilter(request, response);
    }

    private void refreshToken(String token) {


    }
}