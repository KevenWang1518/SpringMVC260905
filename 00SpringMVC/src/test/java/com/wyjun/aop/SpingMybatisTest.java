package com.wyjun.aop;

import com.wyjun.config.SpringConfig;
import com.wyjun.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

public class SpingMybatisTest {
    @Test
    public void SpingMybatisTest1() { //这里使用的是SpringConfig全注解配置类，不用再写spring.xml文件，注意获取context方法不同，效果相同。
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        AccountService accountService = context.getBean(AccountService.class);
        accountService.transfer("act-001", "act-002", new BigDecimal(10000.0));
    }
}
