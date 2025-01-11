package com.rz.api.gateway;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import java.util.Properties;

// 启动（vm options）参数加入-Dspring.cloud.bootstrap.enabled=true。使用bootstrap.yml为启动配置。nacos配置中心需要bootstrap.yml
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    // demo：curl http://localhost:5050/sayHello
    // 路由策略：（getToPostAndAddBody
    // 调用路径：api gateway -》user-base
    public static void main(String[] args) throws NacosException {
//        String serverAddr = "localhost";
//        String dataId = "rz-api-gateway";
//        String group = "DEFAULT_GROUP";
//        Properties properties = new Properties();
//        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
//        properties.put(PropertyKeyConst.NAMESPACE, "dev");
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        String content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);

        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

/*
* spring:
  cloud:
    gateway:
      routes:
        - id: path_route4
          predicates:
            - Path=/service1
          uri: lb://service1
        - id: path_route5
          predicates:
            - Path=/service2
          uri: lb://service2
* 在这个示例中，CustomLoadBalancer 类实现了简单的轮询负载均衡，将请求分发到两个静态IP地址 localhost:8080 和 localhost:8081。
通过这种方式，你可以在不使用服务发现的情况下，手动配置多个静态IP地址并实现负载均衡。
*
*
* 如果你不想使用服务发现，而是希望直接配置多个静态IP地址进行负载均衡，Spring Cloud Gateway 提供了 uri 配置为 http:// 的方式，并结合 DiscoveryClient 或自定义负载均衡策略来实现。不过，Spring Cloud Gateway 本身并不直接支持在 uri 中配置多个静态IP地址进行负载均衡。
你可以通过以下几种方式来实现：
1. 使用 DiscoveryClient 和自定义负载均衡
你可以手动实现一个 DiscoveryClient 来返回多个静态IP地址，并结合自定义的负载均衡策略。
2. 使用 LoadBalancerClient 和自定义负载均衡
你可以使用 LoadBalancerClient 来实现自定义的负载均衡逻辑。
3. 使用 uri 配置多个静态IP地址并手动实现负载均衡
你可以在代码中手动实现一个负载均衡器，根据请求将流量分发到不同的静态IP地址。
以下是一个简单的示例，展示如何在代码中手动实现一个简单的轮询负载均衡器：
1. 定义一个自定义的 LoadBalancerClient
*
* import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CustomLoadBalancerConfig {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> customLoadBalancer(
            LoadBalancerClientFactory loadBalancerClientFactory) {
        return new CustomLoadBalancer();
    }

    private static class CustomLoadBalancer implements ReactorLoadBalancer<ServiceInstance> {

        private final List<ServiceInstance> instances = new ArrayList<>();
        private final AtomicInteger position = new AtomicInteger(0);

        public CustomLoadBalancer() {
            instances.add(new DefaultServiceInstance("service1", "service1", "localhost", 8080, false));
            instances.add(new DefaultServiceInstance("service2", "service2", "localhost", 8081, false));
        }

        @Override
        public Mono<Response<ServiceInstance>> choose(Request request) {
            int index = position.getAndIncrement() % instances.size();
            ServiceInstance instance = instances.get(index);
            return Mono.just(new DefaultResponse(instance));
        }
    }
}
* */
