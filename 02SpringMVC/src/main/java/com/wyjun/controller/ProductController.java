package com.wyjun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {

    @RequestMapping("/product/detail")
    public String detail() {
        System.out.println("查看商品明细");
        return "/product/detail"; //相当于最终生成"/WEB-INF/templates" + "/product/detail" + ".html"
    }
}
