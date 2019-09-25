package com.dotwait.lock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * volatile 关键字在多线程的情况下，保证共享变量对所有线程可见
 * 在不加volatile的情况下，目前有两种情况会使得线程获取主内存的共享变量值
 * （1）Thread.sleep(1）；线程处于空闲
 * （2）类似System.out.print();等包含了同步代码块，线程在进入和离开同步代码块会将工作内存中的变量值与主内存中的共享变量值进行对比
 *
 * 从只在ChangeListener中增加代码，从而可以获取到最新值可以看出，写操作会及时更新到主内存，读操作只会读取线程工作内存的值
 */
public class VariableTest {
    private static final Logger LOGGER = Logger.getLogger("VariableTest");

//    private static volatile int MY_INT = 0;

    private static int MY_INT = 0;

    public static void main(String[] args) {
        new ChangeListener().start();
        new ChangeMaker().start();
    }

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            int local_value = MY_INT;
            while ( local_value < 5){
                if( local_value!= MY_INT){
                    LOGGER.log(Level.INFO,"Got Change for MY_INT : {0}", MY_INT);
                    local_value= MY_INT;
                }
                /*try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
//                System.out.print("wait");
//                process();
            }
        }

        private void process(){
            int a = 1;
            int b = 2;
            int c = 0;
            for (int i=0;i<1000;i++){
                c = i;
            }
            a = c;
        }
    }

    static class ChangeMaker extends Thread{
        @Override
        public void run() {

            int local_value = MY_INT;
            while (MY_INT <5){
                LOGGER.log(Level.INFO, "Incrementing MY_INT to {0}", local_value+1);
                MY_INT = ++local_value;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
}