package com.wyjun.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/*如果响应方式使用 ModelAndView 机制，不使用消息转换器，可以使用以下代码中的注解*/

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常 - 页面跳转
    @ExceptionHandler(BusinessException.class) //异常处理器，BusinessException就走这个。
    public String handleBusinessException(BusinessException e, Model model) {
        // 使用标准输出记录日志（生产环境建议使用日志框架）
        System.err.println("BusinessException: code=" + e.getCode() + ", message=" + e.getMessage());
        e.printStackTrace();

        model.addAttribute("errorCode", e.getCode());
        model.addAttribute("errorMsg", e.getMessage() + "BusinessException，Contact to Administrator");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());

        //返回逻辑视图名称
        return "tip";
    }

    // 处理空指针异常
    @ExceptionHandler(NullPointerException.class) //异常处理器，NullPointerException就走这个
    public String handleNullPointer(NullPointerException e, Model model) {
        System.err.println("NullPointer Exception:" + e.getMessage());
        e.printStackTrace();

        model.addAttribute("error", "NullPointerException，Contact to Administrator");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());

        //返回逻辑视图名称
        return "tip";
    }

    // 处理除零异常
    @ExceptionHandler(ArithmeticException.class) //异常处理器，ArithmeticException 就走这个
    public String handleNullPointer(ArithmeticException e, Model model) {
        System.err.println("divide zero exception:");
        e.printStackTrace();

        model.addAttribute("error", "Divide Zero Exception，Contact to Administrator");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());

        //返回逻辑视图名称
        return "tip";
    }

    // 处理所有其它异常（兜底）
    @ExceptionHandler(Exception.class) //异常处理器，其它异常就走这个
    public String handleAllExceptions(Exception e, Model model, HttpServletRequest request) {
        System.err.println("未捕获异常: URI=" + request.getRequestURI());
        e.printStackTrace();

        model.addAttribute("error", "系统繁忙，请稍后重试");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        //返回逻辑视图名称
        return "tip";
    }

    // 处理404异常（需要配置）
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException e, Model model) {
        System.err.println("404 - 页面未找到: " + e.getRequestURL());

        model.addAttribute("error", "请求的页面不存在");
        model.addAttribute("path", e.getRequestURL());
        return "error/404";
    }
}