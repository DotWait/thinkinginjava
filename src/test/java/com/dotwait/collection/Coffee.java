package com.dotwait.collection;

public class Coffee {
    public String toString(){
        return "";
    }

    public <T> void show(T t){
        System.out.println(t.getClass().getName());
    }
}
