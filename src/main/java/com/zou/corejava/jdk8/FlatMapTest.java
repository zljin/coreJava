package com.zou.corejava.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://zhuanlan.zhihu.com/p/103686124
 */
public class FlatMapTest {

    private static List<KlassGroup> groupList = Arrays.asList(
            new KlassGroup(new Klass(1), new Klass(2), new Klass(3)),
            new KlassGroup(new Klass(4), new Klass(5), new Klass(6)),
            new KlassGroup(new Klass(7), new Klass(8), new Klass(9)),
            new KlassGroup(new Klass(10))
    );

    //需求:将groupList全部转为List<Klass>
    public static void main(String[] args) {

        //两个for循环很低效
//        List<Klass> result2 = new ArrayList<>();
//        for (KlassGroup group : groupList) {
//            for (Klass klass : group.getKlassList()) {
//                result2.add(klass);
//            }
//        }

        /**
         *  * stream api 的 flatMap方法接受一个lambda表达式函数，
         *  * 函数的返回值必须也是一个stream类型，flatMap方法最终会把所有返回的stream合并，map方法做不到这一点
         */
        List<Klass> collects = groupList.stream().flatMap(e -> e.getKlassList().stream()).collect(Collectors.toList());
        System.out.println(collects);





    }

    private static class Klass {
        private int field;

        public Klass(int field) {
            this.field = field;
        }

        @Override
        public String toString() {
            return "field=" + field;
        }
    }

    private static class KlassGroup {
        private List<Klass> group = new ArrayList<>();

        public KlassGroup(Klass... objList) {
            for (Klass item : objList) {
                this.group.add(item);
            }
        }

        public List<Klass> getKlassList() {
            return group;
        }
    }
}
