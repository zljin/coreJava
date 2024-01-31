---
title: threadlocal
date: 2023-03-07 7:22:45
tags: Java语言
categories: 并发编程工具
---


> 线程局部变量：为每一个线程提供一个本地变量(实例副本),专属于这个线程,通过这种方式,避免资源在多线程间共享


## 常用的应用场景：

1. 每个线程都需要独享一个对象，比如一些工具类（如SimpleDateFormat很都线程都要使用），不能共享
2. 每个线程都需要保存的全局变量（如拦截器中获取用户信息）,可以让不同的方法直接使用，避免参数传递的麻烦。

ThreadLocal可以在不影响性能的情况下，保存当前线程对应的用户信息从而减少参数传递。
强调的是同一个请求（同一个线程内）不同方法间的共享
注意每次HTTP请求都对应一个线程，线程之间相互隔离，这就是ThreadLocal的典型应用

spring中的sample实例：
org.springframework.format.datetime.standard.DateTimeContextHolder
org.springframework.web.context.request.RequestContextHolder

代码sample如下：https://github.com/zljin/coreJava/tree/master/src/main/java/com/zou/concurrency/threadlocal



## ThreadLocal原理分析

> 每个ThreadLocal都会持有一个ThreadLocalMap的成员变量，里面有entry数组存放值

![](https://github.com/zljin/hexo/blob/master/image_address/threadlocal1.png?raw=true)

```java
static class ThreadLocalMap {
        static class Entry extends WeakReference<ThreadLocal<?>> {//key为若引用，使用后gc会自动回收
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v; //强引用，需要remove
            }
        }
        private Entry[] table;//存放具体的值
}
```



## ThreadLocal注意事项

1. 任务数很少的时候，不要强行使用，局部变量新建对象即可。
2. 优先使用框架支持而不是自己随意创建
3. ThreadLocal的Value是强引用，注意使用后的回收，不然造成内存泄露，无法gc