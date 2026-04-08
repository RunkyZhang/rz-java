package com.rz.langchain.demo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 * https://langchain4j.cn/get-started/
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.rz.langchain.demo.server.rpc"})
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
