package com.rz.api.gateway.config;

import com.rz.api.gateway.filter.ChangeMethodGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RouteLocatorConfig {

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        //@formatter:off
//        return builder.routes()
//                .route("path_route", r -> r.path("/get")
//                        .uri("http://httpbin.org"))
//                .route("host_route", r -> r.host("*.myhost.org")
//                        .uri("http://httpbin.org"))
//                .route("rewrite_route", r -> r.host("*.rewrite.org")
//                        .filters(f -> f.rewritePath("/foo/(?<segment>.*)",
//                                "/${segment}"))
//                        .uri("http://httpbin.org"))
//                .route("circuitbreaker_route", r -> r.host("*.circuitbreaker.org")
//                        .filters(f -> f.circuitBreaker(c -> c.setName("slowcmd")))
//                        .uri("http://httpbin.org"))
//                .route("circuitbreaker_fallback_route", r -> r.host("*.circuitbreakerfallback.org")
//                        .filters(f -> f.circuitBreaker(c -> c.setName("slowcmd").setFallbackUri("forward:/circuitbreakerfallback")))
//                        .uri("http://httpbin.org"))
//                .route("websocket_route", r -> r.path("/echo")
//                        .uri("ws://localhost:9000"))
//                .build();
//        //@formatter:on
//    }

    @Bean
    public RouteLocator routeLocators(RouteLocatorBuilder builder) {
        // getToPostAndAddBody路由解释：
        // demo：curl http://localhost:5050/sayHello
        // 条件：路径为/sayHello并且是Get调用
        // 动作：Get转为Post；header里面添加contentType值为json；添加带name字段的json格式的body
        // 使用nacos注册中心负载均衡调用：.uri("lb://ww-user-base")) = .uri("http://localhost:7070"))
        return builder.routes()
                .route("getToPostAndAddBody", p -> p.path("/sayHello").and().method(HttpMethod.GET)
                        .filters(f -> f.modifyRequestBody(String.class, Map.class, MediaType.APPLICATION_JSON_VALUE, (exchange, s) -> {
                            Map<String, String> map = new HashMap<>();
                            map.put("name", "Get请求通过gateway转为Post请求【同时添加body】【body里面有name字段】");
                            return Mono.just(map);
                        }).filter(new ChangeMethodGatewayFilterFactory().apply(new ChangeMethodGatewayFilterFactory.Config(HttpMethod.POST))))
                        .uri("lb://ww-user-base"))
                .build();
    }
}
