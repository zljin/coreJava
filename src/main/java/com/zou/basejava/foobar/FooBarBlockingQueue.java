package com.zou.basejava.foobar;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FooBarBlockingQueue extends FooBar {

    private BlockingQueue<Integer> bar = new LinkedBlockingQueue<>(1);
    private BlockingQueue<Integer> foo = new LinkedBlockingQueue<>(1);


    public FooBarBlockingQueue(int n) {
        super(n);
    }

    @Override
    public void foo(Runnable printFoo) throws Exception {
        for (int i = 0; i < n; i++) {
            foo.put(i);
            printFoo.run();
            bar.put(i);
        }
    }

    @Override
    public void bar(Runnable printBar) throws Exception {
        for (int i = 0; i < n; i++) {
            bar.take();
            printBar.run();
            foo.take();
        }
    }

    public static void main(String[] args) {
        FooBar fooBar = new FooBarBlockingQueue(10);
        new Thread(new PrintFoo(fooBar)).start();
        new Thread(new PrintBar(fooBar)).start();
    }
}
