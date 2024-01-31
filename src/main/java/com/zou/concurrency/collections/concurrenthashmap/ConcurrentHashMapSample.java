package com.zou.concurrency.collections.concurrenthashmap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 组合操作并不保证线程安全
 * ConcurrentHashMap提供了两种组合的原子操作，比较常用
 * replace()
 * putIfAbsent()
 */
public class ConcurrentHashMapSample implements Runnable {

    private static Map<String, Integer> ch = new ConcurrentHashMap<>();
    private static Map<String, AtomicLong> ch1 = new ConcurrentHashMap<>();
    private static Map<String, Integer> ch2 = new HashMap<>();

    private static final List<String> contextStr = Arrays.asList(
            "aaaabbb"
            , "ddddpppp"
            , "kkkkhhhhhjjj"
    );

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ConcurrentHashMapSample());
        Thread t2 = new Thread(new ConcurrentHashMapSample());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        ch.forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println("======================================");
        ch1.forEach((k, v) -> System.out.println(k + ":" + v));

        System.out.println("======================================");
        ch2.forEach((k, v) -> System.out.println(k + ":" + v));
    }


    @Override
    public void run() {
        contextStr.stream().forEach(s -> {
            char[] charArray = s.toCharArray();
            for (char c : charArray) {
                String key = c + "";
                if (ch.containsKey(key)) {
                    while (true) {
                        Integer value = ch.get(key);
                        Integer newValue = value + 1;
                        boolean b = ch.replace(key, value, newValue);
                        if (b) {
                            break;
                        }
                    }
                    ch1.put(key, new AtomicLong(ch1.get(key).incrementAndGet()));
                    ch2.put(key, ch2.get(key)+1);
                } else {
                    ch1.put(key,new AtomicLong(1));
                    ch.put(key, 1);
                    ch2.put(key, 1);
                }
            }
        });
    }
}
