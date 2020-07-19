package com.rz.auto.tool;

import org.springframework.beans.factory.FactoryBean;

public class DogToolFactoryBean implements FactoryBean<DogToolService> {
    @Override
    public DogToolService getObject() throws Exception {
        return new DogToolService("wang1mazi", 22);
    }

    @Override
    public Class<?> getObjectType() {
        return DogToolService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
