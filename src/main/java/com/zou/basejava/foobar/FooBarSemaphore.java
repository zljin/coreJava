package com.zou.basejava.foobar;

import java.util.concurrent.Semaphore;

/**
 *  Semaphore 经常用于限制同一时刻获取某种资源的线程数量，最为典型的就是做流量控制
 *  高铁闸机
 */
public class FooBarSemaphore extends FooBar {

    //permits:1 第一个调用acquire就会成功,其余将阻塞除非释放,直到第一个释放
    private Semaphore foo = new Semaphore(1);
    //permits:0 只要调用就阻塞,直到你release
    private Semaphore bar = new Semaphore(0);


    public FooBarSemaphore(int n) {
        super(n);
    }

    @Override
    public void foo(Runnable printFoo) throws Exception {
        for (int i = 0; i < n; i++) {
            foo.acquire();
            printFoo.run();
            bar.release();
        }
    }

    @Override
    public void bar(Runnable printBar) throws Exception {
        for (int i = 0; i < n; i++) {
            bar.acquire();
            printBar.run();
            foo.release();
        }
    }

    public static void main(String[] args) {
        FooBar fooBar = new FooBarSemaphore(10);
        new Thread(new PrintFoo(fooBar)).start();
        new Thread(new PrintBar(fooBar)).start();
    }
}
