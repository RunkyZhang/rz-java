package com.rz.web.demo.chat.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "server", url = "http://localhost:8080")
public interface ServerFeignClient {
    @GetMapping(value = "/hello")
    String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name);
}
