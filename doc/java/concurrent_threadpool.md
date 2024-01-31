---
title: threadpool
date: 2023-03-01 12:00:45
tags: Java语言
categories: 并发编程工具
---

> 系统化的学习并发知识，更偏向于应用开发这块，能在在生产合理使用并发的知识点，提高性能和效率和安全。

代码路径：
> https://github.com/zljin/coreJava/blob/dailyCode/src/main/java/com/zou/corejava/multithread/ThreadPoolManager.java <br>
> https://github.com/zljin/coreJava/tree/develop/src/main/java/com/zou/concurrency/threadpool



## 第一个问题：为何要使用线程池？什么场景下需要使用线程池？

1. 反复创建和销毁线程对系统开销大
2. 过多线程吃内存，容易OOM

线程池的作用就是可以使用少量的线程，并让这部分线程保持工作，从而反复执行任务避免生命周期的损耗。

好处有，1.加快响应速度，合理利用CPU内存和内存  2.统一管理线程资源


适合的场景：
    1. 如服务器上接收大量请求时，每次HTTP的请求都对应一个线程，如果使用线程池可以减少线程的创建和损耗从而提高服务器的工作效率
    2. 如在开发中需要创建五个线程可用线程池管理



## 第二个问题：线程池是如何工作的？

### 我们首先看线程池参数

```java
public ThreadPoolExecutor(int corePoolSize,//核心线程数
                            int maximumPoolSize,//最大线程数
                            long keepAliveTime,//线程存活时间
                            TimeUnit unit,//单位
                            BlockingQueue<Runnable> workQueue,//工作队列，存放todo的线程
                            RejectedExecutionHandler handler//拒绝策略
) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
            Executors.defaultThreadFactory(), handler);
}
```

拒绝策略的四种类型

    AbortPolicy -> 直接抛出异常
    CallerRunsPolicy -> 将任务返回给原来的进程执行
    DiscardOldestPolicy -> 此策略将丢弃最早的未处理的任务请求
    DiscardPolicy --> 直接抛弃任务

工作队列的三种类型？

    SynchronousQueue：直接交接，无队列存储
    LinkedBlockingQueue: 无界队列，无限存储
    ArrayBlockingQueue: 有界队列


### 线程池是如何添加线程的呢

    1. 如果当前线程数小于corePoolSize,即使其他线程空闲,也会在线程池新创建一条线程
    2. 如果当前线程数大于corePoolSize,小于maximumPoolSize,则将todo的线程任务存放在工作队列中等待
    3. 如果工作队列也满了,且当前线程数小于maximumPoolSize,则创建一个新线程来运行任务
    4. 如果工作队列满了,且当前线程数大于maximumPoolSize,则拒绝该任务



## 使用线程池

> jdk中有Executors的工具类，里面有很多不同类型的线程池方便你快速创建,但本质都是ThreadPoolExecutor的构造方法而已

我们可以分析Executors里面各个线程池的特点

```java
/**
* LinkedBlockingQueue无容量上线，当请求越来越多后，并且无法及时处理完毕的时候，就会占用大量内存
从而导致OOM

又因为是无容量上线，所以过期时间这个参数就无意义

线程池不能来着不拒，工作队列是无界的，导致的任务过多而堆积

*/
public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory);
    }


/**
* SynchronousQueue不会存储todo线程，直接透传
且最大线程数是无限的，所以核心线程数就无意义了

线程过多增加，cacheThead会一直增加线程，线程数量过多可能造成线程泄露
线程泄露就是线程执行完后，无法进行回收，这种一般都是业务逻辑问题

**/
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }

/**
* 延迟计划线程池
*/
public ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
              new DelayedWorkQueue());
    }
```


btw,Executors的工具类创建的线程池都又一定的问题，比较成熟的做法是:根据不同的业务场景dev自己的线程池，且参数设定有讲究！！!


### 线程池的数量设置规律

1. CPU密集型，最佳为核心线程数的两倍
2. IO密集型 = CPU核心数*(1+平均等待时间/平均工作时间)
3. 或者更具性能测试工具来确定

## 线程池的销毁
> 看需求是要立即销毁，还是等正在进行任务执行完，进行停止后销毁

```java
executorService.shutdown();
List<Runnable> runnalbeList = executorService.shutdownNow();
```

## 使用线程池的注意点
1. 避免任务堆积 （线程池不能来着不拒，工作队列是无界的，导致的任务过多而堆积）
2. 避免线程数过度增加 （线程过多增加，如cacheThead会一直增加线程，线程数量过多可能造成线程泄露）
3. 排查线程泄露（线程泄露就是线程执行完后，无法进行回收，这种一般都是业务逻辑问题）

