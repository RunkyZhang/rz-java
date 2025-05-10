package com.rz.web.demo.chat.rpc;

import com.rz.web.demo.chat.dto.SfChatRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "siliconFlowStream", url = "https://api.siliconflow.cn")
public interface SiliconFlowStreamFeignClient {
    @PostMapping(value = "/v1/chat/completions")
    Flux<String> streamChat(@RequestBody SfChatRequestDto body, @RequestHeader HttpHeaders headers);
}
