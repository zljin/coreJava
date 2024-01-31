package com.zou.concurrency.lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 描述：
 * main1()
 * 多个线程只申请读锁，都可以申请到
 * 只要有一个线程占用了写锁，其他线程申请写锁或者读锁必须等待前面写锁的释放
 *
 * main()
 * 读写锁的交互：读锁插队policy
 */
public class CinemaReadWrite {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放读锁");
            readLock.unlock();
        }
    }

    private static void write() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放写锁");
            writeLock.unlock();
        }
    }

    public static void main1(String[] args) {
        new Thread(() -> read(), "Thread1").start();
        new Thread(() -> read(), "Thread2").start();
        new Thread(() -> write(), "Thread3").start();
        new Thread(() -> write(), "Thread4").start();
    }

    /**
     * 非公平锁
     * 1. 写锁可随时插队，避免饥饿。
     * 2. 读锁仅在等待队列头节点不是想获取写锁线程的时候可以插队
     */
    public static void main(String[] args) {
        new Thread(() -> write(), "Thread1").start();
        new Thread(() -> read(), "Thread2").start();
        new Thread(() -> read(), "Thread3").start();
        new Thread(() -> write(), "Thread4").start();
        new Thread(() -> read(), "Thread5").start();
    }
}
