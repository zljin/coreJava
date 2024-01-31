package com.zou.corejava.base.generic;

/**
 * 自定义泛型方法
 */
public class GenericMethod {

    public <S> S getParameter(String key, Class<S> type) {
        return null;
    }

    public <T> void show(T t) {
        System.out.println(t);
    }

    public static void main(String[] args) {
        // 实例化对象
        GenericMethod genericMethod = new GenericMethod();
        // 调用泛型方法show，传入不同类型的参数
        genericMethod.show("Java");
        genericMethod.show(222);
        genericMethod.show(222.0);
        genericMethod.show(222L);
    }
}
