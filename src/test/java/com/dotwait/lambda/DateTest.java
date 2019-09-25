package com.dotwait.lambda;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTest {
    @Test
    public void dateTest(){
        LocalDate date = LocalDate.of(2018,4,1);
        System.out.println(date.toString());
        LocalDate now = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        System.out.println(now);
        System.out.println(localTime);
    }
}
