package com.rz.demo.consumer2.cleint;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider")
public interface ProviderClient {
    @GetMapping("/")
    String getHome();
}
