package com.zou.basejava.foobar;

import java.util.concurrent.CyclicBarrier;

/**
 * 循环屏障,允许一组线程相互等待,直到所有线程到达一个公共的屏障点,才继续往下走，可循环使用
 * 班车只有等人都到齐了才能开
 */
public class ForBarCyclicBarrier extends FooBar {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.print("-"));
    volatile boolean flag = true;


    public ForBarCyclicBarrier(int n) {
        super(n);
    }

    @Override
    public void foo(Runnable printFoo) throws Exception {
        for (int i = 0; i < n; i++) {
            while (!flag) ;
            printFoo.run();
            flag = false;
            cyclicBarrier.await();
        }
    }

    @Override
    public void bar(Runnable printBar) throws Exception {
        for (int i = 0; i < n; i++) {
            cyclicBarrier.await();
            printBar.run();
            flag = true;
        }
    }

    public static void main(String[] args) {
        FooBar fooBar = new ForBarCyclicBarrier(10);
        new Thread(new PrintFoo(fooBar)).start();
        new Thread(new PrintBar(fooBar)).start();
    }
}
