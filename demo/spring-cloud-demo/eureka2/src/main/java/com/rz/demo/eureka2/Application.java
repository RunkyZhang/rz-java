package com.rz.demo.eureka2;

import com.rz.core.RZHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Application {
    public static void main(String[] args) throws Exception {
        System.out.println(RZHelper.getIpV4());

        SpringApplication.run(Application.class, args);
    }
}
