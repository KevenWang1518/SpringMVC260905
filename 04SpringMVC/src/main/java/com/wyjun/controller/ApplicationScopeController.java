package com.wyjun.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
    在SpringMVC中怎么获取ServletContext对象?
        通过Servlet API来获取ServletContext对象。
*/

@Controller

public class ApplicationScopeController {

    @GetMapping("/application/test1")
    public String applicationTest1(HttpServletRequest httpServletRequest, Model model) {

        // 获取应用上下文对象。
        ServletContext servletContext = httpServletRequest.getServletContext();

        // 获取项目根路径
        System.out.println(servletContext.getContextPath());
        // 向application域中绑定数据
        servletContext.setAttribute("contextPath", servletContext.getContextPath());

        // 向model中绑定应用文件真实路径。
        model.addAttribute("filePath", servletContext.getRealPath("/WEB-INF/templates/index.html"));

        return "success";
    }
    // /04springmvc
    // app-path:/04springmvc
    // file-path:D:\GitSpace\SpringMVC260905\04SpringMVC\target\04SpringMVC-1.0-SNAPSHOT\WEB-INF\templates\index.html
}
