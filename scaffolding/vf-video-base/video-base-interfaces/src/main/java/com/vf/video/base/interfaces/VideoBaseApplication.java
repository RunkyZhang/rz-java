package com.vf.video.base.interfaces;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
@SpringBootApplication
@ComponentScan(basePackages={"com.vf.video.base", "com.vf.common.architecture"})
@MapperScan(basePackages = "com.vf.video.base.infrastructure.mapper")
public class VideoBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoBaseApplication.class, args);
    }
}