package com.dotwait.async;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * 多线程异步执行，semaphore可保证指定数量的线程获得锁
 *
 * 计数信号量用来控制同时访问某个特定资源的操作数量，或者同时执行某个指定操作的数量。信号量还可以用来实现某种资源池，或者对容器施加边界。
 * Semaphore管理着一组许可（permit）,许可的初始数量可以通过构造函数设定，操作时首先要获取到许可，才能进行操作，操作完成后需要释放许可。
 * 如果没有获取许可，则阻塞到有许可被释放。如果初始化了一个许可为1的Semaphore，那么就相当于一个不可重入的互斥锁（Mutex）
 *
 */
public class Action {
    @Test
    public void action() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        for (int i=0;i<10;i++){
            new Thread(new Task(semaphore)).start();
        }
        while (true){
            System.out.println("队列长度:"+semaphore.getQueueLength());
            Thread.sleep(1000);
        }
    }
}
