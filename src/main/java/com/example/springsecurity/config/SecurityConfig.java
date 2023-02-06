package com.example.springsecurity.config;

import com.example.springsecurity.exception.MyAccessDeniedHandler;
import com.example.springsecurity.filter.ImageCodeValidaterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    /**
     * 配置密码解析bean实例
     * 加载到IOC容器
     * 这是spring security自定义登录逻辑的硬性要求
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    /**
     * 重写配置方法
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ImageCodeValidaterFilter imageCodeValidaterFilter = new ImageCodeValidaterFilter();
        //配置登录页面，注意请求，要和controller中一致
        http.formLogin()
                //只要你没认证的情况下，请求服务器当前应用程序的端口，都会到这个页面；
                //如果请求这个页面会跳转到登录页面（这个逻辑在controller中编写）
                .loginPage("/showLogin")
                //当url中有/login,那么就会执行我们的自定义登录逻辑，就是我们上面配置的
                //这里还指定登录成功后地址栏中显示的地址，如果请求上面的页面，就跳转到登录页面
                //(这个是controller层控制的)，但是地址栏显示的，是这里设置的地址
                .loginProcessingUrl("/login")
                //登录成功后返回的页面。就是地址栏里虽然是上面设置的/login，但是实际登录成功后，访问的请求是这个
//                .successForwardUrl("/loginSuccess")
                //当我们登录成功进入页面，刷新时，会提示重新提交表单
                // 解决重复提交表单问题(由跳转改为重定向)，并且可以站外转发（前后端分离项目使用）
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/loginSuccess");
                    }
                })
                //登录失败页面
//                .failureForwardUrl("/loginFail")
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        response.sendRedirect("/loginFail");
                    }
                })
                //如果form表单账号密码的name不是username和password，就可以用下面两个属性来指定;如果是username和password就不用写下面两行
                .usernameParameter("un")
                .passwordParameter("pwd");


            //添加图形验证码到filter之前
            http.addFilterBefore(imageCodeValidaterFilter, UsernamePasswordAuthenticationFilter.class);


        //配置权限
           http.authorizeRequests()
                   //只要请求showLogin页面，就全部放行
                   //antMatchers,表示指定一个页面，permitAll表示不需要授权，全部放行
                   .antMatchers("/showLogin","/loginFail","/code/image", "/css/**","/js/**","/images/**","/layui/**").permitAll()
                   //给访问路径设置权限。如/loginSuccess设置为具有admin1权限才能访问
                   .antMatchers("/loginSuccess").hasAuthority("admin1")
                   //拒绝访问abc的一切请求
                   //.antMatchers("/abc").denyAll()
                   //只有具有权限为admin的角色才能访问/abc
                   .antMatchers("/abc").hasAuthority("admin")
                   //除了上面配置的，其他任何请求（anyRequest），都必须已经通过验证（authenticated）才能放行
                   .anyRequest().authenticated();

           //配置异常处理
           http.exceptionHandling()
                   //配置，拒绝访问处理程序为我们刚才写的组件
                   .accessDeniedHandler(myAccessDeniedHandler);

           //让csrf禁用，否则会一直验证，进入死循环
           http.csrf().disable();

    }
}
