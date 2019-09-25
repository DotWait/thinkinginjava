package com.dotwait.collection;

import org.junit.Test;

public class TuppleTest {
    @Test
    public void tuppleTest(){
        Tupple<Integer> t1 = new Tupple<>(1);
        System.out.println(t1);
        TuppleTwo<String, Integer> t2 = new TuppleTwo<>("1", 234);
        System.out.println(t2);
        
    }
}
