---
title: AQS
date: 2023-03-27 09:20:45
tags: Java语言
categories: 并发编程工具
---

## FYI
```
美团技术团队《从ReentrantLock的实现看AQS的原理及应用》：https://mp.weixin.qq.com/s/sA01gxC4EbgypCsQt5pVog
老钱《打通 Java 任督二脉 —— 并发数据结构的基石》：https://juejin.im/post/5c11d6376fb9a049e82b6253
HongJie《一行一行源码分析清楚AbstractQueuedSynchronizer》：https://javadoop.com/post/AbstractQueuedSynchronizer
爱吃鱼的KK《AbstractQueuedSynchronizer 源码分析 (基于Java 8)》：https://www.jianshu.com/p/e7659436538b
waterystone《Java并发之AQS详解》：https://www.cnblogs.com/waterystone/p/4920797.html
英文论文的中文翻译：https://www.cnblogs.com/dennyzhangdd/p/7218510.html
AQS作者的英文论文：http://gee.cs.oswego.edu/dl/papers/aqs.pdf
```

## AQS是什么

一个用于构建锁or同步器or协作工具类的工具类框架。

你可以查看AbstractQueuedSynchronizer类的子类，其中
CountDownLatch,ReentrantReadWriteLock,Semaphore,ReentrantLock都用到了此工具类

他们的抽象相似，都类似与一个"闸门" 底层用同一个基类(AQS),
可以把AQS当作一个工具类，将闸门的抽象逻辑功能抽取出来

## 为什么要用AQS

有了这个AQS的工具，构建线程协作类就很容易了


## AQS 三要素

1. state
2. 控制线程抢锁和配合的FIFO队列
3. 期望协作工具类去实现的 获取/释放等重要方法

## 看源码

> 用AQS三要素来分析源码,不同的工具，state意义也不同

### CountDownLatch

分析await()阻塞和countDown()释放
state的含义：表示倒计数器还剩多少

```java

//1.0 构造方法定义state
public CountDownLatch(int count) {
    if (count < 0) throw new IllegalArgumentException("count < 0");
    this.sync = new Sync(count);
}

private static final class Sync extends AbstractQueuedSynchronizer {

    //1.1 这里初始化state
    Sync(int count) {
        setState(count);
    }

    protected int tryAcquireShared(int acquires) {
        return (getState() == 0) ? 1 : -1;
    }

    protected boolean tryReleaseShared(int releases) {
        for (;;) {
            int c = getState();
            if (c == 0) //c=0说明倒计数器已经被其他地方释放了，不需要额外操作
                return false;
            int nextc = c-1;
            if (compareAndSetState(c, nextc))
                return nextc == 0;
        }
    }
}

public void await() throws InterruptedException {
    sync.acquireSharedInterruptibly(1);
}

public final void acquireSharedInterruptibly(int arg)
        throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    if (tryAcquireShared(arg) < 0) //2.1 返回false代表倒计数器还没减到0，阻塞，并存入到队列中
        //3.0 将阻塞的线程存放到队列中
        doAcquireSharedInterruptibly(arg);
}


public void countDown() {
    sync.releaseShared(1);
}

public final boolean releaseShared(int arg) {
    if (tryReleaseShared(arg)) {//2.0 返回为true时，即为倒计数器为0,不阻塞了
        //3.1 将阻塞的队列释放第一个头元素
        doReleaseShared();
        return true;
    }
    return false;
}
```

### Semaphore

分析获取锁的acquire()方法
state的含义：表示许可证的剩余数量

```java

public void acquire() throws InterruptedException {
    sync.acquireSharedInterruptibly(1);
}

public final void acquireSharedInterruptibly(int arg)
        throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    if (tryAcquireShared(arg) < 0) //在 syn中实现
        doAcquireSharedInterruptibly(arg);// 若阻塞则丢入队列中
}

static final class NonfairSync extends Sync {
    private static final long serialVersionUID = -2694183684443567898L;

    NonfairSync(int permits) {
        super(permits);
    }

    protected int tryAcquireShared(int acquires) {
        return nonfairTryAcquireShared(acquires);//这里
    }
}

final int nonfairTryAcquireShared(int acquires) {
    for (;;) {
        int available = getState();
        int remaining = available - acquires;//剩余的许可证数量够不够这次需要
        if (remaining < 0 ||//表示复数，直接失败
            compareAndSetState(available, remaining))//表示正数，返回正数，CAS更新state的可用状态数量,有可能失败
            return remaining;
    }
}

```

### ReentrantLock

分析释放锁的方法：tryRelease
state的含义：代表可重入的次数

每次释放锁，先判断是不是当前线程持有锁的线程释放，不是则抛异常

是就减1，如果减到0则完全释放，相当于free就是true,并发state设置为0

```java
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```


## DIY一次性闸门

> https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/aqs/OneShotLatch.java

