package com.rz.demo.startup.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

// 通过代码被set到ApplicationContext
// 通过ApplicationEvent类和ApplicationListener接口，实现***ApplicationContext***事件处理
public class CowApplicationListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("CowApplicationListener-ContextRefreshedEvent; " + this.getOrder());
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
