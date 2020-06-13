package com.rz.demo.provider1;

import com.rz.core.RZHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Application {
    public static void main(String[] args) throws Exception {
        System.out.println(RZHelper.getIpV4());
        SpringApplication.run(Application.class, args);
    }
}
