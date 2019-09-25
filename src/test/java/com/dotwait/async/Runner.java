package com.dotwait.async;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Runner implements Runnable {
    private CountDownLatch startLatch;
    private CountDownLatch endLatch;
    private Random random = new Random();

    public Runner(CountDownLatch startLatch, CountDownLatch endLatch) {
        this.startLatch = startLatch;
        this.endLatch = endLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread() + "is ready");
            startLatch.await();
            System.out.println(Thread.currentThread() + "start run");
            process();
            endLatch.countDown();
            System.out.println(Thread.currentThread() + "finish run");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void process() {
        try {
            Thread.sleep(random.nextInt(5) * 1000 + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
