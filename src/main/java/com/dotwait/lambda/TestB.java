package com.dotwait.lambda;

public interface TestB {
    default void show(int str){
        System.out.println(str);
    }
}
