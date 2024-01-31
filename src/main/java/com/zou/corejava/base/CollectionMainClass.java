package com.zou.corejava.base;

import java.util.*;

public class CollectionMainClass {

    public static void main(String[] args) {
        //mainArray();
        //mainMap();
        mainIterable();
        //mainSet();
    }


    /**
     * Array: 其元素类型都相同,数组长度固定不变,必须初始化
     */
    public static void mainArray() {
        int[] arr = new int[5];
        int[] arr2 = new int[]{20, 68, 34, 22, 34};
        int[][] arr3 = new int[][]{
                {4, 5, 6, 7},
                {8, 9, 10, 11},
                {1, 2}
        };

        System.out.println(arr3[0][3]);
        System.out.println(arr3[2][1]);
    }


    /**
     * Collection: 单列集合
     * List:有序，可重复
     * Set: 无序. 不可重复
     */
    public static void mainIterable() {
        List<String> list = Arrays.asList("abc", "bc", "efg", "def", "jkl");

        /**
         * Iterable和Iterator的区别
         * <p>
         * Iterable接口是单列集合的顶层父类,实现这个接口的类具有可迭代性
         * 里面有Iterator()方法用于迭代遍历集合元素
         * <p>
         * 意义在于创建新的Iterable不用担心Iterator指针问题,互不影响
         */
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }

        //在迭代器遍历的时候不要去添加删除元素，会有并发问题同时操作
//        ListIterator lit = list.listIterator();
//        while(lit.hasNext()){
//            if("def".equals(lit.next())){
//                lit.add("xyz");
//            }
//        }
    }

    /**
     * Comparable:类的自然排序,类继承此接口,重写compareTo方法
     * Comparator:类的比较器,在集合外部再进行一次比较逻辑
     * 用法如再TreeSet的构造方法入参中，添加比较器的匿名函数即可自动排序
     * HashSet常用作数据检索
     */
    public static void mainSet() {
        //由于优先级的关系,Node类只会根据Comparator中的年龄自行进行比较
        TreeSet ts = new TreeSet(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.age - o2.age;// if return<0 then swap
            }
        });
        ts.add(new Node("李四", 20));
        ts.add(new Node("王五", 19));
        ts.add(new Node("阿无", 14));
        ts.add(new Node("李四", 18));
        System.out.println(ts);
    }


    /**
     * Map: 双列集合
     */
    public static void mainMap() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "huawei");
        map.put("price", "9899");
        map.put("id", "100909");

        /**
         * Map.Entry可以一次性获得这两个值,比map.keySet()方法快一倍,因为第一种要循环请求
         */
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        map.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }


    static class Node implements Comparable<Node> {
        String name;
        int age;

        public Node(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public int compareTo(Node o) {
            int nameResult = this.name.compareTo(o.name);
            return nameResult == 0 ? this.age - o.age : nameResult;
        }

        @Override
        public String toString() {
            return this.name + ":" + this.age;
        }
    }


}
