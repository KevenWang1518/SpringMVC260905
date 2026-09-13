package com.wyjun.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class UserController {
    /*
    1.使用原生的Servlet API获取用户提交的数据。
    2.这种方式不常用。
    3.但有的时候必须使用原生的Servlet API，比如文件下载。需要使用response对象来获取响应流。
    4.因此必须掌握。
    5.怎么获取原生的Servlet API?
    非常简单，只需要在 Controller#method()参数上定义Servlet API即可。
    Spring框架会自动注入，你不用管。
    */
    @PostMapping(value = "/register")
    public String paramsTest(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        System.out.println(request);
        System.out.println(response);
        System.out.println(session);

        //通过request来获取数据。不管前端是提交什么数据，都是以字符串的形式提交的，因此获取的数据都是String类型。
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sex = request.getParameter("sex");
        String[] hobbies = request.getParameterValues("hobby");
        String introduce = request.getParameter("introduce");

        System.out.println(username + "-" + password + "-" + sex);
        System.out.println(Arrays.toString(hobbies));
        System.out.println(introduce);

        return "success";
    }
}
