package com.rz.s.boot.demo.interfaces.controller;

import com.rz.s.boot.demo.application.DemoService;
import com.rz.s.boot.demo.application.cache.LfuCache;
import com.rz.s.boot.demo.common.JacksonHelper;
import com.rz.s.boot.demo.common.dto.Box;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final ApplicationContext applicationContext;
    private final DemoService demoService;
    private final LfuCache lfuCache;

    @Value("${server.port}")
    String port;

    //http://localhost:8080
    @RequestMapping("/")
    public String home() {

//        long key = System.currentTimeMillis();
//        lfuCache.put(String.valueOf(key), String.valueOf(String.valueOf(key).hashCode()));

        Object result = demoService.add();
        return result.toString();

//        DemoService bean = applicationContext.getBean(DemoService.class);
//        bean.getTime(11111);
//        return "--port:" + port;
    }

    // http://localhost:8080/run/111/222
    @RequestMapping("/run/{name}/{age}")
    public String run(@PathVariable String name, @PathVariable int age) {
        Assert.notNull(name, "Assert.notNull: name");

        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("age", age);
        result.put("port", port);
        result.put("value", demoService.getTime(123456));
        Box box = new Box();
        box.setHigh(666);
        result.put("box", box.toString());

        return JacksonHelper.toJson(result, false);
    }
}
