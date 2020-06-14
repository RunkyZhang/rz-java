package com.rz.demo.consumer1.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider")
public interface ProviderClient {
    @GetMapping("/")
    String getHome();
}
