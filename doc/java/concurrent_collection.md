---
title: 并发容器
date: 2023-03-26 09:00:45
tags: Java语言
categories: 并发编程工具
---


## summary

```
ConcurrentHashMap：
    线程安全的HashMap
    应用场景：统计文本的字母出现的次数 采用map结构进行存储,为了提高效率，我们用4个线程去处理提高效率。多个线程去操作同一个map统计
    
CopyOnWriteArrayList:
    先拷贝一份数组,在拷贝的数组中写数据,写完后丢弃原来的数组,指向拷贝后的数组。适合读多写少的场景，保证最终一致性。
    
BlockingQueue:
    feature:
        1. 当生产者添加一个元素时，如果队列已满，则线程被阻塞
        2. 当消费取出一个元素时，如果队列为空，则线程被阻塞
    通过这个feature控制线程之间的通信
```

## code

https://github.com/zljin/coreJava/tree/develop/src/main/java/com/zou/concurrency/collections