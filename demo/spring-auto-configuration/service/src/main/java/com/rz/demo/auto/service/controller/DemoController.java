package com.rz.demo.auto.service.controller;

import com.rz.auto.tool.DogToolService;
import com.rz.demo.auto.service.qualifier.Demo;
import com.rz.demo.auto.tool.starter.CatToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Value("${server.port}")
    String port;

    // 来之starter的bean
    @Autowired
    CatToolService catToolService;

    // 来至enableTool标签的bean
    @Autowired
    DogToolService dogToolService;

    // 有歧义的bean（Demo被多实现），通过指定自定义Service的Qualifier名称注入的bean
    @Autowired
    @Qualifier("ADemoImpl666")
    Demo demo1;

    // 有歧义的bean（Demo被多实现），通过指定bean名称注入的bean
    @Autowired
    @Qualifier("BDemoImpl")
    Demo demo2;

    @RequestMapping("/")
    public String home() {
        return catToolService.getName() + "--" +
                dogToolService.getName() + "--" +
                demo1.getName() + "--" +
                demo2.getName() + "--port:" + port;
    }
}
