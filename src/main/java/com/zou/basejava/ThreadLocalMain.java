package com.zou.basejava;

import com.zou.utils.Convert;
import com.zou.utils.ThreadPoolManager;

import java.util.Date;

public class ThreadLocalMain {

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            ThreadPoolManager.THREAD_POOL_EXECUTOR.submit(() -> {
                String date = new ThreadLocalMain().date(finalI);
                System.out.println(date);
            });
        }
        ThreadPoolManager.THREAD_POOL_EXECUTOR.shutdown();
    }

    public String date(int seconds) {
        Date date = new Date(1000 * seconds);
        return Convert.dateFormatThreadLocal.get().format(date);
    }
}
