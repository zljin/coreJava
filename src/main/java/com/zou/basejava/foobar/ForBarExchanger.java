package com.zou.basejava.foobar;


import java.util.concurrent.Exchanger;

/**
 * 供了两个线程在某个时间点彼此交换信息的功能。
 * 使用 Exchanger 的重点
 * 1. 一对线程使用 同一个Exchanger 对象 和 exchange () 方法，
 * 2. 当一对线程都到达了同步点时，彼此会进行信息交换
 */
public class ForBarExchanger {

    private static Exchanger<Object> exchanger = new Exchanger();

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                Object foo = exchanger.exchange("foo");
                System.out.println(Thread.currentThread().getName() + "收到" + foo);
            } catch (Exception e) {
            }
        }, "快递员").start();

        new Thread(() -> {
            try {
                Object bar = exchanger.exchange("bar");
                System.out.println(Thread.currentThread().getName() + "收到" + bar);

            } catch (InterruptedException e) {
            }
        }, "客户").start();

    }
}
