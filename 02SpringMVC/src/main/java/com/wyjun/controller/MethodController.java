package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
HTTP常用的请求一共是4个:
    GET
    POST
    PUT
    DELETE
以上4个请求方式是专门针对 RESTful编程风格而准备的。
    GET请求:获取服务器端的资源。
    POST请求:保存操作。
    PUT请求:修改操作。
    DELETE请求:删除操作。
*/

//测试@RequestMapping注解的method属性。
@Controller
public class MethodController {

    // 前端的请求路径和Controller#方法()绑定成功的前提是:value/path要对应上。并且 method属性也要对应上。
    // 只要有一个对应失败，则绑定失败。
    // method属性不是必须的。去掉之后，映射过程对method就没有具体的要求了。
    // method是一个数组。可以编写多种请求方式。
    //@RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    //@RequestMapping(value = "/save", method = RequestMethod.POST)

    /*
    SpringMVC为不同的请求方式准备了衍生Mapping语法糖:
        @GetMapping:专门接收get请求
        @PostMapping:专门接收post请求
        @PutMapping:专门接收put请求
        @DeleteMapping:专门接收delete请求
    注意:以上4个注解只能出现在方法上，不能写到类上面。
    */
    @PostMapping("/save")
    public String testMethod() {
        System.out.println("测试@RequestMapping解的method属性");
        return "success";
    }
}
