package com.dotwait.async;

import org.junit.Test;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier适用于这样的情况：你希望创建一组任务，它们并行地执行工作，然后在下一个步骤之前等待，直到所有任务都完成。
 * 栅栏和闭锁的关键区别在于，所有线程必须同时到达栅栏位置，才能继续执行。
 * 闭锁用于等待事件，而栅栏是线程之间彼此等待，等到都到的时候再决定做下一件事
 *
 */
public class HorseRace {
    @Test
    public void horseRace() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5);
        for (int i=0;i<5;i++){
            new Thread(new Horse(barrier)).start();
        }

        while (true){
            System.out.println(barrier.getNumberWaiting());
            Thread.sleep(1000);
        }
    }
}
