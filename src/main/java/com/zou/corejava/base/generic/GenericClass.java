package com.zou.corejava.base.generic;

/**
 * 自定义泛型类
 * @param <T>
 *     http://www.imooc.com/wiki/javalesson/javageneric.html
 */
public class GenericClass<T> {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static void main(String[] args) {
        GenericClass<String> stringGenericClass = new GenericClass<>();
        stringGenericClass.setValue("hello");
        System.out.println(stringGenericClass.getValue());
    }
}
