package com.dotwait.parameter;

public class AppleGreenColorPredicate implements ApplePredicate<Apple> {

    @Override
    public boolean test(Apple apple) {
        return "green".equals(apple.getColor());
    }
}
