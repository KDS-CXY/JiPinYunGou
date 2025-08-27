package com.baizhanshopping.shopping_manager_api.security.config;

import com.baizhanshopping.shopping_manager_api.security.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//开启鉴权配置注解
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * 安全过滤器链
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form ->
                form.usernameParameter("username")//设置用户名参数名
                        .passwordParameter("password")//设置密码参数名
                        .loginProcessingUrl("/admin/login")//设置登录处理url
                        .successHandler(new MyLoginSuccessHandle())//设置登录成功处理
                        .failureHandler(new MyLoginFailureHandler())//设置登录失败处理
                );
        http.authorizeHttpRequests(resp->{
            resp.requestMatchers("/login","/admin/login").permitAll();//设置登录相关的url无需认证
            resp.anyRequest().authenticated();//设置其他url需要认证
        });
        http.logout(logout->{
            logout.logoutUrl("/admin/logout")//设置注销成功url
                    .logoutSuccessHandler(new MyLogoutSuccessHandler())//设置注销成功处理
                    .clearAuthentication(true)//设置注销时清除认证信息
                    .invalidateHttpSession(true);//设置注销时失效会话
        });
        http.exceptionHandling(exception->{
            exception.authenticationEntryPoint(new MyAuthenticationEntryPoint())//设置未认证处理
                    .accessDeniedHandler(new MyAccessDeniedHandler());//设置无权限处理
        });
        http.cors(cors->cors.disable());//禁用cors
        http.csrf(csrf->csrf.disable());//禁用csrf
        return http.build();
    }
    /**
     * 密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
