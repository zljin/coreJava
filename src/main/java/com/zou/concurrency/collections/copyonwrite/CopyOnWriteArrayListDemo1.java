package com.zou.concurrency.collections.copyonwrite;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * CopyOnWriteArrayList feature
 *
 * 1. 原理是创建新的副本,读写分离
 *
 * 先拷贝一份数组,在拷贝的数组中写数据,写完后丢弃原来的数组,指向拷贝后的数组
 * 适用于读多写少
 * 因为写是复制机制，写操作会同时驻扎两个对象的内存
 * 写过多导致拷贝数组的量过多,最后导致栈溢出
 *
 *
 * 2. 旧的副本容器不会变，不会有读并发问题
 *
 * 3，迭代的过程中可以修改数组内容，ArrayList不行
 *
 * 4. 最终一致性,不是实时性的
 */
public class CopyOnWriteArrayListDemo1 {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        //CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            System.out.println("list is" + list);
            String next = iterator.next();
            System.out.println(next);

            if (next.equals("2")) {
                list.remove("5");
            }
            if (next.equals("3")) {
                list.add("3 found");
            }
        }
    }
}
