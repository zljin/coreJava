package com.zou.concurrency.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 加锁解决ThreadLocalNormalUsage002的问题
 */
public class ThreadLocalNormalUsage003 {

    public static final ExecutorService threalPool = Executors.newFixedThreadPool(10);

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threalPool.submit(() -> {
                //线程池只有10个，1个线程执行100条任务
                String date = new ThreadLocalNormalUsage003().date(finalI);
                System.out.println(date);
            });
        }
        threalPool.shutdown();
    }

    public String date(int seconds) {
        //参数的单位是毫秒，从1970.1.1 00：00：00 GMT开始计时
        Date date = new Date(1000 * seconds);
        String s;
        synchronized (ThreadLocalNormalUsage003.class) {
            s = simpleDateFormat.format(date);
        }
        return s;
    }
}
