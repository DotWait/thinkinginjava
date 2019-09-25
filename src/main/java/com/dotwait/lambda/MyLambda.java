package com.dotwait.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyLambda {
    private static List<Apple> apples = new ArrayList<>();
    private static Random random = new Random();

    public static void init() {
        for (int i = 0; i < 20; i++) {
            apples.add(new Apple(random.nextInt(4), random.nextInt(20)));
        }
    }

    public static List<Apple> getApples(){
        return apples;
    }

    public static void myLambda() {

    }

    public List<Apple> filterGreenApple() {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : apples) {
            if (apple.getColor() == 1) {
                result.add(apple);
            }
        }
        return result;
    }

    public List<Apple> filterWeightApple() {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : apples) {
            if (apple.getWeight() > 10) {
                result.add(apple);
            }
        }
        return result;
    }

    public static boolean isGreenApple(Apple apple){
        return 1 == apple.getColor();
    }

    public static boolean isHeavyApple(Apple apple){
        return apple.getWeight() > 10;
    }

    public interface Predicate<T>{
        boolean testt(T t);
    }

    static List<Apple> filterApples(Predicate<Apple> p){
        List<Apple> result = new ArrayList<>();
        for (Apple apple : apples) {
            if (p.testt(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

}
