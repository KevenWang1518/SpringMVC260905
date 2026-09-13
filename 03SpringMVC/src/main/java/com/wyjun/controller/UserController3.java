package com.wyjun.controller;

import com.wyjun.dto.CustomerDTO;
import com.wyjun.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
public class UserController3 {
    /*
        springmvc底层自动完成了这样一件事:
        它可以将前端提交过来的数据，自动封装到JavaBean对象的对应属性上。
        底层原理是什么?
            1.使用JavaBean接收数据更方便。
            2.前提条件是提交的参数name必须和JavaBean的属性名保持一致。
            3.底层实现原理是:通过反射机制调用setter方法给属性赋值的。
    伪代码:
        第一步:前端会发送请求提交数据。
            http://localhost:8080/springmvc/register
            body中提交数据:
                username=jack&password=123&sex=1&hobby=1&hobby=2&intro=jackok
        第二步:SpringMVC框架就开始解析这个参数了。
            首先:获取所有参数的name。
            Enumeration<String> names = request.getParameterNamesO);
            while (names.hasMoreElement()){
                String name = names.nextElement();// 获取到参数名字username了
                // 将参数的名字修改为setter方法的方法名。
                String setMethodName = "setUsername";
                // 调用setter方法。
                // 通过反射机制来调用setUsername()方法。注意不是直接给属性赋值。
                Method method= 通过反射获取到了这个set方法;
                // 获取属性的值
                String value = request.getParameter("username");
                method.invoke(user, value);
     */
    @PostMapping(value = "/register3")
    public String paramsTest(UserDTO userDTO) {//params只传给1个对象
        System.out.println(userDTO);
        return "success";
    }
    //UserDTO(username=ZhangSan, password=zs123456, sex=1, hobby=1,2,3, introduce=I am zhangsan)

    @PostMapping(value = "/register4")
    public String paramsTest2(UserDTO userDTO, CustomerDTO customerDTO, String username) {
        //params可以同时传给2个对象，但要求属性相同，否则属性为null
        //也可以只给一个username属性赋值，只要一个username的值
        System.out.println(userDTO);
        System.out.println(customerDTO);
        System.out.println(username);
        return "success";
    }
    //UserDTO(username=ZhangSan, password=zs123456, sex=1, hobby=1,2,3, introduce=I am zhangsan)
    //CustomerDTO(username=ZhangSan, password=zs123456, sex=1, hobby=1,2,3, introduce=I am zhangsan)
    //zhangsan
}
