package com.rz.demo.consumer2;

import com.rz.core.RZHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) throws Exception {
        System.out.println(RZHelper.getIpV4());
        SpringApplication.run(Application.class, args);
    }
}
