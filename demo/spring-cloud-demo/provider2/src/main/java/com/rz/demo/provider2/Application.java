package com.rz.demo.provider2;

import com.rz.core.RZHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        System.out.println(RZHelper.getIpV4());
        SpringApplication.run(Application.class, args);
    }
}
