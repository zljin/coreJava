---
title: lock
date: 2023-03-12 09:00:45
tags: Java语言
categories: 并发编程工具
---

> 锁是一种控制共享资源访问的工具


## Lock接口


synchronized瓶颈：
1. 效率低，锁释放的情况小，试图获取锁时不能设定超时，不能中断一个正在试图获取锁的线程
2. 不够灵活，加减锁释放单一，每个锁仅有单一的条件，可能不够
3. 无法知道是否能够成功获取锁



Lock接口其中的实现类有ReentrantLock


lock():最普通的获取锁，最好的实践就是try catch finally,在异常的时候释放锁，但不会中断
tryLock():比lock更好，可中断，不会发生死锁永久等待的问题


sample: https://github.com/zljin/coreJava/tree/master/src/main/java/com/zou/concurrency/lock/lock


## 锁的可见性保证
> 一个线程对变量的修改，另一个线程能立马看到 happen-before
其中Lock是肯定有可见性保障的


## 锁的分类

![](https://github.com/zljin/hexo/blob/master/image_address/lock1.png?raw=true)

### 乐观锁与悲观锁

```
悲观锁也叫互斥同步锁

悲观认为每次有线程访问时都会更改共享数据，都要加锁

    劣势：
        1. 阻塞和唤醒带来性能劣势
        2. 永久阻塞
    应用场景：
        lock.class ，synchronized.class

        数据库的例子:
            select for update
        
        适合写多的场景

乐观锁也叫非互斥同步锁

采用CAS算法加版本控制实现(解决ABA问题)
    应用场景：
        AtomicInteger.class ，并发容器
        Git 也是乐观锁的体现
        数据库的例子：
            表中添加lock_version字段，update时查询当前lock_version是不是上个lock_version，然后lock_version++

        适合读多的场景
```

sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/reentrantlock/PessimismOptimismLock.java

### 可重入锁(也叫递归锁)


先第一次申请这把锁后，当再次申请这把锁时，无需提前释放这把锁，而是可以直接继续使用这把锁.(可重入)

同一个线程可以多次获取同一把锁,而不需要强制先释放

好处：避免死锁，减少频繁解锁的流程

sample: 
https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/reentrantlock/GetHoldCount.java
https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/reentrantlock/RecursionDemo.java


> 可重入锁源码分析 

```java
//class ReentrantLock
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {//1. 获取锁时:先判断当前线程就是已经占有锁的线程
        int nextc = c + acquires; //2. state+1
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}

protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread()) //1. 释放锁时::先判断当前线程就是已经占有锁的线程
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {//2. state=0则为真正释放锁
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```

> 非可重入锁源码分析

```java
//class ThreadPoolExecutor.Worker
protected boolean tryAcquire(int unused) {
    //1. 第一次直接尝试获取锁，state默认为0，且expect state也为0，则修改state 为1 
    //2. 当同一个线程再获取锁时，expect state还是0,但state已经变为1了，则return false
    if (compareAndSetState(0, 1)) { 
        setExclusiveOwnerThread(Thread.currentThread());
        return true;
    }
    return false;
}

protected boolean tryRelease(int unused) {
    setExclusiveOwnerThread(null);
    setState(0);//1. 释放锁直接设置为0即可
    return true;
}
```

### 公平锁

> 按照线程请求顺序来分配锁。非公平时不完全按照顺序，有时可插队

优缺点：
    1. 优点：保证线程公平平等
    2. 缺点：吞吐量小，，慢

非公平可能会导致饥饿线程

sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/reentrantlock/FairLock.java

### 共享锁和排他锁

> 共享锁如读锁 排他锁如写锁

读写锁的规则：

多个线程只申请读锁，都可以申请到
只要有一个线程占用了写锁，其他线程申请写锁或者读锁必须等待前面写锁的释放
如果有一个线程占用了读锁，其他线程要申请写锁，必须等待前面读锁的释放

注意理解：读写锁是一把锁，有两种锁定关系而已

把获取写锁理解为 把读写锁进行写锁定

基本用法：
sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/readwrite/CinemaReadWrite.java


读写锁的交互

1. 读锁插队policy: 

    公平锁不允许插队
    非公平锁
        1. 写锁可随时插队，避免饥饿。
        2. 读锁仅在等待队列头节点不是想获取写锁线程的时候可以插队


下面是非公平锁插队策略的源码：

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = -8159625535654395037L;
    final boolean writerShouldBlock() {
        return false; // writers can always barge
    }
    final boolean readerShouldBlock() {
        //当wait queue中头节点为写锁时，读锁不能插队
        return apparentlyFirstQueuedIsExclusive();
    }
}
```


下面插队策略的模拟，sample:
https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/readwrite/NonfairBargeDemo.java



### 锁升级和降级

一般只支持锁的降级，不支持锁的升级，如写的过程是悲观锁，当后面只有读取操作后，可以将锁降级为乐观锁
减少锁的创建和关闭消耗

演示ReentrantReadWriteLock可以降级，不能升级：
sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/readwrite/Upgrading.java

为什么不支持锁升级，这容易带来死锁，如果两个线程都要要升级，都要等待对方解锁
只有保证每次只有一个线程升级，即可使用锁的升级

### 自旋锁和阻塞锁
> 产生的原因

阻塞和唤醒一个Java线程需要操作系统切换CPU的状态完成，这些状态转换
需要耗费处理器的时间

如果同步代码中的内容过于简单，状态转换消耗的时间有可能比用户的代码片段执行的时间还要长

许多场景中，同步资源的锁定较短，为了这一段小时间去切换线程，线程挂起和恢复现场的花费时间会让系统得不偿失

自旋锁就是当前面线程的锁定时，我们自己先自旋一下（先占着CPU资源不放，如果太久则太耗CPU资源），前面线程释放后，则进行加锁。这样可以避免切换线程的开销。

> 原理和源码分析

atmoic包下的类基本是自旋锁的实现,基于CAS算法

java.util.concurrent.atomic.AtomicInteger#getAndIncrement
```java
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}

public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    //do while就是一个自旋操作
    do {
        var5 = this.getIntVolatile(var1, var2);
        //while里死循环，直至修改成功
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
```

自定义自旋锁sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/spinlock/SpinLock.java

使用场景：
适用于多核服务器，且并发度不高，且适用与临界区很小(即线程拿到锁后释放的时间小)的情况


### 可中断锁

线程A执行锁中的代码，B线程等待获取该锁，但时间太长，B线程不想等待，就不等了，直接中断，这叫可中断锁。

## 锁的优化

### JVM的锁优化

自旋锁和自适应锁
JVM会给你判断当前场景，然后自适应锁需要的锁

锁消除
有些地方不需要锁的情况下，JVM会锁消除

锁粗化
减少一段代码中要反复加锁减锁的场景，直接把锁范围扩大了

### 写代码时如何优化锁和提高并发性能

1. 缩小同步代码块
2. 尽量不要锁住方法（范围太大了，不建议，按需选择，精准部位）
3. 减少锁的次数（比如10个线程打印10个日志，如果每个线程来吧互斥锁就很低效，我们可以把这10个线程
放入消息队列里面，然后统一加锁减锁）
4. 避免人为制造"热点"
5. 不要锁中再包含锁，容易导致获取锁不一致
6. 选择适合的锁，如多读少些，读写锁，并发度不高的，原子类