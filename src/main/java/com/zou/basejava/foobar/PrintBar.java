package com.zou.basejava.foobar;

public class PrintBar implements Runnable {

    FooBar fooBar;

    public PrintBar(FooBar fooBar) {
        this.fooBar = fooBar;
    }

    @Override
    public void run() {
        try {
            fooBar.bar(() -> System.out.print("bar"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
