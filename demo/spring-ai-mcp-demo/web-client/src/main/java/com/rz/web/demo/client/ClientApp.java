package com.rz.web.demo.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 */
@EnableFeignClients(basePackages = {"com.rz.web.demo.client"})
@SpringBootApplication
public class ClientApp {
    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }
}
