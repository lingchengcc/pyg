package com.pinyougou.manager.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/**
 * @author silent
 * @version 1.0
 * @date 2019/6/27 15:03
 **/
@EnableWebSecurity
public class ManagerSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //设置放行资源,其余的都需要认证,不需要授权
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "/plugins/**", "/login.html").permitAll()
                .anyRequest().authenticated();
        //设置登录页面,登录请求地址,登录成功跳转地址,登录失败跳转地址
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin/index.html", true)
                .failureUrl("/login?error");
        //设置注销请求路径,清空session
        http.logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true);
        //关闭跨域请求伪造
        http.csrf().disable();
        //开启同源iframe访问策略
        http.headers().frameOptions().sameOrigin();
    }
}
