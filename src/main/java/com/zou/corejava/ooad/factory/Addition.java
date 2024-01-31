package com.zou.corejava.ooad.factory;


public class Addition implements Operation {
    @Override
    public int apply(int a, int b) {
        return a + b;
    }
}
