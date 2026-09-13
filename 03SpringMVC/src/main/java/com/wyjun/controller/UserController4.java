package com.wyjun.controller;

import com.wyjun.dto.CustomerDTO;
import com.wyjun.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class UserController4 {
    /*
    @RequestHeader作用:将请求头信息映射到方法的形参上。
    请求头上提交数据也是key value结构。
    Referer:http://localhost:8080/springmvc
    */
    @GetMapping(value = "/testheader")
    public String headerTest1(
            @RequestHeader(value = "Referer", required = false, defaultValue = "http://locahost:8080")
            String referer) {
        System.out.println(referer);
        return "success";
    }
    //http://locahost:8080

    @PostMapping(value = "/testheader2")
    public String headerTest2(
            @RequestHeader(value = "Referer", required = false, defaultValue = "http://locahost:8080")
            String referer,
            UserDTO userDTO) {
        System.out.println(referer);
        System.out.println(userDTO);
        return "success";
    }
    //http://www.wyjun.com
    //UserDTO(username=ZhangSan, password=zs123456, sex=1, hobby=1,2,3, introduce=I am zhangsan)

    @PostMapping(value = "/testcookie1")
    public String cookieTest1(
            @CookieValue(value = "username", required = false, defaultValue = "administrator")
            String cookieUsername,
            @CookieValue(value = "token", required = false, defaultValue = "123456789")
            String cookieToken,
            @CookieValue(value = "sessionID", required = false, defaultValue = "id12345678")
            String cookieSessionID,
            UserDTO userDTO) {
        System.out.println(cookieUsername + "-" + cookieToken + "-" + cookieSessionID);
        System.out.println(userDTO);
        return "success";
    }
    //admin-abc123456-id12345678
    //UserDTO(username=ZhangSan, password=zs123456, sex=1, hobby=1,2,3, introduce=I am zhangsan)
}
