package com.rz.demo.startup.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

// 通过spring.factories被set到ApplicationContext
// ApplicationContextInitializer是一个回调接口，它会在ConfigurableApplicationContext的refresh()方法调用之前被调用,做一些容器的初始化工作
// Order数值越小优先级越高; 可以使用Ordered.HIGHEST_PRECEDENCE; 实现Ordered这个interface也行
@Order(DogApplicationContextInitializer.order)
public class DogApplicationContextInitializer implements ApplicationContextInitializer {
    static final int order = 2;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("DogApplicationContextInitializer; " + DogApplicationContextInitializer.order);
    }
}
