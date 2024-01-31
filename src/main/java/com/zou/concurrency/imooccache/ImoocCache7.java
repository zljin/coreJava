package com.zou.concurrency.imooccache;

import com.zou.concurrency.imooccache.computable.Computable;
import com.zou.concurrency.imooccache.computable.ExpensiveFunction;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 描述：     利用Future，避免重复计算
 */
public class ImoocCache7<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public ImoocCache7(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws Exception {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> callable = new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<>(callable);
            f = ft;
            //by leonard 如果有两个同时计算666的线程，同时调用cache.get()方法
            //那么返回的结果都为null,后面还是会创建两个任务去计算相同的值
            //please refer to ImoocCache8.java
            cache.put(arg, ft);
            System.out.println("从FutureTask调用了计算函数");
            ft.run();
        }
        return f.get();
    }

    public static void main(String[] args) throws Exception {
        ImoocCache7<String, Integer> expensiveComputer = new ImoocCache7<>(
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
