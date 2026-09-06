package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String indexMethod() {
        //返回视图的逻辑名称
        return "index";
    }
}
