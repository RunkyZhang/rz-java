package com.vf.user.base.interfaces;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
@SpringBootApplication
@ComponentScan(basePackages={"com.vf.user.base", "com.vf.common.architecture"})
@MapperScan(basePackages = "com.vf.user.base.infrastructure.mapper")
public class UserBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserBaseApplication.class, args);
    }
}
