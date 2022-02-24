package com.auth.config;

import com.auth.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * 配置授权服务器
 *
 * @author by lucongwen
 * @Date 2022/2/24 16:10
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource(name = "redisTokenStore")
    private TokenStore tokenStore;

    @Resource
    private UserServiceImpl userService;

    /**
     * 配置访问授权服务器验证参数携带方式
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许在请求的表单参数中携带授权服务器验证用户名和密码
                .allowFormAuthenticationForClients()
                // 允许未携带 token 的请求访问授权服务器
                .checkTokenAccess("permitAll()");
    }

    /**
     * 配置授权服务器授权账户名和密码
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 此处将用户名和密码放在内存中，实际开发中可放在数据库中，授权时查询数据库是否存在该账户密码
                .inMemory()
                // 配置 client-id
                .withClient("admin")
                // 配置 client-secret
                .secret(passwordEncoder.encode("112233"))
                // 配置访问 token 的有效期
                .accessTokenValiditySeconds(3600)
                // 配置刷新 Token 有效期
                .refreshTokenValiditySeconds(86400)
                // 自动授权配置
                .autoApprove(true)
                // 配置申请的权限范围
                .scopes("all")
                // 配置 grant-type，表示授权类型
                .authorizedGrantTypes("password", "refresh_token", "authorization_code");
    }

    /**
     * 配置密码模式，通过 Spring Security 的用户名密码进行授权
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 配置 token 存储位置
                .tokenStore(tokenStore)
                // 配置 AuthenticationManager
                .authenticationManager(authenticationManager)
                // 关联 Spring Security 的用户名和密码进行授权
                .userDetailsService(userService);
    }
}
