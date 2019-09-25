package com.dotwait.lambda;

public interface TestA {
    default void show(int i){
        System.out.println(i);
    }
}
