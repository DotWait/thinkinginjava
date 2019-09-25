package com.dotwait.lambdatest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UserTest {
    public void test(){
        UserAction userAction = new UserAction();
        List<User> userList = new ArrayList<>();
        List<User> filter = userAction.filter(userList, user -> user.getName().length() > 2);
        Function<User, String> getName = User::getName;
        BiFunction<String, Integer, String> stringIntegerStringBiFunction = String::substring;
        String apply = stringIntegerStringBiFunction.apply("123", 1);
        System.out.println(apply);
    }


}
