package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ValueController {
    //value属性和path属性可以看做是同一个属性。
    //value和path都是用来指定绑定的具体的url。(请求路径)
    //value和path都是数组，也就是说支持多个URL绑定一个Controller#method()

    @RequestMapping(value = {"/a", "b"})  //2个路径映射到同一个方法
    //@RequestMapping(path = {"/a", "b"})  //这里把value写成path也是可以的
    //@RequestMapping({"/a", "b"})  //value和path都不写也是可以的
    public String success() {
        System.out.println("测试@RequestMapping注解的value和path属性");
        return "success";
    }
}
