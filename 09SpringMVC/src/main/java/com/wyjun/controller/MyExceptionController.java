package com.wyjun.controller;

import com.wyjun.exception.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;

@Controller
public class MyExceptionController {

    @GetMapping("/test/exception")
    public String testException() {
        System.out.println(10 / 0);//模拟除零异常
        return "success";
    }

    @GetMapping("/test/exception2")
    public String testException2() {
        //模拟转账异常
        int inMoney = 5;
        int outMoney = new Random().nextInt(10);
        if (inMoney < outMoney)
            throw new BusinessException("9527", "less money , transfer error");
        return "success";
    }

    @GetMapping("/test/exception3")
    public String testException3() {
        //模拟指针异常
        int in = 5;
        int out = new Random().nextInt(10);
        if (in < out)
            throw new NullPointerException();
        return "success";
    }
}
