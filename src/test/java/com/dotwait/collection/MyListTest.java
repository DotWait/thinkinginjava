package com.dotwait.collection;

import org.junit.Test;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyListTest {
    @Test
    public void myListTest(){
        int[][] arr = new int[4][3];
        System.out.println(arr.length);
        System.out.println(arr[0].length);
    }

    @Test
    public void test(){
        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(new Date(now));
        String date = sdf.format(new Date(now));
        System.out.println(date);
        long time = sdf.parse(date, new ParsePosition(0)).getTime();
        System.out.println(time);
        for (int i=0;i<35;i++){
            long l = time + i * 3600L * 1000;
            System.out.println(i+"\t"+zeroTime(l));
        }
    }

    private long zeroTime(long now){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(now));
        return sdf.parse(date, new ParsePosition(0)).getTime();
    }

    @Test
    public void arrTest(){
        int[] arr = new int[5];
        setArrValue(arr);
        System.out.println(arr[0]);
    }

    private void setArrValue(int[] arr){
        arr[0] = 1;
    }

    private static final long DAY_MS = 24L * 3600 * 1000;
    private static final long HOUR_MS = 3600L * 1000;

    @Test
    public void timeTest(){
        /**
         * System.currentTimeMillis()获取的时间戳时UTC时间
         * 这种方式获取的是东八区的0点时间戳，此时的UTC为16点
         */
        long now = System.currentTimeMillis();
        System.out.println(now);
        long remainHours = now % DAY_MS / HOUR_MS;
        System.out.println("remainHours = "+remainHours);
        long zeroTimestamp = now - (now + 8 * HOUR_MS) % DAY_MS;
        System.out.println(zeroTimestamp);
        remainHours = zeroTimestamp % DAY_MS / HOUR_MS;
        System.out.println("remainHours = "+remainHours);
    }
}
