package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/user/detail")
    public String detail() {
        System.out.println("display user detail");
        return "/user/detail"; //相当于最终生成"/WEB-INF/templates" + "/user/detail" + ".html"
    }
}
