package com.rz.webdemo.api;

public interface DemoService {
    /**
     * 根据名字say hello
     * @param name 名字
     * @return name + hello
     */
    String sayHelloByName(String name);
}