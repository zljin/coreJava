package com.zou.corejava.jdk8;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author leonard
 * @date 2022/12/27
 * @Description Optional深度解析
 * https://pdai.tech/md/java/java8/java8-optional.html
 */
public class OptionalTest {

    public static void main11(String[] args) {
        Optional<String> name = Optional.of("leonard");
        name.ifPresent((v)->{
            System.out.println("my name is "+v);
        });

    }

    public static void main(String[] args) {
        Optional<String> name = Optional.of("leonard");
        Optional<Object> empty = Optional.ofNullable(null);
        System.out.println(name.isPresent());
        System.out.println(empty.isPresent());
        if(name.isPresent()){
            System.out.println(name.get());
        }

        try{
            System.out.println(empty.get());
        }catch (NoSuchElementException ex){
            System.out.println(ex.getMessage());
        }

        System.out.println(name.orElse("no value present"));
        System.out.println(empty.orElse("no value present11"));

        Optional<String> upperName = name.map((v) -> v.toUpperCase());

        upperName = name.flatMap((v) -> Optional.of(v.toUpperCase()));

        System.out.println(upperName.orElse("no value found"));




    }
}
