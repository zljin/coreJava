package com.zou.utils;

import com.zou.pojo.Person;
import org.junit.jupiter.api.Test;

class BuilderTest {

    @Test
    void test() throws Exception{
        Person person = Builder.of(Person::new)
                .with(Person::setAge, 22)
                .with(Person::setName, "Win10").builder();
        System.out.println(person);
    }
}
