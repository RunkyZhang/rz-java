package com.ww.common.architecture.controller;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Service
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    @Resource
    private ControllerHandlerInterceptor interceptor;
    @Resource
    private CacheServletRequestResponseWrapperFilter filter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    /**
     * 配置过滤器
     */
    @Bean
    public FilterRegistrationBean<CacheServletRequestResponseWrapperFilter> addRequestBodyWrapperFilter() {
        FilterRegistrationBean<CacheServletRequestResponseWrapperFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(filter);
        bean.addUrlPatterns("/*"); // 拦截所有的资源
        //bean.addUrlPatterns(WebConstant.API + "/*"); // 拦截 API所有的资源
        bean.setOrder(1);
        bean.setAsyncSupported(true);
        return bean;
    }
}
