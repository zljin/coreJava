package com.zou.concurrency.lock.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock不会synchronized一样，异常的时候自动释放锁
 * 最佳实践是finally释放锁
 *
 */
public class MustUnlock {

    private static Lock lock  = new ReentrantLock();


    //但lock方法不能中断，死锁就会永久等待，下面用trylock
    //tryLock尝试获取锁，如果当前锁没被占用返回true,该方法会立即返回，不会一直等待
    //tryLock(long time,TimeUnit unit):超时就放弃
    public static void main(String[] args) {
        lock.lock();
        try {
            //获取本锁保护的资源
            System.out.println(Thread.currentThread().getName()+"开始执行任务");
        }catch (Exception e){
            System.err.println(e.getMessage());
        }finally {
            lock.unlock();
        }
    }

}
