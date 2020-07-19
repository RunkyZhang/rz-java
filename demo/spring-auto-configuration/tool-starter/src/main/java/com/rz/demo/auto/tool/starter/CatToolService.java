package com.rz.demo.auto.tool.starter;

import org.springframework.stereotype.Component;

@Component
public class CatToolService {
    private String name;
    private int age;

    public CatToolService(ToolProperties toolProperties) {
        this.name = toolProperties.getName();
        this.age = toolProperties.getAge();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
