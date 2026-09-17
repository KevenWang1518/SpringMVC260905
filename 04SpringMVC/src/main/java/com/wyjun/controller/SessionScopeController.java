package com.wyjun.controller;

/*
    在SpringMVC中怎么获取session对象?
    第一种方式:使用原生的Servlet API。
    第二种方式:使用注解:@SessionAttributes(value ={"x"，"y"})
*/

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller

// 注解的作用:设置哪些数据绑定到session域中。这里的value属性用来指定多个键。
@SessionAttributes(value = {"sex", "age"})

public class SessionScopeController {

    @GetMapping("/session/test1")
    public String sessionTest1(HttpSession httpSession) {

        // 向session会话对象中绑定数据。
        httpSession.setAttribute("username", "ZhangSan_HttpSession");
        // 返回逻辑视图名称。
        return "success";
    }

    @GetMapping("/session/test2")
    public String sessionTest1(ModelMap modelMap) {

        //向session域中存储数据。
        modelMap.addAttribute("sex", "woman");
        modelMap.addAttribute("age", "22");
        // 返回逻辑视图名称。
        return "success";
    }

}
