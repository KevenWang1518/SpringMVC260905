package com.wyjun.entity;

import java.util.Arrays;
import java.util.Random;

public class MyArray {
    public static void main(String[] args) {

        int[] intArray = new int[10000];

        for (int i = 0; i < 10000; i++) {
            intArray[i] = new Random().nextInt(10000);
            System.out.print(intArray[i] + " ");
        }

        Arrays.sort(intArray);

        System.out.println("---------------");
        for (int i = 0; i < 10000; i++) {
            System.out.print(intArray[i] + " ");
        }
    }
}
