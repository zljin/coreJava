package com.zou.basejava.foobar;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FooBarReentrantLock extends FooBar {

    Lock lock = new ReentrantLock(true);
    private final Condition foo = lock.newCondition();
    volatile boolean flag = true;

    public FooBarReentrantLock(int n) {
        super(n);
    }

    @Override
    public void foo(Runnable printFoo) {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (!flag) {
                    foo.await();
                }
                printFoo.run();
                flag = false;
                foo.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void bar(Runnable printBar) {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (flag) {
                    foo.await();
                }
                printBar.run();
                flag = true;
                foo.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

    public static void main(String[] args) {
        FooBar fooBar = new FooBarReentrantLock(10);
        new Thread(new PrintFoo(fooBar)).start();
        new Thread(new PrintBar(fooBar)).start();
    }
}
