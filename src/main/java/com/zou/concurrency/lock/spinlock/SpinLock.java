package com.zou.concurrency.lock.spinlock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 描述：     开发一个自己的自旋锁
 *
 */
public class SpinLock {

    //开发前提要求：必须拥有一个原子操作，CAS
    //AtomicReference：给此类有原子的能力
    private AtomicReference<Thread> sign = new AtomicReference<>();

    public void lock() {
        Thread current = Thread.currentThread();
        //expect:null 代表我希望当前没有任何线程有持有
        //update:current 代表若当前没有其他线程持有，则将当前的只修改为当前线程的持有
        while (!sign.compareAndSet(null, current)) {//死循环，知道成功为止
            System.out.println("自旋获取失败，再次尝试");
        }
    }

    public void unlock() {
        Thread current = Thread.currentThread();

        //直接将update置为null即可，原来的expect当然是刚刚set进去的current线程引用
        sign.compareAndSet(current, null);
    }

    public static void main(String[] args) {
        SpinLock spinLock = new SpinLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始尝试获取自旋锁");
                spinLock.lock();
                System.out.println(Thread.currentThread().getName() + "获取到了自旋锁");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    spinLock.unlock();
                    System.out.println(Thread.currentThread().getName() + "释放了自旋锁");
                }
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }
}
