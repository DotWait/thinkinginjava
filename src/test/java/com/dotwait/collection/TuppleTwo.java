package com.dotwait.collection;

public class TuppleTwo<A, B> extends Tupple<A> {
    public final B b;
    public TuppleTwo(A a, B b) {
        super(a);
        this.b = b;
    }

    @Override
    public String toString() {
        return "TuppleTwo{" +
                "b=" + b +
                ", a=" + a +
                '}';
    }
}
