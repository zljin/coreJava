package com.zou.concurrency.imooccache;

import com.zou.concurrency.imooccache.computable.Computable;
import com.zou.concurrency.imooccache.computable.ExpensiveFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：     缩小了synchronized的粒度，提高性能，但是依然并发不安全
 */
public class ImoocCache6<A, V> implements Computable<A, V> {

    private final Map<A, V> cache = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public ImoocCache6(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws Exception {
        System.out.println("进入缓存机制");
        V result = cache.get(arg);
        if (result == null) {
            //by leonard 这部如果计算时间过长，那么第二个线程过来就会重复计算
            //如果重复计算，则缓存的意义就不大了
            //可以将计算过程丢到Future里面就好
            //please refer to ImoocCache7.java
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache6<String, Integer> expensiveComputer = new ImoocCache6<>(
                new ExpensiveFunction());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = expensiveComputer.compute("666");
                    System.out.println("第一次的计算结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = expensiveComputer.compute("666");
                    System.out.println("第三次的计算结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = expensiveComputer.compute("667");
                    System.out.println("第二次的计算结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
