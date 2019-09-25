package com.dotwait.collection;

public class TuppleThree<A, B, C> extends TuppleTwo<A, B> {
    final C c;
    public TuppleThree(A a,B b,C c) {
        super(a, b);
        this.c = c;
    }

    @Override
    public String toString() {
        return "TuppleThree{" +
                "c=" + c +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
