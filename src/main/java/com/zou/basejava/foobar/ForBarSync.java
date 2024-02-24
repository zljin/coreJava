package com.zou.basejava.foobar;

public class ForBarSync extends FooBar {

    Object object = new Object();
    volatile boolean flag = true;

    public ForBarSync(int n) {
        super(n);
    }

    @Override
    public void foo(Runnable printFoo) throws Exception {
        for (int i = 0; i < n; i++) {
            synchronized (object) {
                while (!flag) {
                    object.wait();
                }
                printFoo.run();
                flag = false;
                object.notify();
            }
        }
    }

    @Override
    public void bar(Runnable printBar) throws Exception {
        for (int i = 0; i < n; i++) {
            synchronized (object) {
                while (flag) {
                    object.wait();
                }
                printBar.run();
                flag = true;
                object.notify();
            }
        }
    }

    public static void main(String[] args) {
        FooBar fooBar = new ForBarSync(10);
        new Thread(new PrintFoo(fooBar)).start();
        new Thread(new PrintBar(fooBar)).start();
    }
}
