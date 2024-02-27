package com.zou.basejava.foobar;

import com.zou.utils.ThreadPoolManager;

import java.util.concurrent.CountDownLatch;

public class FooBarCountdownLatch {
    public static void main(String[] args) throws InterruptedException {
        final int totalThread = 10;
        CountDownLatch countDownLatch = new CountDownLatch(totalThread);
        for (int i = 0; i < totalThread; i++) {
            ThreadPoolManager.THREAD_POOL_EXECUTOR.execute(() -> {
                System.out.print("foobar");
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("end");
        ThreadPoolManager.THREAD_POOL_EXECUTOR.shutdown();
    }
}
