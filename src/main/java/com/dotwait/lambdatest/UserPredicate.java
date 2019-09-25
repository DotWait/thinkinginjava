package com.dotwait.lambdatest;

@FunctionalInterface
public interface UserPredicate<T> {
    boolean userTest(T t);
}
