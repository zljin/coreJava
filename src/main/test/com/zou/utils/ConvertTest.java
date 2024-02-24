package com.zou.utils;

import com.zou.pojo.Person;
import org.junit.jupiter.api.Test;

import java.util.*;

class ConvertTest {
    @Test
    void testReadJsonFile2Object() throws Exception{
        Person person = Convert.readJsonFile2Object("/person.json", Person.class,true);
        System.out.println(Convert.toString(person,true));
    }

    @Test
    void testCopyFile() throws Exception{
        Convert.copyFile("D:\\Codes\\coreJava\\src\\main\\resources\\person.json",
                "D:\\Codes\\coreJava\\src\\main\\resources\\person_1.json",false);
    }

    @Test
    void testToString(){
        List<String> list = Arrays.asList("abc", "bc", "efg", "def", "jkl");
        Map<String, String> map = new HashMap<>();
        map.put("price", "9899");
        map.put("id", "100909");
        System.out.println(Convert.toString(map,true));
        System.out.println(Convert.toString(list,true));
    }

    @Test
    void testGetObject(){
        try {
            Person person = Convert.getObject(Person.class);
            System.out.println(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}