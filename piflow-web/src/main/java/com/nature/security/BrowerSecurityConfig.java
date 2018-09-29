package com.nature.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin() // 定义当需要用户登录时候，转到的登录页面。
				.and().authorizeRequests() // 定义哪些URL需要被保护、哪些不需要被保护
				.anyRequest() // 任何请求,登录后可以访问
				.authenticated();
	}
}