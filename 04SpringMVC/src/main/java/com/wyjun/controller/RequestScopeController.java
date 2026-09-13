package com.wyjun.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RequestScopeController {
    @GetMapping("requesttest1")
    public String requestTest1( HttpServletRequest request){
        return "success";

    }
}
