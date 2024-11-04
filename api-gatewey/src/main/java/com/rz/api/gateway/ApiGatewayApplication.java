package com.rz.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 启动（vm options）参数加入-Dspring.cloud.bootstrap.enabled=true。使用bootstrap.yml为启动配置。nacos配置中心需要这种方式启动
@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

