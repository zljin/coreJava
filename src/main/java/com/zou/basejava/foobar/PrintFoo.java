package com.zou.basejava.foobar;

public class PrintFoo implements Runnable {

    FooBar fooBar;

    public PrintFoo(FooBar fooBar) {
        this.fooBar = fooBar;
    }

    @Override
    public void run() {
        try {
            fooBar.foo(() -> System.out.print("foo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
