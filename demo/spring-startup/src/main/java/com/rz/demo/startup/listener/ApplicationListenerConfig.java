package com.rz.demo.startup.listener;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class ApplicationListenerConfig {
    // 通过Config bean被set到ApplicationContext
    // 通过ApplicationEvent类和ApplicationListener接口，实现***ApplicationContext***事件处理
    @EventListener(classes = {ContextRefreshedEvent.class})
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("ConfigApplicationListener-ContextRefreshedEvent");
    }
}
