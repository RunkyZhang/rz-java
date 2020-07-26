package com.rz.demo.startup.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

// ApplicationContent初始好之后执行Runner
// getOrder数值越小优先级越高; 使用Order标签也行
@Component
public class CatApplicationRunner implements ApplicationRunner, Ordered {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("CatApplicationRunner; " + this.getOrder());
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
