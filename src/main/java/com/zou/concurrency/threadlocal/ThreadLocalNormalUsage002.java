package com.zou.concurrency.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1000个线程打印日期任务，用线程池来执行,效率很快
 */
public class ThreadLocalNormalUsage002 {

    public static final ExecutorService threalPool = Executors.newFixedThreadPool(10);

    /**
     * 相比与ThreadLocalNormalUsage001,虽然减少了每次创建的对象，但是
     * 会有线程问题，出现重复的日期
     *
     * 当很多线程共用一个线程对象simpleDateFormat时发生了线程安全问题
     */
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threalPool.submit(() -> {
                //线程池只有10个，1个线程执行100条任务
                String date = new ThreadLocalNormalUsage002().date(finalI);
                System.out.println(date);
            });
        }
        threalPool.shutdown();
    }

    public String date(int seconds) {
        //参数的单位是毫秒，从1970.1.1 00：00：00 GMT开始计时
        Date date = new Date(1000 * seconds);
        return simpleDateFormat.format(date);
    }
}
