package com.wyjun.config;

import jakarta.servlet.Filter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// 以下配置是代替 web.xml 的。
// 等同于在web.xml文件中配置DispatcherServlet
// 1.指定SpringMVC.xml文件的位置。
// 2.指定spring.xml文件的位置。
// 3.配置映射路径url-pattern
// 4.字符编码过滤器
// 5.HiddenHttpMethodFilter


// 它的生命周期由Tomcat管理，而不是由Spring容器管理。
// WebConfig 类的作用是：配置DispatcherServlet本身，它会在Spring容器启动之前就被Servlet容器调用。
// 不需要@Configuration  // 注意这里不能添加 @Configuration 注解
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    // 我们之前写的SpringMVC.xml文件中的配置，既有spring的配置，又有SpringMVC的配置
    // 中大型项目一般都将这两个配置分开，我们这里将SpringMVC.xml拆分为两个配置类。
    // 1. SpringConfig 类编写Spring配置。
    // 2. SpringMvcConfig 类编写SpringMVC配置。

    // 指定Spring的配置
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    // 指定SpringMVC的配置
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    // 配置DispatcherServlet的 url-pattern
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // 配置字符编码过滤器以及RESTful过滤器
    @Override
    protected Filter[] getServletFilters() {
        // 字符编码过滤器
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceRequestEncoding(true);
        characterEncodingFilter.setForceResponseEncoding(true);

        // 模拟RESTful接口的过滤器
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        return new Filter[]{characterEncodingFilter, hiddenHttpMethodFilter};
    }
}
