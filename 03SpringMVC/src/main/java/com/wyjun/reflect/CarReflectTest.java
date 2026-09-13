package com.wyjun.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CarReflectTest {
    public static void main(String[] args) throws Exception {

        // 1. 通过全类名获取Class对象
        Class<?> clazz = Class.forName("com.wyjun.dto.Car");

        // 2. 创建实例
        Object newCar = clazz.getDeclaredConstructor().newInstance();

        // 3. 获取私有字段
        Field nameField = clazz.getDeclaredField("name");
        Field colorField = clazz.getDeclaredField("color");
        nameField.setAccessible(true); // 允许访问私有
        colorField.setAccessible(true); // 允许访问私有
        nameField.set(newCar,"BMW");
        colorField.set(newCar, "BLACK");

        // 4. 获取run方法并调用
        Method runMethod = clazz.getDeclaredMethod("run");
        runMethod.invoke(newCar);
    }
}
