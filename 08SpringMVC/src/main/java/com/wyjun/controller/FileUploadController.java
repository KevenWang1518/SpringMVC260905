package com.wyjun.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Controller
public class FileUploadController {

    @PostMapping(value = "/file/upload")
    public String fileUpload(@RequestParam("fileName") MultipartFile multipartFile, HttpServletRequest request) {
        String name = multipartFile.getName();
        System.out.println(name);

        // 获取文件名
        String originalFilename = multipartFile.getOriginalFilename();
        System.out.println(originalFilename); //QQ.png

        // 最终要将文件存储到服务器的哪个目录中
        // 获取上传之后的存放目录
        ServletContext application = request.getServletContext();
        String uploadDir = application.getRealPath("/upload");
        System.out.println("file save in the directory = " + uploadDir);

        // 判断这个目录是否存在，如果服务器目录不存在则新建
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // 拼接文件名，采用UUID来生成文件名，防止服务器上传文件时产生覆盖
        String uploadFileName = uploadDirFile.getAbsolutePath() + "/" + UUID.randomUUID().toString() + originalFilename;

        // 获取输入流和输出流，一边读一边写
        try (BufferedInputStream in = new BufferedInputStream(multipartFile.getInputStream());
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(uploadFileName));) {
            byte[] bytes = new byte[1024 * 1024];
            int readCount = 0;
            while ((readCount = in.read(bytes)) != -1) {
                out.write(bytes, 0, readCount);
            }

            // 使用缓冲流记得刷新
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            // 不要吞没异常。一直往上返回异常，直到SpringMVC的全局异常处理器捕捉到该异常为止。
            throw new RuntimeException(e);
        }

        // 返回逻辑视图名字。
        return "success";
    }
}
