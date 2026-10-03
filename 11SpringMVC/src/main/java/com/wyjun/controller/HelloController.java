package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "success";
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
