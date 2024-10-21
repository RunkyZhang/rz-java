package com.rz.webdemo.api.webdemo.provider.dubbo;

import com.rz.webdemo.api.DemoService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHelloByName(String name) {
        return name+",hello!";
    }
}
