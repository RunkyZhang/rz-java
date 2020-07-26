package com.rz.demo.startup.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

// ApplicationContent初始好之后执行Runner；CommandLineRunner和ApplicationRunner区别只有参数不同
// getOrder数值越小优先级越高; 使用Order标签也行
@Component
public class DogCommandLineRunner implements CommandLineRunner, Ordered {
    @Override
    public void run(String... strings) throws Exception {
        System.out.println("DogApplicationRunner; " + this.getOrder());
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
