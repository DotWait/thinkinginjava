package com.dotwait.lambdatest;

import java.util.ArrayList;
import java.util.List;

public class UserAction {
    public List<User> filterUser(List<User> users){
        List<User> result = new ArrayList<>();
        for (User user : users){
            if (user.getAge() > 10){
                result.add(user);
            }
        }
        return result;
    }

    public List<User> filterUserByName(List<User> users){
        List<User> result = new ArrayList<>();
        for (User user : users){
            if (user.getName().length() > 3){
                result.add(user);
            }
        }
        return result;
    }

    public <T> List<T> filter(List<T> ts, UserPredicate<T> ut){
        List<T> result = new ArrayList<>();
        for (T t : ts){
            if (ut.userTest(t)){
                result.add(t);
            }
        }
        return result;
    }
}
