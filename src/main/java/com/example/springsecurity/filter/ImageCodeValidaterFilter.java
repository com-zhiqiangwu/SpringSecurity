package com.example.springsecurity.filter;

import com.example.springsecurity.controller.SpringSecurityDemoController;
import com.example.springsecurity.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ImageCodeValidaterFilter  extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //如果访问的是/login并且为post请求
            if (StringUtils.equals("/login",request.getRequestURI())
            && StringUtils.equals(request.getMethod(),"post")){
                    //获取session的验证码
                    String sessionCode = (String) request.getSession().getAttribute(SpringSecurityDemoController.SESSION_KEY);
                    //获取用户输入的验证码
                    String code = request.getParameter("code");
                    //判断是否正确
                    if (sessionCode == null || !sessionCode.equals(code)){
                       throw  new ValidateCodeException("验证码错误！");
                    }
                }
        } catch (AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request,response,e);
            return;
        }
        filterChain.doFilter(request,response);
    }

}
