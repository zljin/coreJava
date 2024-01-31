package com.zou.concurrency.threadlocal;

/**
 * 描述： 演示ThreadLocal用法2： 避免参数传递的麻烦
 *
 * 在不影响性能和线程安全的情况下，无需层层传递参数，
 * 就可以保证当前线程获取对应的用户信息目的
 *
 */

/**
 * ThreadLocal总结：
 *  1. 让某个需要用到的对象在线程间隔离（每个线程都有自己独立的对象）
 *  2. 在任何方法中都可以轻松获取到该对象
 *
 *
 *  根据共享对象和生成时机的不同，选择initialValue或set来保存对象
 *
 *  initialValue可以将第一次get的时候将对象初始化出来，对象的、
 *  初始化时机可以由我们控制
 *
 *
 *  set:存储对象不由我们直接控制，必须要前面的步骤生成好才行
 *
 *
 *  四点好处：
 *  1. 达到线程安全
 *  2. 不需要加锁
 *  3. 更高效地利用内存，节省开销：相比与每个线程新建一个SimpleDateFormat对象
 *  ，显然用ThreadLocal可以节省内存和开销
 *  4. 避免传参
 */
public class ThreadLocalNormalUsage100 {

    public static void main(String[] args) {
        new ServiceA().process();
    }

}

/*
mock生成用户对象
 */
class ServiceA {

    public void process() {
        User user = new User("leonard");
        UserContextHolder.holder.set(user);
        new ServiceB().process();
    }

}

class ServiceB {

    public void process() {
        User user = UserContextHolder.holder.get();
        System.out.println("ServiceB 拿到用户名"+user.name);
        new ServiceC().process();
    }
}

class ServiceC {

    public void process() {
        User user = UserContextHolder.holder.get();
        System.out.println("ServiceC 拿到用户名"+user.name);
        //help to gc
        UserContextHolder.holder.remove();
    }

}

class UserContextHolder {
    public static ThreadLocal<User> holder = new ThreadLocal<>();
}


class User {
    String name;

    public User(String name) {
        this.name = name;
    }

    public User() {
    }
}