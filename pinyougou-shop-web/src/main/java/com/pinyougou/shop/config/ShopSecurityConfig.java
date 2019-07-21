package com.pinyougou.shop.config;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/27 15:03
 **/
@EnableWebSecurity
public class ShopSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //设置放行资源,其余的都需要认证,设置所有的其他请求都需要认证通过即可 也就是用户名和密码正确即可不需要其他的角色
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "/plugins/**", "/*.html", "/seller/register.shtml").permitAll()
                .anyRequest().authenticated();
        //设置登录页面,登录请求地址,登录成功跳转地址,登录失败跳转地址
        http.formLogin()
                .loginPage("/shoplogin.html")
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
