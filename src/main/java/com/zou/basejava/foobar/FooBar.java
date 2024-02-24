package com.zou.basejava.foobar;

/**
 * https://leetcode.cn/problems/print-foobar-alternately/solutions/542996/duo-xian-cheng-liu-mai-shen-jian-ni-xue-d220n/
 */
public abstract class FooBar {

    protected int n;

    public FooBar(int n) {
        this.n = n;
    }

    public abstract void foo(Runnable printFoo) throws Exception;

    public abstract void bar(Runnable printBar) throws Exception;
}
