package com.zou.concurrency.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * hreadLocalNormalUsage003的synchronized是同步方法，
 * 会有严重的性能问题
 * <p>
 * 此时我们可以用ThreadLocal存放simpleDateFormat对象
 * ThreadLocal会给每个线程单处创建一份空间，分配自己的df对象，彼此隔离，不会有影响
 */
public class ThreadLocalNormalUsage004 {

    public static final ExecutorService threalPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threalPool.submit(() -> {
                //线程池只有10个，1个线程执行100条任务
                String date = new ThreadLocalNormalUsage004().date(finalI);
                System.out.println(date);
            });
        }
        threalPool.shutdown();
    }

    public String date(int seconds) {
        //参数的单位是毫秒，从1970.1.1 00：00：00 GMT开始计时
        Date date = new Date(1000 * seconds);
        return ThreadSafeFormatter.dateFormatThreadLocal.get().format(date);
    }
}

class ThreadSafeFormatter {
    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal
            = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
    };
}
