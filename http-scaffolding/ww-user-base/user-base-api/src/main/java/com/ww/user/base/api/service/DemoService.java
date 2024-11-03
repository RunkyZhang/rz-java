package com.ww.user.base.api.service;

import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("ww-user-base")
public interface DemoService {
    @PostMapping("/sayHello")
    RpcResult<String> sayHelloByName(@RequestBody SayHelloByNameRequestDto requestDto);
}