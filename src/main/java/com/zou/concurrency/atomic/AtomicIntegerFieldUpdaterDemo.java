package com.zou.concurrency.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 描述：
 *  演示AtomicIntegerFieldUpdater的用法
 *  适合将复杂对象进行原子化，并且更适用于更新频率小的场景
 *  如每天只有一次会更新的频率
 *
 *  注意点：
 *      1. 底层原理依赖与反射，对于不可见的对象如private修饰，效果不行
 *      2，不能时static修饰的变量
 *      3.
 *
 */
public class AtomicIntegerFieldUpdaterDemo implements Runnable{

    static Candidate tom;
    static Candidate peter;

    public static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater
            .newUpdater(Candidate.class, "score");

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            peter.score++;
            scoreUpdater.getAndIncrement(tom);
        }
    }

    public static class Candidate {

        volatile int score;
    }

    public static void main(String[] args) throws InterruptedException {
        tom=new Candidate();
        peter=new Candidate();
        AtomicIntegerFieldUpdaterDemo r = new AtomicIntegerFieldUpdaterDemo();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("普通变量："+peter.score);
        System.out.println("升级后的结果"+ tom.score);
    }
}
