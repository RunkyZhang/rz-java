package com.rz.demo.auto.service.qualifier;

import org.springframework.stereotype.Component;

@Component
public class BDemoImpl implements Demo {
    @Override
    public String getName() {
        return "BDemoImpl";
    }
}
