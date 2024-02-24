package com.zou.basejava.foobar;

public class FooBarSpin extends FooBar {

    volatile boolean flag = true;

    public FooBarSpin(int n) {
        super(n);
    }

    @Override
    public void foo(Runnable printFoo) throws Exception {
        for (int i = 0; i < n; i++) {
            if (flag) {
                printFoo.run();
                i++;
                flag = false;
            } else {
                Thread.yield();
            }


        }
    }

    @Override
    public void bar(Runnable printBar) throws Exception {
        for (int i = 0; i < n; ) {
            if (!flag) {
                printBar.run();
                i++;
                flag = true;
            } else {
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) {
        FooBar fooBar = new FooBarSpin(10);
        new Thread(new PrintFoo(fooBar)).start();
        new Thread(new PrintBar(fooBar)).start();
    }
}
