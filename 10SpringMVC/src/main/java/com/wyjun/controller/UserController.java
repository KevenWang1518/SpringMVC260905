package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/test/interceptor")
    public String testInterceptor() {
        System.out.println("Interceptor method is running");
        return "success";
    }
}
