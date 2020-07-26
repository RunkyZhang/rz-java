package com.rz.demo.startup.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Value("${server.port}")
    String port;

    @RequestMapping("/")
    public String home() {
        return "--port:" + port;
    }
}
