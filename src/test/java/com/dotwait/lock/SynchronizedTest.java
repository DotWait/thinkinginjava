package com.dotwait.lock;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 锁机制有两个特性：
 * 互斥性（原子性）：同一时间只能有一个线程持有锁
 * 可见性：在释放锁之前必须保证对共享变量的修改在之后获取锁的线程是可见的
 *
 * 对象锁：每个对象都会有一个 monitor 对象，这个对象其实就是 Java 对象的锁，通常会被称为“内置锁”或“对象锁”。类的对象可以有多个，所以每个对象有其独立的对象锁，互不干扰。
 * 类锁：类锁实际上是通过对象锁实现的，即类的 Class 对象锁。每个类只有一个 Class 对象，所以每个类只有一个类锁。
 *
 */
public class SynchronizedTest {

    class SyncThread implements Runnable {

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            if (threadName.startsWith("A")) {
                async();
            } else if (threadName.startsWith("B")) {
                sync1();
            } else if (threadName.startsWith("C")) {
                sync2();
            }
        }

        /**
         * 异步方法
         */
        private void async() {
            try {
                System.out.println(Thread.currentThread().getName() + "_Async_Start: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "_Async_End: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 方法中有 synchronized(this|object) {} 同步代码块
         */
        private void sync1() {
            System.out.println(Thread.currentThread().getName() + "_Sync1: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            synchronized (this) {
                try {
                    System.out.println(Thread.currentThread().getName() + "_Sync1_Start: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + "_Sync1_End: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * synchronized 修饰非静态方法
         */
        private synchronized void sync2() {
            System.out.println(Thread.currentThread().getName() + "_Sync2: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            try {
                System.out.println(Thread.currentThread().getName() + "_Sync2_Start: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "_Sync2_End: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*sync2和sync1获取的是同一个锁，即该类的对象的对象锁*/
    @Test
    public void test() throws InterruptedException {
        SyncThread syncThread = new SyncThread();
        Thread A_thread1 = new Thread(syncThread, "A_thread1");
        Thread A_thread2 = new Thread(syncThread, "A_thread2");
        Thread B_thread1 = new Thread(syncThread, "B_thread1");
        Thread B_thread2 = new Thread(syncThread, "B_thread2");
        Thread C_thread1 = new Thread(syncThread, "C_thread1");
        Thread C_thread2 = new Thread(syncThread, "C_thread2");
        A_thread1.start();
        A_thread2.start();
        B_thread1.start();
        B_thread2.start();
        C_thread1.start();
        C_thread2.start();
        Thread.sleep(20000);
    }

    /*每个线程获取各自的对象锁*/
    @Test
    public void test2() throws InterruptedException {
        Thread A_thread1 = new Thread(new SyncThread(), "A_thread1");
        Thread A_thread2 = new Thread(new SyncThread(), "A_thread2");
        Thread B_thread1 = new Thread(new SyncThread(), "B_thread1");
        Thread B_thread2 = new Thread(new SyncThread(), "B_thread2");
        Thread C_thread1 = new Thread(new SyncThread(), "C_thread1");
        Thread C_thread2 = new Thread(new SyncThread(), "C_thread2");
        A_thread1.start();
        A_thread2.start();
        B_thread1.start();
        B_thread2.start();
        C_thread1.start();
        C_thread2.start();
        Thread.sleep(20000);
    }
}
