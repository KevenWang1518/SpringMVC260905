package com.wyjun.aop;

import com.wyjun.config.SpringConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringMVCTest {
    @Test
    public void SpingMybatisTest1() { //这里使用的是SpringConfig全注解配置类，不用再写spring.xml文件，注意获取context方法不同，效果相同。
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}
