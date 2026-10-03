package com.wyjun.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/*
拦截器的执行规则:
1.拦截器的执行顺序按照 springmvc.xml 文件中配置的顺序依次执行。自上而下。
2.拦截器链中的任何一个拦截器 preHandLe返回false，目标Controller就不会执行，拦截器上的 postHandle也不会执行。
3.拦截器链中的所有拦截器preHandLe都返回true，则目标才会执行，拦截器链上的 postHandle才会执行。
4.假设有一个拦截器链，顺序是1,2,3，如果执行到3的时候preHandLe返回了false (1和2都返回true)，执行效果是什么?
    1--> preHandle
    2--> preHandle
    3--> preHandle
    2--> afterCompletion
    1--> afterCompletion
5.只要当前拦截器的 preHandle方法的返回值是true，则该拦截器中的 afterCompletion会执行。

SpringMVC九大角色：
1.前端控制器
2.处理器映射器HandlerMapping
3.处理器执行链HandlerExecutionChain
4.拦截器的preHandle
5.处理器适配器HandlerAdapter (ilDispatcherServlet统一面向处理器适配器调用方法，统一处理方案)因为SpringMVC底层有很多处理器的类型，Controller只是其中一个.
6.控制器Controller
7.ModelAndView对象
8.视图解析器ViewResolver
9.视图View
*/

@Component
public class MyInterceptor2 implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("MyInterceptor2's preHandle method is running");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("MyInterceptor2's postHandle method is running");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("MyInterceptor2's afterCompletion method is running");
    }
}
