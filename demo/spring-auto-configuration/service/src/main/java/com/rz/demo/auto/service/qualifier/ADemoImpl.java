package com.rz.demo.auto.service.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier("ADemoImpl666")
@Component
public class ADemoImpl implements Demo {
    @Override
    public String getName() {
        return "ADemoImpl";
    }
}
