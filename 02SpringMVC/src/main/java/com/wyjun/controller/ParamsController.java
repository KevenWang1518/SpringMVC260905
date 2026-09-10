package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// @RequestMapping注解中的params属性。
// 作用:是控制前端必须提交什么参数，或者前端不能提交什么参数。甚至可以控制前端提交的参数的具体值。
// 添加了params属性，等于又添加了一个约束条件，映射规则更加严格了。
// 当前端提交的参数和后端要求的参数不一致的时候，会出现400错误。

@Controller
public class ParamsController {

    // @GetMapping(value = "/test/params", params = {"username", "password"})
    // @GetMapping(value = "/test/params", params = {"!username", "password"})
    // @GetMapping(value = "/test/params", params = {"username=admin", "password"})

    //要求前端必须发送get请求。
    @GetMapping(value = "/test/params", params = {"username!=admin", "password"})
    public String paramsTest() {
        System.out.println("params mapping success");
        return "success";
    }
}
