package com.zou.concurrency.lock.reentrantlock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述：
 */
public class PessimismOptimismLock {

    int a;

    //乐观锁
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();
    }

    //悲观锁
    public synchronized void testMethod() {
        a++;
    }


}
