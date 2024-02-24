package com.zou.pojo;

import lombok.Data;

@Data
public class Person implements Comparable<Person> {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {
    }

    public void say(String str) {
        System.out.println(str + " my name is " + name + " and " + age + " year's old");
    }

    static void run(String str) {
        System.out.println(str + " static run....");
    }

    @Override
    public int compareTo(Person o) {
        int nameResult = this.name.compareTo(o.name);
        return nameResult == 0 ? this.age - o.age : nameResult;
    }

}
