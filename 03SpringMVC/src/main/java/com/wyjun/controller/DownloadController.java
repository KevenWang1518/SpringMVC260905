package com.wyjun.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class DownloadController {
    @GetMapping("/downloadpic")
    public void downloadTest(HttpServletRequest request, HttpServletResponse response) {
        try {

            // 获取ServletContext对象。
            ServletContext application = request.getServletContext();

            // 通过ServletContext获取文件绝对路径。
            String realPath = application.getRealPath("/image/24v8.jpg");
            File file = new File(realPath);

            // 设置图片相关的响应头
            response.setContentType("image/jpeg");

            // 设置文件下载行为：attachment 强制下载，不直接在浏览器中显示
            response.setHeader("Content-Disposition", "attachment; filename=\"24v8.jpg\"");
            response.setHeader("Content-Length", String.valueOf(file.length()));

            // 写入响应流
            Files.copy(file.toPath(), response.getOutputStream());
            response.flushBuffer();

        } catch (IOException e) {
            // 处理异常
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
