package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    //首页面的效果。
    //当请求路径是:http://localhost:8080/03springmvc
    //那么会将请求映射到:IndexController#index()
    @RequestMapping("/")
    public String indexMethod() {
        //返回视图的逻辑名称
        return "index";
    }
}
