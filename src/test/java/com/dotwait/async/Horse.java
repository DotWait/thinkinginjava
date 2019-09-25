package com.dotwait.async;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Horse implements Runnable {
    private CyclicBarrier barrier;
    private Random random = new Random();

    public Horse(CyclicBarrier barrier){
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            for (int i=1;i<=2;i++){
                System.out.println(Thread.currentThread() + " start run");
                Thread.sleep(random.nextInt(5)*1000);
                System.out.println(Thread.currentThread() + "finish run");
                barrier.await();
                System.out.println(Thread.currentThread() + "第一轮结束");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
