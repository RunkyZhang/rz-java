package com.rz.demo.startup.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

// 通过spring.factories被set到ApplicationContext
// 通过ApplicationEvent类和ApplicationListener接口，实现***ApplicationContext***事件处理
public class DogApplicationListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("DogApplicationListener-ContextRefreshedEvent; " + this.getOrder());
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
