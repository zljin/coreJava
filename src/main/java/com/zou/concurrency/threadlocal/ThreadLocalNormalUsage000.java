package com.zou.concurrency.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 两个线程打印日期
 */
public class ThreadLocalNormalUsage000 {

    public static void main(String[] args) {
        new Thread(()->{
            String date = new ThreadLocalNormalUsage000().date(10);
            System.out.println(date);
        }).start();

        new Thread(()->{
            String date = new ThreadLocalNormalUsage000().date(1006);
            System.out.println(date);
        }).start();
    }

    public String date(int seconds){
        //参数的单位是毫秒，从1970.1.1 00：00：00 GMT开始计时
        Date date = new Date(1000 * seconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
}
