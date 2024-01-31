---
title: future
date: 2023-03-27 10:20:45
tags: Java语言
categories: 并发编程工具
---

治理线程的两大法宝，第一是线程池，第二是future


## Callable feature

Callable类似于Runnble,被其他线程执行的任务，
且实现call()方法且有返回值

Runnble的缺陷，如无返回值，在run方法中无法抛出checked Exception.
https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/future/RunnableCantThrowsException.java


## Future
> 获取子线程的返回结果

如一个方法的计算，可能很耗时间，计算的过程没必要进行等待，遇到耗时的方法
我们可以用子线程执行，子线程执行的时候，可以去做其他的事情，直到想要结果的时候，再用future去控制

## future和callable的关系

Future是一个存储器，它存储了call()这个任务的结果

Future.get获取Callable的执行结果
Future.isDone判断任务是否执行完成，以及还能够取消这个任务和限时获取任务等

call()未执行完毕前，调用get()会阻塞线程，直至返回结果后，才能get到结果，然后主线程才会
切换到runnnable状态


## 用法

### 线程池submit方法返回Future对象

https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/future/OneFuture.java

批量执行演示
https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/future/MultiFutures.java

异常展示
https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/future/GetException.java

超时中断
https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/future/Timeout.java


### 用FutureTask创建future

FutureTask和Callable和Runnable本质上都是一种任务而已
用来获取Future和任务的结果
也是一种包装器，可以把Callable转换为Future和Runnbale,它同时实现二者的接口

既可以作为Runnbale被线程执行，又可作为future得到Callable的返回值

```java
public interface RunnableFuture<V> extends Runnable, Future<V> {
    /**
     * Sets this Future to the result of its computation
     * unless it has been cancelled.
     */
    void run();
}
```

https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/future/FutureTaskDemo.java


## Future的注意点

当for循环批量获取future的结果时，容易发生一部分线程很慢的情况

解决方案：
1. get方法调用时应该适用timeout限制

2. CompleteFuture如果某一个子任务先完成，就可以先去获取到这个结果


Future的生命周期不能后退

就和线程池的生命周期一样，一旦完全完成了任务，它就永久停在了"已完成"的状态，不能重头再来


## summary

我们需要往前看，如果给我从头再来的机会，我不会回头，因为你怎么多年的努力奋斗的结果，怎么可能让他白白流失
，过好当下，往未来看，人生只有一次，过好这一次就好。