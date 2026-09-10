package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 *1.@RequestMapping中的 headers属性
 *2.headers属性是一个字符串数组。
 *3.给请求映射再添加一个约束。让绑定规则更严格。
 *4.作用:约束前端发送请求的时候，请求头当中必须有什么key或者必须不能有什么key，甚至可以约束到请求头提交时的value具体值。
 */
@Controller
public class HeadersController {
    //以下条件综合起来是:
    //1.get请求
    //2.请求路径必须是:/test/headers
    //3.请求头中不能有 Referer，但必须有Host
    @GetMapping(value = "/test/headers", headers = {"!Referer", "Host"})
    public String headersTest() {
        System.out.println("params mapping success");
        return "success";
    }
}
