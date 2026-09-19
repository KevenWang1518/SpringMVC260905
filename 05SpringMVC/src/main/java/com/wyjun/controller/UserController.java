package com.wyjun.controller;

//演示RESTfuL的CRUD操作。

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    //根据id查询一个
    //发送一个GET请求，请求路径:/user/1
    @GetMapping("/api/user/{id}")
    public String getUserById(@PathVariable("id") String userId) {
        System.out.println("query user info by id = " + userId);
        return "success";
    }

    //查询所有
    //发送一个GET请求，请求路径:/user
    @GetMapping("/api/user")
    public String getAllUser() {
        System.out.println("query all user info");
        return "success";
    }

    //新增保存用户
    //发送一个POST请求，请求路径:/user
    @PostMapping("/api/user")
    public String saveUser(@RequestParam("username") String userName, @RequestParam("password") String passWord) {
        System.out.println("save user info by name  = " + userName + "-" + passWord);
        return "success";
    }

    //根据id删除数据
    @DeleteMapping("/api/user/{id}")
    public String deleteUserById(@PathVariable("id") String userId) {
        System.out.println("delete user info by id = " + userId);
        return "success";
    }

    //修改
    //发送PUT请求，请求路径:/user
    @PutMapping("/api/user")
    public String updateUser(@RequestParam("username") String userName) {
        System.out.println("update user info by name = " + userName);
        return "success";
    }
}
