package com.zou.corejava.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射：
 * 在运行时能够动态的获取类的信息和动态调用对象的方法和属性的能力
 * <p>
 * 由较好的灵活性使用于各种框架，但破坏了类的封装性，有性能损耗
 *
 * @author lingjin.zou
 * @Date 2022-02-06
 */
public class ReflectMainClass {

    public static void main(String[] args) throws Exception {
        reflectTest01();
    }

    /**
     * 反射的基础使用
     *
     * @throws Exception
     */
    public static void reflectTest01() throws Exception {
        //1. 获取类镜像
        Class<?> aClass = Class.forName("com.zou.pojo.Person");

        //2. 动态获取类构造器
        Constructor<?> constructor = aClass.getConstructor(String.class, int.class);

        //3. 通过类构造器创建对象
        Object obj = constructor.newInstance("leonard", 24);

        //4. 通过类镜像获取其成员变量
        Field name = aClass.getDeclaredField("name");
        name.setAccessible(true);//暴力访问
        name.set(obj, "kawhi");

        //5. 通过类镜像获取成员方法
        Method method = aClass.getMethod("say", String.class);
        method.invoke(obj, "hello");

        //6. 通过类镜像获取静态方法
        Method staticMethodSay = aClass.getDeclaredMethod("run", String.class);
        staticMethodSay.invoke(null, "lady");
    }
}
