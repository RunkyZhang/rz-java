package com.rz.webdemo.api.webdemo.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class ProviderApplication {

    // 启动nacos：
    // 目录：./nacos/bin
    // 执行：sh startup.sh -m standalone
    // 查看：http://localhost:8848/nacos
    // 等待Consumer用dubbo的方式调用sayHelloByName接口
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
