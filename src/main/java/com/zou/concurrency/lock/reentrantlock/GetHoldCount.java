package com.zou.concurrency.lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：     可重入锁：同一个线程可以多次获取同一把锁，而不是必须先解锁再加锁
 *
 * 可以避免死锁：
 *  比如一个线程有两个方法加了锁，如果不能重入
 *  则线程跑第一个方法的时候，第二个方法就跑不了了，因为必须释放
 *
 *  ReentrantLock就是可重入的锁
 *
 */
public class GetHoldCount {
    private  static ReentrantLock lock =  new ReentrantLock();

    public static void main(String[] args) {
        System.out.println(lock.getHoldCount());
        lock.lock();
        System.out.println(lock.getHoldCount());
        lock.lock();
        System.out.println(lock.getHoldCount());
        //连续获取三把相同的锁，根本不需要解锁再加锁，这就是可重入
        lock.lock();
        System.out.println(lock.getHoldCount());
        lock.unlock();
        System.out.println(lock.getHoldCount());
        lock.unlock();
        System.out.println(lock.getHoldCount());
        lock.unlock();
        System.out.println(lock.getHoldCount());
    }
}
