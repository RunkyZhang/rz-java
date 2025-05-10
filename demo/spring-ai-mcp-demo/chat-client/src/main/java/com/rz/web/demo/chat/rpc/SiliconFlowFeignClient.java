package com.rz.web.demo.chat.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "siliconFlow", url = "https://api.siliconflow.cn")
public interface SiliconFlowFeignClient {
    @PostMapping(value = "/v1/chat/completions")
    String chat(@RequestBody String name, @RequestHeader HttpHeaders headers);
}
