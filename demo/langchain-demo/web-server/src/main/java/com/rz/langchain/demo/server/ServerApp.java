package com.rz.langchain.demo.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Map;

/**
 * Hello world!
 * https://langchain4j.cn/get-started/
 * RequestExecutor.executeRaw -> DefaultOpenAiClient.completion -> SyncRequestExecutor.execute -> LoggingHttpClient.execute
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.rz.langchain.demo.server.rpc"})
@MapperScan("com.rz.langchain.demo.server.mapper")
public class ServerApp {
    public static void main(String[] args) {
        Map<String, String> props = Helper.loadProperties();
        System.out.println(props.get("ai.model.bailian.apiKey"));

        SpringApplication.run(ServerApp.class, args);
    }
}
