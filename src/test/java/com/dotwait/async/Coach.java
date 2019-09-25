package com.dotwait.async;

import java.util.concurrent.CountDownLatch;

public class Coach implements Runnable {
    private CountDownLatch startLatch;
    private CountDownLatch endLatch;

    public Coach(CountDownLatch startLatch, CountDownLatch endLatch){
        this.startLatch = startLatch;
        this.endLatch = endLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread() + " all runner start run");
            startLatch.countDown();
            endLatch.await();
            System.out.println(Thread.currentThread() + " all runner finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
