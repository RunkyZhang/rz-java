package com.ww.video.base.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// 启动（vm options）参数加入-Dspring.cloud.bootstrap.enabled=true。使用bootstrap.yml为启动配置。nacos配置中心需要这种方式启动
@SpringBootApplication
@EnableDiscoveryClient
// 扫描feignClient的bean。被调用的其他服务的api包要扫描进来
@EnableFeignClients(basePackages = {"com.ww.video.base.infrastructure.rpc", "com.ww.user.base.api"})
@ComponentScan(basePackages = {"com.ww.video.base", "com.ww.common.architecture"})
@MapperScan(basePackages = "com.ww.video.base.infrastructure.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class VideoBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoBaseApplication.class, args);
    }
}
