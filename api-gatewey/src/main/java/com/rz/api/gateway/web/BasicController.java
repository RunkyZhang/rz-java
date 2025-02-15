/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rz.api.gateway.web;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.rz.api.gateway.config.RouteLocatorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RefreshScope
@Controller
public class BasicController {
    @Value("${useLocalCache:false}")
    private boolean useLocalCache;
    @Value("${userName:1111}")
    private String userName;
    @Value("${serverAddress:11。11。11。11}")
    private String serverAddress;
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosConfigAddress;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String nacosConfigNamespace;

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private RouteLocatorConfig routeLocatorConfig;

    @Resource
    private RouteLocator routeLocator;
    @Resource
    private RouteDefinitionLocator routeDefinitionLocator;
    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;
    @Resource
    private PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator;
    @Resource
    private RouteDefinitionRepository routeDefinitionRepository;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private RouteDefinitionRouteLocator routeDefinitionRouteLocator;

    private List<RouteLocator> routeLocators;

    @Resource
    private void setRouteLocators(List<RouteLocator> routeLocators){
        this.routeLocators  = routeLocators;
    }


    // http://127.0.0.1:5050?name=lisi
    @RequestMapping("")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) throws NacosException, URISyntaxException, NoSuchFieldException, IllegalAccessException {
        String dataId = "redis.properties";
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosConfigAddress);
        properties.put(PropertyKeyConst.NAMESPACE, nacosConfigNamespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String configContent = configService.getConfig(dataId, group, 5000);

//        RouteDefinition route1 = new RouteDefinition();
//        route1.setId("path_route1");
//        route1.setUri(new URI("https://www.taobao.com"));
//        route1.getPredicates().add(new PredicateDefinition("Path=/google"));
//        route1.getFilters().add(new FilterDefinition("StripPrefix=1"));
//
//        ConfigurableApplicationContext context = (ConfigurableApplicationContext) this.applicationContext;
//        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
//        //反射获取Factory中的singletonObjects 将该名称下的bean进行替换
//        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
//        singletonObjects.setAccessible(true);
//        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
//        RouteLocatorBuilder routeLocatorBuilder = (RouteLocatorBuilder) map.get("routeLocatorBuilder");
////        Object asdasd = this.applicationContext.getBean("routeLocators");
//
//        String[] beanNames = this.applicationContext.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            Object value = this.applicationContext.getBean(beanName);
//            if (value == routeLocators) {
//                System.out.println("asdasd");
//            }
//
//            System.out.println(beanName);
//        }
//
//        for (Object value : map.values()) {
//            if (value == routeLocators) {
//                System.out.println("asdasd");
//            }
//        }
//
//        for (RouteLocator locator : routeLocators) {
//            System.out.println(locator.toString());
//        }

//        Object routeLocatorBuilderLambda = map.get("asdasdasd");
//        for (String s : map.keySet()) {
//            System.out.println(s);
//        }
//
//        map.put("asdasdasd", routeLocatorConfig.newRouteLocators(routeLocatorBuilder));


//        routeDefinitionRouteLocator.getRoutes().collectList().subscribe(o -> {
//            System.out.println(o);
//        });
        //getToPostAndAddBody
        //path_route1
        //path_route2
        //path_route3

//        routeDefinitionRepository.delete(Mono.just("path_route1")).subscribe();
//
//        routeDefinitionWriter.delete(Mono.just("path_route1")).subscribe();


//        routeLocator.getRoutes().subscribe(o -> {
//            if ("path_route1".equals(o.getId())) {
//                System.out.println(o.hashCode());
//            }
//        });

//        ids.subscribe(id -> {
//            System.out.println(id);
//            //routeDefinitionWriter.delete(Mono.just(id)).subscribe();
//        });

        // 清空现有路由
//        routeLocator.getRoutes().collectList().block().forEach(route -> {
//            routeDefinitionWriter.delete(Mono.just(route.getId())).subscribe();
//        });

        // 重新加载路由配置
//        loadRoutesFromConfig();


        name += "===" + serverAddress + "---" + userName + "---" + useLocalCache;
        return "网关服务自定义接口： " + name + "---" + configContent;
    }


    private void loadRoutesFromConfig() throws URISyntaxException {
        // 假设从配置文件中加载路由配置
        // 这里仅作为示例，实际应用中需要根据实际情况加载配置
        RouteDefinition route1 = new RouteDefinition();
        route1.setId("path_route1");
        route1.setUri(new URI("https://www.taobao.com"));
        route1.getPredicates().add(new PredicateDefinition("Path=/google"));
        route1.getFilters().add(new FilterDefinition("StripPrefix=1"));


        routeDefinitionWriter.save(Mono.just(route1)).subscribe();
//        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
