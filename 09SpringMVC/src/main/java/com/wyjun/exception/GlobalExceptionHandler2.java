package com.wyjun.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*如果响应方式要使用消息转换器，不使用 ModelAndView 机制，可以使用以下代码中的注解*/

@RestControllerAdvice  // 等价于 @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler2 {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 返回400状态码（将HTTP响应协议层的状态码设置一下，不设置的话在HTTP响应协议层状态码是200）
    public Map<String, Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        System.err.println("业务异常: code=" + e.getCode() + ", message=" + e.getMessage());
        e.printStackTrace();

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", e.getCode());
        result.put("message", e.getMessage());
        result.put("timestamp", LocalDateTime.now());
        result.put("path", request.getRequestURI());
        return result;
    }

    // 处理空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 返回500状态码（将HTTP响应协议层的状态码设置一下，不设置的话在HTTP响应协议层状态码是200）
    public Map<String, Object> handleNullPointer(NullPointerException e, HttpServletRequest request) {
        System.err.println("空指针异常:");
        e.printStackTrace();

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", 500);
        result.put("message", "系统内部错误，请联系管理员");
        result.put("timestamp", LocalDateTime.now());
        result.put("path", request.getRequestURI());
        return result;
    }

    // 处理所有其他异常（兜底）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleAllExceptions(Exception e, HttpServletRequest request) {
        System.err.println("未捕获异常: URI=" + request.getRequestURI());
        e.printStackTrace();

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", 500);
        result.put("message", "系统繁忙，请稍后重试");
        result.put("timestamp", LocalDateTime.now());
        result.put("path", request.getRequestURI());
        return result;
    }

    // 处理404异常（需要配置 spring.mvc.throw-exception-if-no-handler-found=true）
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 返回404状态码（将HTTP响应协议层的状态码设置一下，不设置的话在HTTP响应协议层状态码是200）
    public Map<String, Object> handleNotFound(NoHandlerFoundException e, HttpServletRequest request) {
        System.err.println("404 - 页面未找到: " + e.getRequestURL());

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", 404);
        result.put("message", "请求的资源不存在");
        result.put("path", e.getRequestURL());
        result.put("timestamp", LocalDateTime.now());
        return result;
    }
}