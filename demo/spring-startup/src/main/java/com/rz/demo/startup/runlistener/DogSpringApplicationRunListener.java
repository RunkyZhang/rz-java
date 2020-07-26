package com.rz.demo.startup.runlistener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

// 通过spring.factories被set
// 必须要有特定的构造函数
@Order(1)
public class DogSpringApplicationRunListener implements SpringApplicationRunListener {
    private SpringApplication springApplication;
    private String[] args;

    public DogSpringApplicationRunListener(SpringApplication springApplication, String[] args) {
        this.springApplication = springApplication;
        this.args = args;
    }

    @Override
    public void starting() {
        System.out.println("CatSpringApplicationRunListener-starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment configurableEnvironment) {
        System.out.println("CatSpringApplicationRunListener-environmentPrepared");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("CatSpringApplicationRunListener-contextPrepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("CatSpringApplicationRunListener-contextLoaded");
    }

    @Override
    public void finished(ConfigurableApplicationContext configurableApplicationContext, Throwable throwable) {
        System.out.println("CatSpringApplicationRunListener-finished");
    }
}
