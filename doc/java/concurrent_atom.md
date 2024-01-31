---
title: atom
date: 2023-03-13 09:00:45
tags: Java语言
categories: 并发编程工具
---

> 原子类本质和锁一样都是保证高并发下的线程安全，保证一组资源不可分隔，不可中断
但相比与锁，粒度更细（原子类可以时变量级别的）,除了高度竞争外，原子类效率比锁更高


## 原子类的六大分类

基本类型原子类：AtomicInteger
sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/atomic/AtomicIntegerDemo1.java

原子数组类： AtomicIntegerArray
sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/atomic/AtomicArrayDemo.java


引用原子类：AtomicReference （让一个对象保持原子性）
sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/lock/spinlock/SpinLock.java


升级类型原子类：对普通的变量进行升级为原子类，适合复杂的对象，且更新频率较低的场景 AtomicIntegerFieldUpdater
sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/atomic/AtomicIntegerFieldUpdaterDemo.java


Adder累加器类：
jdk8引入的比较新的类
高并发下LongAdder比AtomicLong效率更高，通过空间换时间的方法

AtomicLong的每一次加法都要flush和reflush，很耗资源

flush即不读取local cache而是直接从shared cache的更新

flush和reflush是一个可见性的概念，每个线程每次必须从内存中去拿取数据（进行同步），然后实时更新局部缓存



LongAdder 引入了分段累加的概念

```java
public long sum() {
    //base变量和Cell[]数组共同参与计数
    //竞争激烈时，各个线程分散累加到自己的槽cell[i]中
    Cell[] as = cells; Cell a;
    //竞争不激烈时直接累加到base上
    long sum = base;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
```

与AtomicLong对比：
AtomicLong具有CAS方法的独特性，且在线程激烈的竞争下，LongAdder吞吐量高很多且更占空间，适合大量的统计和计数场景

sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/atomic/LongAdderDemo.java


Accumulator累加器类
sample: https://github.com/zljin/coreJava/blob/master/src/main/java/com/zou/concurrency/atomic/LongAccumulatorDemo.java


## CAS源码剖析
> 缺点ABA的问题，用版本控制即可解决

```java
//native方法获取原来值的内存地址
valueOffset = unsafe.objectFieldOffset
                (AtomicLong.class.getDeclaredField("value"));

//volatile设置变量的可见性,线程中的变量直接从内存拿去,不经过缓存
private volatile long value;
public AtomicLong(long initialValue) {
    value = initialValue;
}

//如果当前的数值等于预期的数值，则以原子的方式将该值设置为输入值即update
public final boolean compareAndSet(V expect, V update) {
    return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
}
```