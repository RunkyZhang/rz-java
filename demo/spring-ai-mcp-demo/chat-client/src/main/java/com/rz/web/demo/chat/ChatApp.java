package com.rz.web.demo.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * Hello world!
 */
// 启动参数加：-Dspring.config.location=classpath:/application.yml
@EnableFeignClients(basePackages = {"com.rz.web.demo.chat"})
@EnableReactiveFeignClients(basePackages = {"com.rz.web.demo.chat"})
@SpringBootApplication
public class ChatApp {
    public static void main(String[] args) {
        SpringApplication.run(ChatApp.class, args);
    }
}
