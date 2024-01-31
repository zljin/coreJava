package com.zou.concurrency.future;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 描述：     演示批量提交任务时，用List来批量接收结果
 */
public class MultiFutures {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(20);
        ArrayList<Future> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Future<Integer> future = service.submit(new CallableTask());
            futures.add(future);
        }
        //这里休眠5秒后，下面的结果是几乎是一瞬间同时出来的
        //说明已经提前运算好了
        //而且开了20个核心线程，也正好只有20个线程执行，所以都是并行执行
        Thread.sleep(5000);
        for (int i = 0; i < 20; i++) {
            Future<Integer> future = futures.get(i);
            try {
                Integer integer = future.get();
                System.out.println(integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    static class CallableTask implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000);
            return new Random().nextInt();
        }
    }
}
