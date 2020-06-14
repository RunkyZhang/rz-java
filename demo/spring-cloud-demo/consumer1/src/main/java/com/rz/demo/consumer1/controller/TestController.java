package com.rz.demo.consumer1.controller;

import com.rz.demo.consumer1.client.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProviderClient providerClient;

    @Value("${server.port}")
    String port;

    @RequestMapping("/")
    public String home() {
        String result = "";
        result += "ribbon---" + restTemplate.getForEntity("http://provider/", String.class).getBody() + "---" + port;
        result += "<br/>";
        result += "feign---" + providerClient.getHome() + "---" + port + "\n\t";

        return result;
    }
}
