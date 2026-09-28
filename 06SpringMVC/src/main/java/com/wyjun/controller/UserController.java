package com.wyjun.controller;

import com.wyjun.dao.UserDao;
import com.wyjun.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/user/list")
    public String userList(Model model) {
        List<User> userList = userDao.getAllUsers();
        model.addAttribute("users", userList);

        return "user_list";
    }

    @PostMapping("/api/user/add")
    public String userAdd(User user) {
        userDao.addUser(user);

        //新增用户后，重定向到列表页面
        return "redirect:/user/list";
    }

    @GetMapping("/user/modify/{id}")
    public String modifyUserById(@PathVariable("id") Long id, Model model) {
        User user = userDao.getUserById(id);
        model.addAttribute("user", user);
        return "user_edit";
    }

    @PutMapping("/user/update")
    public String updateUserById(User user) {
        //修改用户信息
        userDao.updateUserById(user);

        //更新用户后，重定向到列表页面
        return "redirect:/user/list";
    }

    @DeleteMapping("/user/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        //删除用户信息
        userDao.deleteUserById(id);

        //更新用户后，重定向到列表页面
        return "redirect:/user/list";
    }
}
