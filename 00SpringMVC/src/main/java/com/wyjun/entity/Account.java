package com.wyjun.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data //lombok自动生成 getter、setter、toString、构造器等样板代码
public class Account {
    private long id;
    private String actNo;
    private BigDecimal balance;
}
