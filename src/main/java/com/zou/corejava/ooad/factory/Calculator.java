package com.zou.corejava.ooad.factory;

/**
 * if else reconstr way
 */
public class Calculator {
    public int calculateUsingFactory(int a, int b, String operator) {
        Operation targetOperation = OperatorFactory
                .getOperation(operator)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Operator"));
        return targetOperation.apply(a, b);
    }

    public static void main(String[] args) {
        int addresult = new Calculator().calculateUsingFactory(
                1, 2, "add"
        );
        System.out.println(addresult);
    }
}
