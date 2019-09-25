package com.dotwait.collection;

import org.junit.Test;

public class GenerateCoffee implements Generate<Coffee> {
    private Class[] types = {Cappuccino.class, Mocha.class};
    public Coffee getRandomCoffee(int num) throws IllegalAccessException, InstantiationException {
        return (Coffee) types[num & 1].newInstance();
    }
}
