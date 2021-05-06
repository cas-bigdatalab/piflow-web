package cn.cnic.base.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cn.cnic.base.config.jwt.CustomUserDetailsService;
import cn.cnic.base.config.jwt.JwtAuthenticationEntryPoint;
import cn.cnic.base.config.jwt.JwtAuthenticationTokenFilter;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.constant.SysParamsCache;

@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//Pre-Method Check for Allowing Page Entry
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();


    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Override
    public void configure(WebSecurity web) {
        //Solving the problem of static resources being intercepted
        web.ignoring().antMatchers("/components/**", "/js/**", "/css/**", "/my_js/*", "/my_js/**", "/my_css/*", "/my_css/**", "/img/**", "/img/*", "/images/**", "/images/*"
                , "/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**"
                );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());//Add a custom userDetailsService certificate
        auth.eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // 对于获取token的rest api要允许匿名访问
                //.antMatchers("/api/v1/auth", "/api/v1/signout", "/error/**", "/api/**").permitAll()
                .antMatchers("/register", "/checkUserName", "/jwtLogin", "/error", "/login").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers().permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        // 禁用缓存
        http.headers().cacheControl();

        // 添加JWT filter
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);


        if (SysParamsCache.IS_IFRAME) {
            http.headers().frameOptions().disable();
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
