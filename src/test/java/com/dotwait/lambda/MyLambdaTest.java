package com.dotwait.lambda;

import com.dotwait.lambdatest.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MyLambdaTest {
    @Test
    public void test() {
        MyLambda.init();
//        List<Apple> apples = MyLambda.filterApples(MyLambda::isGreenApple);
        List<Apple> apples = MyLambda.filterApples((Apple a) -> 1 == a.getColor());
        System.out.println("=========Green Apple==========");
        apples.forEach(System.out::println);
        List<Apple> apples1 = MyLambda.filterApples(MyLambda::isHeavyApple);
        List<Apple> apples2 = MyLambda.filterApples((Apple a) -> a.getWeight() > 10);
        System.out.println("===========Heavy Apple=========");
        apples2.forEach(System.out::println);
        List<Apple> apples3 = MyLambda.getApples();
        List<Apple> collect = apples3.stream().filter((Apple a) -> a.getColor() == 1).collect(toList());
        System.out.println("=========Green Apple==========");
        collect.forEach(System.out::println);
    }

    @Test
    public void streamTest() {
        List<User> list = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            User user = new User();
            user.setAge(i);
            user.setId(i + 100);
            user.setName("user" + i);
            list.add(user);
        }
        List<User> collect = list.stream().filter(user -> user.getAge() > 15).limit(5).collect(toList());
        collect.forEach(System.out::println);
        Optional<Integer> reduce = list.stream().map(User::getAge).reduce(Integer::max);
        System.out.println(reduce.get());
        int sum = list.stream().mapToInt(User::getAge).sum();
        System.out.println(sum);
        Integer[] numbers = {2, 3, 4, 2, 2, 5, 6};
        int sum1 = Arrays.stream(numbers).mapToInt(n -> n).sum();
        int[] numberArr = {1, 2, 2, 2, 2};
        int sum2 = Arrays.stream(numberArr).sum();

    }

    @Test
    public void stream() {
        List<User> list = new ArrayList<>(10000000);
        for (int i = 0; i < 10000000; i++) {
            User user = new User();
            user.setAge(i);
            user.setId(i);
            user.setName("user" + i);
            list.add(user);
        }
        List<User> result = new ArrayList<>(10000);
        long start = System.currentTimeMillis();
        for (User user : list) {
            if (user.getAge() > 10000 && user.getAge() < 20000){
                result.add(user);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start + "ms");

//        long start = System.currentTimeMillis();
//        List<User> collect = list.stream().filter(user -> user.getAge() > 10000 && user.getAge() < 20000).collect(toList());
//        long end = System.currentTimeMillis();
//        System.out.println(end-start + "ms");
    }
}
