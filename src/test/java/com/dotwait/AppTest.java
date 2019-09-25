package com.dotwait;

import static org.junit.Assert.assertTrue;

import com.dotwait.collection.Coffee;
import com.dotwait.collection.GenerateCoffee;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws InstantiationException, IllegalAccessException {
        GenerateCoffee generateCoffee = new GenerateCoffee();
        Coffee coffee = generateCoffee.getRandomCoffee(0);
        System.out.println(coffee);
        coffee.show(1);
        coffee.show("1");
        coffee.show(1L);
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @Test
    public void stringTest(){

    }
}
