package com.zou.basejava;

import com.zou.utils.ThreadPoolManager;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureMain {

    public static void main1(String[] args) {
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt();
            }
        };
        Future<Integer> future = ThreadPoolManager.THREAD_POOL_EXECUTOR.submit(task);
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ThreadPoolManager.THREAD_POOL_EXECUTOR.shutdown();
    }

    public static void main2(String[] args) {
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt();
            }
        };

        /**
         * FutureTask是一个类，实现了RunnableFuture接口，RunnableFuture接口继承自Future接口。
         * FutureTask同时具有Runnable和Future的能力，既可以作为Future得到Callable的返回值，又可以作为一个Runnable
         */
        FutureTask<Integer> integerFutureTask = new FutureTask<>(task);
        //new Thread(integerFutureTask).start();
        ThreadPoolManager.THREAD_POOL_EXECUTOR.submit(task);

        try {
            System.out.println("task运行结果：" + integerFutureTask.get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ThreadPoolManager.THREAD_POOL_EXECUTOR.shutdown();
    }

    public static void main(String[] args) {
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalArgumentException("Callable抛出异常");
            }
        };

        /**
         * 描述：     演示get方法过程中抛出异常，for循环为了演示抛出Exception的时机：
         * 并不是说一产生异常就抛出，直到我们get执行时，才会抛出。
         *
         * future能够抛出异常，runnable不会
         */
        Future<Integer> future = ThreadPoolManager.THREAD_POOL_EXECUTOR.submit(task);
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(i);
                Thread.sleep(500);
            }
            System.out.println(future.isDone());
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("InterruptedException异常");
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.out.println("ExecutionException异常");
        }
        ThreadPoolManager.THREAD_POOL_EXECUTOR.shutdown();
    }
}
