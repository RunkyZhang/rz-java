//package com.rz.api.gateway;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.ww.common.base.JacksonHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
//import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import reactor.core.publisher.Mono;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Slf4j
//public class RouteLoader {
//    @Resource
//    private RouteDefinitionWriter routeDefinitionWriter;
//    @Resource
//    private ApplicationEventPublisher applicationEventPublisher;
//    @Resource
//    private PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator;
//
//
//    private static final List<String> routeList = new ArrayList<>();
//
//    /**
//     * 清理集合中的所有路由，并清空集合
//     */
//    private void clear() {
//        // 全部调用API清理掉
//        routeList.forEach(id -> routeDefinitionWriter.delete(Mono.just(id)).subscribe());
//        // 清空集合
//        routeList.clear();
//    }
//
//    /**
//     * 新增路由
//     *
//     * @param routeDefinitions
//     */
//    private void add(List<RouteDefinition> routeDefinitions) {
//
//        try {
//            routeDefinitions.forEach(routeDefinition -> {
//                routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
//                routeList.add(routeDefinition.getId());
//            });
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    /**
//     * 发布进程内通知，更新路由
//     */
//    private void publish() {
//        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));
//    }
//
//    /**
//     * 更新所有路由信息
//     *
//     * @param configStr
//     */
//    public void refreshAll(String configStr) {
//        log.info("start refreshAll : {}", configStr);
//        // 无效字符串不处理
//        if (!StringUtils.hasText(configStr)) {
//            log.error("invalid string for route config");
//            return;
//        }
//
//        List<RouteDefinition> routeDefinitions = JacksonHelper.toObj(configStr, new TypeReference<List<RouteDefinition>>() {}, false);
//        if (null == routeDefinitions) {
//            return;
//        }
//
//        // 清理掉当前所有路由
//        clear();
//
//        // 添加最新路由
//        add(routeDefinitions);
//
//        // 通过应用内消息的方式发布
//        publish();
//
//        log.info("finish refreshAll");
//    }
//}
