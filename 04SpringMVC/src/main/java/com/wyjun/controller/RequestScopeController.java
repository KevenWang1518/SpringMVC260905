package com.wyjun.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class RequestScopeController {
    //补充小知识点：
    //forward（转发）：一次请求，地址栏不变，request共享
    //redirect（重定向）：两次请求，地址栏改变，request不共享

    //Servlet API参数request会被spring自动注入。
    @GetMapping("request/test1")
    public String requestTest1(HttpServletRequest request) {

        //向request域中绑定数据。
        request.setAttribute("username", "ZhangSan_HttpServletRequest");

        //跳转到逻辑视图。
        return "success";
    }

    // 使用Model接口也可以达到request域的效果。
    @GetMapping("request/test2")
    public String requestTest2(Model model) {

        //向request域中绑定数据。
        //MVC (Model View Controller)
        //Model是SpringMVC内置的接口，专门用来存储数据的。
        model.addAttribute("username", "ZhangSan_Model");

        //跳转到逻辑视图。
        return "success";
    }

    // 这种方式:map参数spring框架也会自动注入。这个map默认情况下是专门用来存储Model数据的。
    @GetMapping("request/test3")
    public String requestTest3(Map<String, Object> attributeMap) {

        //向map集合中添加键值对。
        attributeMap.put("username", "ZhangSan_Map");

        //跳转到逻辑视图。
        return "success";
    }

    @GetMapping("request/test4")
    public String requestTest4(ModelMap modelMap) {

        //这个ModelMap对象也可以存储Model数据。
        //modelMap.put("username", "ZhangSan_ModelMap");
        modelMap.addAttribute("username", "ZhangSan_ModelMap");

        //跳转到逻辑视图。
        return "success";
    }

    @GetMapping("request/test5")
    public ModelAndView requestTest5() {

        // 不需要框架注入了（上面方法不需要形参了），需要自己手动new一个ModelAndView对象。
        // MVC (Model  View  Controller)
        ModelAndView modelAndView = new ModelAndView();

        //设置Model，在Model中添加数据。
        modelAndView.addObject("username", "ZhangSan_modelAndView");

        //设置View，设置将来跳转的视图。
        modelAndView.setViewName("success");

        //返回ModelAndView对象
        return modelAndView;
    }
}
