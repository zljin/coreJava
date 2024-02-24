package com.zou.basejava;

import com.zou.pojo.Person;
import com.zou.utils.Convert;

import java.util.Comparator;
import java.util.TreeSet;

public class SetMain {
    public static void main(String[] args) {
        TreeSet ts = new TreeSet(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge();
            }
        });
        ts.add(new Person("李四", 20));
        ts.add(new Person("王五", 19));
        ts.add(new Person("阿无", 14));
        ts.add(new Person("李四", 18));
        System.out.println(Convert.toString(ts,false));
    }
}
