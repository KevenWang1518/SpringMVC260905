package com.wyjun.controller;

import com.wyjun.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EntityController {
    @RequestMapping("/send")
    @ResponseBody
    public String send(RequestEntity<User> requestEntity) {
        System.out.println("Request Method : " + requestEntity.getMethod());
        System.out.println("Request URL : " + requestEntity.getUrl());
        HttpHeaders headers = requestEntity.getHeaders();
        System.out.println("Request Content Type : " + headers.getContentType());
        System.out.println("Request Headers : " + headers);

        //直接获取请求体，底层会自动将请求体的数据转换成java对象。
        User user = requestEntity.getBody();
        System.out.println(user);
        System.out.println(user.getUsername());
        System.out.println(user.getAge());
        return "success";
    }

    // 自己定制相应协议的话可以使用这种方式。
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = new User("JohnLiu", 23);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
        } else {
            return ResponseEntity.ok(user); // 200
        }
    }
}
/*
Request Method : POST
Request URL : http://localhost:8080/07springmvc/send
Request Content Type : application/json;charset=UTF-8
Request Headers : [accept:"**", accept-encoding:"gzip, deflate, br", user-agent:"PostmanRuntime-ApipostRuntime/1.1.0", connection:"keep-alive", cache-control:"no-cache", host:"localhost:8080", content-length:"44", Content-Type:"application/json;charset=UTF-8"]
User(username=LucyZhang, age=22)
LucyZhang
22
*/
