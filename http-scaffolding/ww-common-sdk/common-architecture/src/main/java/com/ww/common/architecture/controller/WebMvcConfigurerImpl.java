package com.ww.common.architecture.controller;

import com.ww.common.architecture.controller.aop.ControllerHandlerInterceptor;
import com.ww.common.architecture.controller.body.RequestResponseWrapperFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

// 注入aop和配置filter
@Service
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    @Resource
    private ControllerHandlerInterceptor controllerHandlerInterceptor;
    @Resource
    private RequestResponseWrapperFilter requestResponseWrapperFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controllerHandlerInterceptor);
    }

    /**
     * 配置过滤器
     */
    @Bean
    public FilterRegistrationBean<RequestResponseWrapperFilter> addRequestBodyWrapperFilter() {
        FilterRegistrationBean<RequestResponseWrapperFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(requestResponseWrapperFilter);
        bean.addUrlPatterns("/*"); // 拦截所有的资源
        bean.setOrder(1);
        bean.setAsyncSupported(true);
        return bean;
    }
}
