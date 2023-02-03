package com.example.springsecurity.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //异步响应，需要处理乱码
        //响应状态设置为禁止，否则返回状态码是200，成功，不符合业务逻辑
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        //设置响应编码
        response.setHeader("Content-Type","application/json;charset=utf-8");

        //设置响应的流
        PrintWriter out = response.getWriter();
        //将数据输出到页面
        out.write("{\"status\":\"error\",\"msg\":\"权限不足，请联系管理员！\"}");
        out.flush();//刷新流
        out.close();//关闭流

    }
}
