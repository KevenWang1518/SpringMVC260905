package com.wyjun.controller;

import com.wyjun.entity.User;
import com.wyjun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    //自动注入了:通过构造方法注入的。
    private final UserService userService;

    @PostMapping("/save")
    public String save(User user) {
        userService.save(user);
        return "success";
    }
}
