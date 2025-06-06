package com.rz.webdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableDubbo
@SpringBootApplication
public class ConsumerApplication {

    // 启动nacos：
    // 目录：./nacos/bin
    // 执行：sh startup.sh -m standalone
    // 查看：http://localhost:8848/nacos
    // 访问：http://127.0.0.1:9090/hello?name=lisi，用dubbo方式调用Provider的sayHelloByName接口
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
