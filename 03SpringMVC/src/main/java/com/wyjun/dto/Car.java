package com.wyjun.dto;

import lombok.Data;

@Data
public class Car {
    private String name;
    private String color;

    public void run() {
        System.out.println(name + "汽车跑起来了,颜色是" + color);
    }
}
