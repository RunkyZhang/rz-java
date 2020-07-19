package com.rz.demo.auto.service.listbean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

// ApplicationContext 可以list出所有bean
@Component
public class ListBeanApplicationContextAware implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        return ListBeanApplicationContextAware.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ListBeanApplicationContextAware.applicationContext = applicationContext;

        for (String beanDefinitionName : ListBeanApplicationContextAware.applicationContext.getBeanDefinitionNames()) {
            System.out.println("****bean: " + beanDefinitionName);
        }
    }
}
