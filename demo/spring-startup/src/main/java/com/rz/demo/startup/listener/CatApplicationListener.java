package com.rz.demo.startup.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 通过Component bean被set到ApplicationContext
// 通过ApplicationEvent类和ApplicationListener接口，实现***ApplicationContext***事件处理
// Order数值越小优先级越高; 可以使用Ordered.HIGHEST_PRECEDENCE; 实现Ordered这个interface也行
@Order(CatApplicationListener.order)
@Component
public class CatApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    static final int order = 1;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("CatApplicationListener-ContextRefreshedEvent; " + CatApplicationListener.order);
    }
}
