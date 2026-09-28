package com.wyjun.controller;

import com.wyjun.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HttpMessageController {

    @GetMapping("/getMsgString")
    @ResponseBody //启用消息转换器
    public String getMsgString() {
        //由于返回的是普通字符串，因此底层会使用 StringHttpMessageConverter
        //这里返回的就不是一个逻辑视图名了，是一个普通的字符串。不走ModelAndView机制了。
        return "hello message";
    }

    @GetMapping("/getMsgJson")
    @ResponseBody //启用消息转换器
    public User getUser() {
        User user = new User("ZhangSan", 20);
        //当返回一个java对象，并且使用了@ResponseBody注解，那么底层会自动启用:MappingJackson2HttpMessageconverter
        return user;
    }
}
