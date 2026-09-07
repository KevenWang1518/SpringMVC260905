package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")//注意这里加不加/都是可以的
public class StudentController {

    @RequestMapping("/detail")//注意这里加不加/都是可以的
    public String detail() {
        System.out.println("display student detail");
        return "/student/detail"; //相当于最终生成"/WEB-INF/templates" + "/student/detail" + ".html"
    }

    @RequestMapping("modify")//注意这里加不加/都是可以的
    public String modify() {
        System.out.println("modify student detail");
        return "/student/modify"; //相当于最终生成"/WEB-INF/templates" + "/student/modify" + ".html"
    }
}
