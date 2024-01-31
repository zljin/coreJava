package com.zou.concurrency.future;

import java.util.concurrent.*;

/**
 * 描述：演示get的超时方法，需要注意超时后处理，调用future.cancel()。
 * 演示cancel传入true和false的区别，代表是否中断正在执行的任务。
 *
 *
 *
 *
 */
public class Timeout {

    private static final Ad DEFAULT_AD = new Ad("无网络时候的默认广告");
    private static final ExecutorService exec = Executors.newFixedThreadPool(10);

    static class Ad {

        String name;

        public Ad(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }


    static class FetchAdTask implements Callable<Ad> {

        @Override
        public Ad call() throws Exception {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("sleep期间被中断了");
                return new Ad("被中断时候的默认广告");
            }
            return new Ad("旅游订票哪家强？找某程");
        }
    }


    public void printAd() {
        Future<Ad> f = exec.submit(new FetchAdTask());
        Ad ad;
        try {
            ad = f.get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            ad = new Ad("被中断时候的默认广告");
        } catch (ExecutionException e) {
            ad = new Ad("异常时候的默认广告");
        } catch (TimeoutException e) {
            ad = new Ad("超时时候的默认广告");
            System.out.println("超时，未获取到广告");

            /**
             * cancel取消任务的执行
             * 对于还没开始执行的任务，直接不会执行返回true
             * 如果任务已经完成或者已经取消,会执行失败返回false
             *
             * 如果已经开始执行了，在执行中间。则根据mayInterruptIfRunning的参数判断
             *
             * f.cancel(true)：适用于任务能够处理Interrupted的情况，一般推荐用这个
             *
             *
             * f.cancel(false): 适用1. 不清楚任务是否取消 2. 需要等待已经开始的任务执行完成 3. 不能处理Interrupted的情况
             *
             */

            boolean cancel = f.cancel(true);
            System.out.println("cancel的结果：" + cancel);
        }
        exec.shutdown();
        System.out.println(ad);
    }

    public static void main(String[] args) {
        Timeout timeout = new Timeout();
        timeout.printAd();
    }
}

