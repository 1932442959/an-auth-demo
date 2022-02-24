package com.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 配置加密方式
 *
 * @author by lucongwen
 * @Date 2022/2/24 16:02
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 初始化一个 AuthenticationManager，密码模式需要使用
     *
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置请求
        http
                // 禁用 csrf
                .csrf().disable()
                // 对请求配置放行规则
                .authorizeRequests()
                // 放行 oauth 路径获取授权
                .antMatchers("/oauth/**").permitAll()
                // 其他请求不放行，需要授权
                .anyRequest().authenticated();
    }
}
