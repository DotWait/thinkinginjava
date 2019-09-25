package com.dotwait.lock;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁
 * 支持公平锁和非公平锁
 * 公平锁：FIFO
 * 非公平锁：
 */
public class ReentrantLockTest {
    private ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    class Add implements Runnable{

        /*@Override
        public void run() {
            for (int i=0;i<100000;i++){
                try{
                    lock.lock();
                    System.out.println(Thread.currentThread() + " : count = "+count);
                    count++;
                }finally {
                    lock.unlock();
                }
            }
        }*/

        /*count会到达1001
        @Override
        public void run() {
            for (int i=0;i<100000;i++){
                int local = count;
                if (local < 1000){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
                System.out.println(Thread.currentThread() + " : count = "+count);
            }
        }*/

        /*count到达1000*/
        @Override
        public void run() {
            for (int i=0;i<100000;i++){
                try{
                    lock.lock();
                    int local = count;
                    if (local < 1000){
                        Thread.sleep(10);
                        count++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
                System.out.println(Thread.currentThread() + " : count = "+count);
            }
        }
    }

    @Test
    public void addTest() throws InterruptedException {
        Thread t1 = new Thread(new Add());
        Thread t2 = new Thread(new Add());
        Thread t3 = new Thread(new Add());
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(20000);
    }
}
