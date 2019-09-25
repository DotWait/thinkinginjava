package com.dotwait.collection;

public class Tupple<A> {
    public final A a;
    public Tupple(A a){
        this.a = a;
    }

    @Override
    public String toString() {
        return "Tupple{" +
                "a=" + a +
                '}';
    }

}
