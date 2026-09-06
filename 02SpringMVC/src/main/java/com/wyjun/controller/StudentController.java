package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    @RequestMapping("detail")
    public String detail() {
        System.out.println("查看学生信息");
        return "/student/detail"; //相当于最终生成"/WEB-INF/templates" + "/student/detail" + ".html"
    }

    @RequestMapping("modify")
    public String modify() {
        System.out.println("修改学生信息");
        return "/student/modify"; //相当于最终生成"/WEB-INF/templates" + "/student/modify" + ".html"
    }
}
