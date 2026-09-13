package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
public class UserController2 {
    /*
    1.使用 @RequestParam 注解来获取请求的数据。
    2.该注解的作用:将请求参数与方法上的形参映射。
    3.@RequestParam 是针对于GET/POST请求提交的参数。
        专门为这种格式的数据设计的:name=value&name=value&name=value
    4.默认情况下:ORequestParam("username")这个代码要求前端必须提交 username 参数，如果前端没有提交，则400错误。
    5.怎么避免400错误?
        400是因为后端要求你提交某个/某些参数，结果前端没有提交这些参数时，报400错误。
        因为 @RequestParam 注解中有 required 属性，这个属性值默认是true，表示必须的。
        如果手动设置为false，则不再强制要求前端必须提交某个/某些参数。
    6.defaultValue属性的生效时机:
        前端没有提交这个参数，或者前端提交的参数的值是空字符串的时候。
    */
    @PostMapping(value = "/register2")
    public String paramsTest(
            @RequestParam(name = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "sex", required = false) String sex,
            //小细节:当前端提交数据的时候，多个name是相同的，例如:hobby=1&hobby=2&hobby=3
            //后端接收数据的时候，不一定使用数组，可以直接使用String来接收:@RequestParam("hobby") String[] hobbies,。底层会自动采用逗号进行字符串的拼接:"1,2,3"
            //也可以采用List集合来接收这个数据: @RequestParam("hobby") List<String> hobbies
            @RequestParam("hobby") String[] hobbies,
            @RequestParam(value = "introduce", defaultValue = "I am a superman") String introduce) {
        System.out.println(username + "-" + password + "-" + sex + "-" + Arrays.toString(hobbies) + "-" + introduce);
        return "success";
    }
}
