package com.zou.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolManager {

    private ThreadPoolManager(){}

    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;
    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 50;
    private static final int KEEP_ALIVE_TIME = 60;

    static {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE
                , MAX_POOL_SIZE
                , KEEP_ALIVE_TIME
                , TimeUnit.SECONDS
                /**
                 * 工作队列，存放todo的线程
                 * SynchronousQueue：直接交接，无队列存储 --> 造成线程数过度增加
                 * LinkedBlockingQueue: 无界队列，无限存储  -->造成任务堆积
                 * ArrayBlockingQueue: 有界队列
                 */
                , new ArrayBlockingQueue<>(QUEUE_CAPACITY)
                /**
                 * 四大拒绝策略: 当最大线程数已满,阻塞队列已满
                 * AbortPolicy -> 直接抛出异常
                 * CallerRunsPolicy -> 将任务返回给原来的进程执行
                 * DiscardOldestPolicy -> 此策略将丢弃最早的未处理的任务请求
                 * DiscardPolicy --> 直接抛弃任务
                 */
                , new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
