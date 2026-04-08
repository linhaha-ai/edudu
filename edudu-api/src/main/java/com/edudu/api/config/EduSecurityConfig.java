package com.edudu.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import com.edudu.api.security.EduAuthenticationEntryPoint;
import com.edudu.api.security.EduJwtAuthenticationFilter;
import com.edudu.api.security.EduLogoutSuccessHandler;

/**
 * API模块 spring security配置
 * 
 * @author edudu
 */
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
@Order(1) // 优先级高于 Admin 模块的 SecurityConfig
public class EduSecurityConfig
{
    /**
     * 认证失败处理类
     */
    @Autowired
    private EduAuthenticationEntryPoint unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private EduLogoutSuccessHandler logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private EduJwtAuthenticationFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 身份验证实现
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    public SecurityFilterChain eduFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
            // CSRF禁用，因为不使用session
            .securityMatcher("/api/**") // 只处理 /api/** 路径
            .csrf(AbstractHttpConfigurer::disable)
            // 禁用HTTP响应标头
            .headers((headersCustomizer) -> {
                headersCustomizer.cacheControl(HeadersConfigurer.CacheControlConfig::disable).frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
            })
            // 认证失败处理类
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            // 基于token，所以不需要session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求授权
            .authorizeHttpRequests((requests) -> {
                // 对于登录register 注册login 验证码captcha 允许匿名访问
                requests.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/captcha").permitAll()
                    // 静态资源，可匿名访问
                    .requestMatchers("/api/swagger-ui.html", "/api/v3/api-docs/**", "/api/swagger-ui/**").permitAll()
                    // 除上面外的所有请求全部需要鉴权认证
                    .anyRequest().authenticated();
            })
            // 添加Logout filter
            .logout(logout -> logout.logoutUrl("/api/auth/logout").logoutSuccessHandler(logoutSuccessHandler))
            // 添加JWT filter
            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            // 添加CORS filter
            .addFilterBefore(corsFilter, EduJwtAuthenticationFilter.class)
            .build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
