package com.wyjun.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

//@WebFilter("/*")
public class MyCharacterEncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 设置请求体的字符编码方式，解决POST请求乱码。
        servletRequest.setCharacterEncoding("UTF-8");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }
}