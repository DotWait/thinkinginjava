package com.dotwait.async;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Task implements Runnable {
    private Semaphore semaphore;
    private Random random = new Random();

    public Task(Semaphore semaphore){
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread() + "start task");
            process();
            semaphore.release();
            System.out.println(Thread.currentThread() + "finish task");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void process(){
        try {
            Thread.sleep(random.nextInt(5)*1000+1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
