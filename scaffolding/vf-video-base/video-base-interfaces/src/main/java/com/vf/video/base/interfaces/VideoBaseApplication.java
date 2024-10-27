package com.vf.video.base.interfaces;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// 启动（vm options）参数加入-Dspring.cloud.bootstrap.enabled=true。使用bootstrap.yml为启动配置。nacos配置中心需要这种方式启动
@EnableDubbo
@SpringBootApplication
@ComponentScan(basePackages={"com.vf.video.base", "com.vf.common.architecture"})
@MapperScan(basePackages = "com.vf.video.base.infrastructure.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class VideoBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoBaseApplication.class, args);
    }
}
