package com.rz.auto.tool;

import org.springframework.stereotype.Component;

@Component
public class DogToolService {
    private String name;
    private int age;

    public DogToolService(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
