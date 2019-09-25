package com.dotwait.async;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 闭锁是一种同步工具类，可以延迟线程的进度直到其到达终止状态。
 * CountDownLatch是一种灵活的闭锁实现，它可以使一个或者多个线程等待一组事件的发生。
 * 闭锁状态包含一个计数器，该计数器被初始化为一个正数，表示需要等待的事件数量。countDown方法递减计数器，表示已经有一个事件已经发生了。
 * 而await方法等待计数器达到0，这表示所有需要等待的事件都已经发生。如果计数器的值非0，那么await会一直阻塞直到计数器为0，或者等待中的线程中断或者超时。
 *
 */
public class RunningGame {
    private static final int SIZE = 10;

    @Test
    public void game() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(SIZE);
        for (int i=0;i<SIZE;i++){
            new Thread(new Runner(startLatch, endLatch)).start();
        }

        Thread.sleep(2000);
        System.out.println("go");
        new Thread(new Coach(startLatch, endLatch)).start();

        while (true){
            System.out.println("startLatch:"+startLatch.getCount());
            System.out.println("endLatch:"+endLatch.getCount());
            Thread.sleep(1000);
        }
    }
}
