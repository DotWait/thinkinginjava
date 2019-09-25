package com.dotwait.parameter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FilterApples {
    private static List<Apple> inventory;
    public static <T> List<T> filterApples(List<T> inventory, ApplePredicate<T> p){
        List<T> result = new ArrayList<>();
        for (T apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(){
        filterApples(inventory, (Apple apple) -> "green".equals(apple.getColor()));
        Comparator<Apple> byWeight = Comparator.comparing(Apple::getWeight);
    }
}
