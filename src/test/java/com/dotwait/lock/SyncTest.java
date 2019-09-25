package com.dotwait.lock;

import org.junit.Test;

import java.util.Arrays;

public class SyncTest {
    @Test
    public void test(){
        synchronized (this){
            System.out.println(1);
        }
    }
}
