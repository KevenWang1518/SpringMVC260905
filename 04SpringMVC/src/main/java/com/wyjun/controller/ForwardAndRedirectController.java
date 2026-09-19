package com.wyjun.controller;

/*
    SpringMVC中如何完成转发和重定向。
    当转发的时候，也就是说Controller#method(返回的结果是:"forward:xxx"，底层会创建 InternalResourceView对象来处理。
    当重定向的时候，也就是说Controller#method()返回的结果是:"redirect:xxx"，底层会创建 RedirectView对象来处理。
    当返回的名字是一个建辑视图名称，也就是读Controller#nethod(返回的结果是:"xxx"，底层会创建对应的视图对象来处理，如果是Thymeleaf则创建:ThymeLeafView对象来处理。
*/

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForwardAndRedirectController {

    @GetMapping("/a")
    public String a(HttpServletRequest httpServletRequest) {
        System.out.println("a method is running!");
        httpServletRequest.setAttribute("username", "ZhangSan_forward");

        // 需要转发过去的话，需要在开始的位置添加一个"forward:”标识，这样SpringMVC就会自动将其看做转发动作了。
        // 注意：转发只能是项目内部资源的跳转。
        // 转发到/c
        return "forward:/c";

        // 这是逻辑视图名，不是转发，最终springmvc会将它转换成物理视图名:/WEB-INF/templates/c.html
        // 注意：这样写只是会显示c.html这个网页，而不是转发效果。
        // return "/c";
    }

    @GetMapping("/b")
    public String b(HttpServletRequest httpServletRequest) {
        System.out.println("b method is running!");
        httpServletRequest.setAttribute("username", "ZhangSan_redirect");

        // 重定向的前缀标识:"redirect:”
        // 重定向到/c，浏览器发送一次全新的请求。
        return "redirect:/c";

        //如果重定向的时候需要跨域。怎么写?
        //重定向到其他项目的资源。(编写全路径即可。)
        //例如：return "redirect:http://localhost:8080/02springmvc/login";
        //例如：return "redirect:http://172.168.10.20:8081/05springmvc/index";
    }

    @GetMapping("/c")
    public String c() {
        System.out.println("c method is running!");

        // 返回逻辑视图名
        return "c";
    }

}
