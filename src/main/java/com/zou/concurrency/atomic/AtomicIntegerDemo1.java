package com.zou.concurrency.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * 基本类型原子类
 *  演示AtomicInteger的基本用法，对比非原子类的线程安全问题，使用了原子类之后，不需要加锁，也可以保证线程安全。
 */
public class AtomicIntegerDemo1 implements Runnable {

    private static final AtomicInteger atomicInteger = new AtomicInteger();


    //CAS乐观锁策略，保证数据累加的原子性
    public void incrementAtomic() {
        atomicInteger.getAndAdd(1);
    }


    //volatile:设置变量的可见性，线程中的变量直接从内存拿，不经过缓存（并不保证原子性）
    private static volatile int basicCount = 0;


    //通过给方法加锁保证累加的原子性，采用悲观锁策略
    public synchronized void incrementBasic() {
        basicCount++;
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerDemo1 r = new AtomicIntegerDemo1();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();

        //join()等待t1和t2线程都执行完毕后，再执行下面的主线程，主线程会一直等
        t1.join();
        t2.join();
        System.out.println("原子类的结果：" + atomicInteger.get());
        System.out.println("普通变量的结果：" + basicCount);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            incrementAtomic();
            incrementBasic();
        }
    }
}
