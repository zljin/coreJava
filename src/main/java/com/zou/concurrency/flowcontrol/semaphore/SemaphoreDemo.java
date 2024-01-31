package com.zou.concurrency.flowcontrol.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 描述：     演示Semaphore用法
 * 其中acquire(3)可以设置权重，比如我一个线程需要拿3个信号量，如果构造函数只有3，
 * 那就别人拿不到，当然释放的时候也要为3
 *
 * 获取和释放一定要一致，不然不够用
 */
public class SemaphoreDemo {

    //一般设置公平性要设置为true更为合理，信号量适合执行比较慢的任务
    static Semaphore semaphore = new Semaphore(3, true);

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 100; i++) {
            service.submit(new Task());
        }
        service.shutdown();
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            try {
                semaphore.acquire(3);
                //semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "拿到了许可证");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "释放了许可证");
            //semaphore.release();
            semaphore.release(3);
        }
    }
}
