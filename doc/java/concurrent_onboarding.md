---
title: concurrent_onboarding
date: 2022-11-21 09:00:45
tags: Java语言
categories: 并发编程工具
---

# code

https://github.com/zljin/coreJava/tree/develop/src/main/java/com/zou/concurrency

https://github.com/zljin/coreJava/tree/develop/src/main/java/com/zou/corejava/multithread


# FYI
> http://www.imooc.com/wiki/concurrencylesson

> http://www.imooc.com/wiki/ctoolslesson

> https://blog.csdn.net/ThinkWon/article/details/104863992


# 基础概念

## term

```
1. 内存泄漏: 程序动态分配的堆内存无法释放，导致系统内存浪费甚至到系统崩溃
指无用对象（不再使用的对象）持续占有内存或得不到及时释放，
从而造成的内存空间的浪费，导致新的对象无法申请内存

2. 上下文切换:内核（操作系统的核心）在CPU上对进程或者线程进行切换

3. 进程和线程:
进程进行资源调度和分配的的基本单位,由多个线程构成，多个线程共享进程内存，线程是系统最小的执行和调度单位

4. 并发:单位时间内处理多个任务
5. 并行:同一时刻处理多个任务

6. 用户线程：运行在前台，执行具体的任务
7. 守护线程：运行在后台，为其他前台主线程服务(gc)
    7.1 用户线程退出守护线程也会退出，无论是否执行完任务
    7.2 setDaemon(true)必须在start()方法前执行，否则会抛出 IllegalThreadStateException 异常

8. 死锁：
两个或两个以上的进程（线程）在执行过程中，由于竞争资源或者由于彼此通信而造成的一种阻塞的现象
，这些永远在互相等待的进程（线程）称为死锁进程（线程）。

形成死锁的四个条件：
    1.互斥条件：
        线程(进程)对于所分配到的资源具有排它性，即一个资源只能被一个线程(进程)占用，直到被该线程(进程)释放
    2.请求与保持条件：
        一个线程(进程)因请求被占用资源而发生阻塞时，对已获得的资源保持不放。
    3.不剥夺条件：
        线程(进程)已获得的资源在末使用完之前不能被其他线程强行剥夺，只有自己使用完毕后才释放资源。
    4.循环等待条件：
        当发生死锁时，所等待的线程(进程)必定会形成一个环路（类似于死循环），造成永久阻塞
```

## 多线程编写原则

![](https://github.com/zljin/hexo/blob/develop/image_address/concurrent2.png?raw=true)

```
1. 原子性：一组操作要么全部成功 or 全部失败
频繁的上下文切换

2. 可见性:一个线程对变量的修改，另一个线程能立马看到(volatile解决)
缓存导致可见性问题

3. 有序性:
代码执行顺序编译前和编译后不变(处理器可能会对指令进行重排序)

编译优化带来的有序性问题
Happens-Before 规则(保证多线程执行结果不变)可以解决有序性问题

```
## 线程生命周期

![](https://github.com/zljin/hexo/blob/develop/image_address/concurrent1.png?raw=true)
```
1.新建(new)
    新创建一个线程对象

2.可运行(runable)
    调用start方法以后就是进入可运行就绪状态，等待获取cpu使用权

3.运行(running)
    获得了cpu使用权的线程

4.阻塞(block)
    暂时放弃cpu使用权，进入阻塞状态
        4.1.等待阻塞：线程执行 wait()方法，JVM会把该线程放入等待队列(waitting queue)中
            使本线程进入到等待阻塞状态；
        4.2.同步阻塞：线程在获取 synchronized 同步锁失败(因为锁被其它线程所占用)，
            则JVM会把该线程放入锁池(lock pool)中，线程会进入同步阻塞状态；
        4.3.其他阻塞: 通过调用线程的 sleep()或 join()或发出了 I/O 请求时，线程会进入到阻塞状态。
            当 sleep()状态超时、join()等待线程终止或者超时、或者 I/O 处理完毕时，线程重新转入就绪状态。

5.死亡(dead)

```

## 基本用法

### 创建方式

```
extends Thread
implements Runnable
implements Callable 有返回值FutureTask 
future=拿到Callable返回的值,start完之后,get即可
```

### 线程通信

```
join:保证多条线程一起执行完后才可执行后续代码
yield:一个线程调用此方法,让出自己CPU的使用权
Thread.sleep():线程调用会进入阻塞状态
Object.wait() or notify():消息的等待与通知(生产者消费者模型)
```

如何理解wait()，notify()，notifyAll被定义在Object中？
因为任何对象都可以作为锁,将锁定义在对象上方便，且执行上面的方法的前提是有锁，在同步代码块内

Java如何实现线程间的通讯和协作？
1.syncrhoized加锁的线程使用wait、notify、notifyAll方法
2.ReentrantLock类加锁的线程的Condition类的await()、signal()、signalAll()线程间的直接数据交换。
3.通过管道进行线程通信：字节流和字符流