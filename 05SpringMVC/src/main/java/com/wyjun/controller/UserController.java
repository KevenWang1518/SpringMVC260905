package com.wyjun.controller;

//演示RESTfuL的CRUD操作。
/*
    在Spring MVC中怎么使用form表单，发送PUT/DELETE请求?
        1.必须是基于POST请求的。
        2.前端使用表单提交数据，表单中需要添加一个隐藏域:_method=PUT
        3.需要配置一个过滤器:HiddenHttpMethodFilter，拦截所有的请求。
    底层过滤器的原理:
        1.拦截所有的请求
        2.如果请求是一个POST请求，并且提交的数据当中有:_method=PUT/DELETE
        3.则springMVC认为前端提交的请求是一个PUT/DELETE请求。
*/

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    //根据id查询一个
    //发送一个GET请求，请求路径:/api/user/1
    @GetMapping("/api/user/{id}")
    public String getUserById(@PathVariable("id") String userId) {
        System.out.println("query user info by id = " + userId);
        return "success";
    }

    //查询所有
    //发送一个GET请求，请求路径:/api/user
    @GetMapping("/api/user")
    public String getAllUser() {
        System.out.println("query all user info");
        return "success";
    }

    //新增保存用户
    //发送一个POST请求，请求路径:/api/user
    @PostMapping("/api/user")
    public String saveUser(@RequestParam("username") String userName, @RequestParam("password") String passWord) {
        System.out.println("save user info by username & passWord  = " + userName + "-" + passWord);
        return "success";
    }

    //根据id删除数据
    @DeleteMapping("/api/user/{id}")
    public String deleteUserById(@PathVariable("id") String userId) {
        System.out.println("delete user info by id = " + userId);
        return "success";
    }

    //修改保存用户
    //发送PUT请求，请求路径:/api/user
    //要先在web.xml文件中增加filter过滤器设置，否则就走到POST新增保存用户路径了，不是PUT修改保存用户了。
    @PutMapping("/api/user")
    public String updateUser(@RequestParam("username") String userName, @RequestParam("password") String passWord) {
        System.out.println("update user info by username & passWord = " + userName + " - " + passWord);
        return "success";
    }
}
