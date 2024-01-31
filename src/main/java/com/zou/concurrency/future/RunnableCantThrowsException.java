package com.zou.concurrency.future;

/**
 * Runnable的问题：
 *  1. 在run方法中无法抛出checked Exception
 *  2. 无返回值
 */
public class RunnableCantThrowsException {

    public void ddd() throws Exception {
        throw new Exception();
    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//下面这种直接报错，checked异常无法处理
//            @Override
//            public void run() throws Exception {
//                throw new Exception();
//            }
        };


    }
}
