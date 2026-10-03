package com.wyjun.config;

import com.wyjun.interceptor.LogInterceptor;
import com.wyjun.interceptor.SecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

// SpringMVC容器和Spring容器是两个独立的容器
// Spring是父容器（Root容器），SpringMVC是子容器。
// Spring扫描和SpringMVC不要重复扫描。
// SpringMVC只负责扫描 @Controller和@ControllerAdvice
// 如果你把 @Controller和@ControllerAdvice都放到controller包下，以下组件扫描时只需要指定controller包即可。
// 注意:所有controller的扫描需要放到SpringMvcConfig中，如果只放到 SpringConfig 中进行扫描是不行的。
@Configuration
@EnableWebMvc // 表示启用springMVC的配置，代替的是：<mvc:annotation-driven/>
@ComponentScan(basePackages = {"com.wyjun.controller"}) //注意：SpringConfig配置文件中扫描的包是@ComponentScan(basePackages = "com.wyjun"。
public class SpringMvcConfig implements WebMvcConfigurer {
    // 配置Thymeleaf视图解析器
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        return viewResolver;
    }

    private SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    // 静态资源配置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/")
                .setCachePeriod(3600);
    }

    // 配置视图控制器（对于没有业务的Controller可以直接配置，不需要写）
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    // 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置安全权限拦截器
        SecurityInterceptor securityInterceptor = new SecurityInterceptor();
        // 指定哪些路径拦截，哪些不拦截
        registry.addInterceptor(securityInterceptor).addPathPatterns("/**").excludePathPatterns("/test");

        // 配置日志记录拦截器
        LogInterceptor logInterceptor = new LogInterceptor();
        // 指定哪些路径拦截，哪些不拦截
        registry.addInterceptor(logInterceptor).addPathPatterns("/**").excludePathPatterns("/test");
    }
}