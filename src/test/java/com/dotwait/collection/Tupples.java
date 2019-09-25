package com.dotwait.collection;

public class Tupples {
    public static <A, B> TuppleTwo<A, B> tupple(A a, B b) {
        return new TuppleTwo<>(a, b);
    }

    public static <A, B, C> TuppleThree<A, B, C> tupple(A a, B b, C c) {
        return new TuppleThree<>(a, b, c);
    }
}
