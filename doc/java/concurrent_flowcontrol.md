---
title: 并发流程控制
date: 2023-03-26 15:00:45
tags: Java语言
categories: 并发编程工具
---


## summary

```
CountDownLatch：倒数计数器，每次执行完一个多线程，计数器减去1,当减为0的时候,主线程开始执行，不能循环利用

CyclicBarrier：循环屏障,允许一组线程相互等待,直到所有线程到达一个公共的屏障点,
这些进程再一起执行后续的逻辑,可重复使用(类比于班车,只有人到期了才能开)

Semaphore(信号量):(类比于地铁闸机，只开5个口进行进出)

作用是限制某段代码的并发数，Semaphore的构造函数可以传入一个int型整数n，表示某段代码最多只有n个线程可以访问，
如果超出了n，那么请等待，等到某个线程执行完毕这块代码，下个线程在进入，如果设置为1，相当于synchronized

其中acquire(3)可以设置权重，比如我一个线程需要拿3个信号量，如果构造函数只有3，那就别人拿不到，当然释放的时候也要为3

获取和释放一定要一致，不然不够用

一般设置公平性要设置为true更为合理，信号量适合执行比较慢的任务



相比较synchionized和ReentrantLock都是一次只允许一个线程访问资源，Semaphore(信号量)可以允许多个线程访问某个资源


Exchanger:
线程协作工具类，用于两个线程之间的数据交换，两个线程都执行到exchange方法，那么这两个线程就可以交换数据

Condition:
    condition.await()  //线程阻塞
    condition.signal()  //线程唤醒
```

## code

https://github.com/zljin/coreJava/tree/develop/src/main/java/com/zou/concurrency/flowcontrol

