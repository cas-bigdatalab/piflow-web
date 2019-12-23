package com.nature.base.config;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SpringContextUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.SysRoleType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.statistics.model.Statistics;
import com.nature.component.system.model.SysMenu;
import com.nature.component.system.model.SysRole;
import com.nature.component.system.model.SysUser;
import com.nature.component.system.vo.SysMenuVo;
import com.nature.domain.statistics.StatisticsDomain;
import com.nature.mapper.system.SysMenuMapper;
import com.nature.mapper.system.SysUserMapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//Pre-Method Check for Allowing Page Entry
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Override
    public void configure(WebSecurity web) throws Exception {
        //Solving the problem of static resources being intercepted
        web.ignoring().antMatchers("/components/**", "/js/**", "/css/**", "/custom/css/**", "/img/**", "/img/*", "/druid/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());//Add a custom userDetailsService certificate
        auth.eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/bootPage/initPage").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/checkUserName").permitAll()
                .anyRequest()//all others request authentication
                .authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().successHandler(loginSuccessHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler());
        if (SysParamsCache.IS_IFRAME) {
            http.headers().frameOptions().disable();
        }
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler() { //Login processing
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                UserVo userDetails = (UserVo) authentication.getPrincipal();
                String remoteAddr = request.getLocalAddr();
                Statistics statistics = new Statistics();
                statistics.setId(SqlUtils.getUUID32());
                statistics.setLoginUser(userDetails.getUsername());
                statistics.setLoginTime(new Date());
                statistics.setLoginIp(remoteAddr);
                StatisticsDomain statisticsDomain = (StatisticsDomain) SpringContextUtil.getBean("statisticsDomain");
                statisticsDomain.saveOrUpdate(statistics);
                logger.info("USER : " + userDetails.getUsername() + " LOGIN SUCCESS !  ");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { //Logout processing
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                try {
                    UserVo userVo = (UserVo) authentication.getPrincipal();
                    logger.info("USER : " + userVo.getUsername() + " LOGOUT SUCCESS !  ");
                } catch (Exception e) {
                    logger.error("LOGOUT EXCEPTION , e : ", e);
                }
                httpServletResponse.sendRedirect("/piflow-web/login");
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {    //Implementation of user login
        return new UserDetailsService() {

            UserVo getUserDetails(String username) {
                UserVo userVo = null;
                SysUserMapper sysUserMapper = (SysUserMapper) SpringContextUtil.getBean("sysUserMapper");
                SysMenuMapper sysMenuMapper = (SysMenuMapper) SpringContextUtil.getBean("sysMenuMapper");
                SysUser sysUser = sysUserMapper.findUserByUserName(username);
                String password = "";
                if (null != sysUser) {
                    userVo = new UserVo();
                    password = sysUser.getPassword();
                    userVo.setUsername(sysUser.getUsername());
                    userVo.setPassword(sysUser.getPassword());
                    userVo.setName(sysUser.getName());
                    userVo.setAge(sysUser.getAge());
                    List<SysRole> sysRoleTypes = sysUser.getRoles();
                    userVo.setRoles(sysRoleTypes);
                    if (CollectionUtils.isNotEmpty(sysRoleTypes)) {
                        SysRoleType sysRoleHighest = null;
                        String[] valueArray = new String[sysRoleTypes.size()];
                        for (int i = 0; i < sysRoleTypes.size(); i++) {
                            SysRole sysRole = sysRoleTypes.get(i);
                            if (null != sysRole && null != sysRole.getRole()) {
                                SysRoleType role = sysRole.getRole();
                                valueArray[i] = sysRole.getRole().getValue();
                                if (role == SysRoleType.ADMIN) {
                                    sysRoleHighest = role;
                                }
                            }

                        }
                        if (valueArray.length > 0) {
                            userVo.setAuthorities(AuthorityUtils.createAuthorityList(valueArray));
                            if (null == sysRoleHighest) {
                                sysRoleHighest = SysRoleType.USER;
                            }
                        }
                        List<SysMenu> sysMenuList = sysMenuMapper.getSysMenuList(sysRoleHighest.getValue());
                        if (CollectionUtils.isNotEmpty(sysMenuList)) {
                            List<SysMenuVo> sysMenuVoList = new ArrayList<>();
                            SysMenuVo sysMenuVo = null;
                            for (SysMenu sysMenu : sysMenuList) {
                                sysMenuVo = new SysMenuVo();
                                BeanUtils.copyProperties(sysMenu, sysMenuVo);
                                sysMenuVoList.add(sysMenuVo);
                            }
                            userVo.setSysMenuVoList(sysMenuVoList);
                        }
                    }
                }
                return userVo;
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserVo user = getUserDetails(username);
                if (user == null) {
                    throw new UsernameNotFoundException("Username " + username + " not found");
                }
                return user;
            }
        };
    }

}
