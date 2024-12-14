package com.rz.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// 启动（vm options）参数加入-Dspring.cloud.bootstrap.enabled=true。使用bootstrap.yml为启动配置。nacos配置中心需要bootstrap.yml
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    // demo：curl http://localhost:5050/sayHello
    // 路由策略：（getToPostAndAddBody
    // 调用路径：api gateway -》user-base
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

