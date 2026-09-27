package com.wyjun.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //生成无参数构造方法
@NoArgsConstructor  //生成全参数构造方法
public class User {
    private Long id;
    private String name;
    private String email;
    private Integer gender;
}