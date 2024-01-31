---
title: 并发实战_打造高性能缓存
date: 2023-03-27 16:43:45
tags: Java语言
categories: 并发编程工具
---


## feature

```
基础好的直接从ImoocCache7.java 开始看起，里面有细节备注by leonard

通过Future避免重复计算
通过ConcurrentHashMap.putIfAbsent避免某种特殊场景
考虑缓存污染和重试的场景
处于安全考虑的设置缓存有效期

最后通过线程池加countDownLatch进行压测，看看效果
```

## code
https://github.com/zljin/coreJava/tree/master/src/main/java/com/zou/concurrency/imooccache