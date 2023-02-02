package com.example.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .successForwardUrl("/loginSuccess")
                //登录失败页面
                .failureForwardUrl("/loginFail")
                //如果form表单账号密码的name不是username和password，就可以用下面两个属性来指定;如果是username和password就不用写下面两行
                .usernameParameter("un")
                .passwordParameter("pwd");

           //配置权限
           http.authorizeRequests()
                   //只要请求showLogin页面，就全部放行
                   //antMatchers,表示指定一个页面，permitAll表示不需要授权，全部放行
                   .antMatchers("/showLogin").permitAll()
                   //除了上面配置的，其他任何请求（anyRequest），都必须已经通过验证（authenticated）才能放行
                   .anyRequest().authenticated();

           //让csrf禁用，否则会一直验证，进入死循环
           http.csrf().disable();

    }
}