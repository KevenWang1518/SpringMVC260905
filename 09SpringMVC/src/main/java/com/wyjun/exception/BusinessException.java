package com.wyjun.exception;

/**
 * 自定义业务异常类
 */

public class BusinessException extends RuntimeException {

    private String code;    // 错误码
    private String message; // 错误信息

    // 构造方法
    public BusinessException() {
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    // Getter 方法
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // Setter 方法
    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}