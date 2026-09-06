package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FirstController {

    //@RequestMapping注解的作用是:将请求路径和对应的控制器的某个/某些方法绑定在一起。
    //翻译为:请求映射。
    @RequestMapping("/springmvc01")
    public String firstMvcMethod() {
        //返回视图的逻辑名称
        return "springmvc01";
    }

    //一个Controller控制器中可以编写多个方法。
    @RequestMapping("/springmvc02")
    public String secondMvcMethod() {
        //返回视图的逻辑名称
        return "springmvc02";
    }
}
// DispatcherServlet调用 FirstController.firstMethod()方法
// 最终这个firstMethod()方法返回一个视图的逻辑名称。
// DispatcherServLet 会将视图的逻辑名称转换成物理名称，怎么转换的？通过springmvc.xml配置文件中的视图解析器中配置来进行转换的。
// 视图的物理路径拼接成:/WEB-INF/templates/springmvc01.html
// 就是用的springmvc-servlet.xml文件中设置的前缀<property name="prefix" value="/WEB-INF/templates/"/>和后缀<property name="suffix" value=".html"/>来拼接的。
// templateResolver 模板解析器负责解析 /WEB-INF/templates/springmvc01.html 这个文件
// 最终解析完毕之后会生成一个HTML字符串。
// 视图解析器会将解析的结果交给 DispatcherServlet
// DispatcherServlet 将HTML字符串响应给客户端。