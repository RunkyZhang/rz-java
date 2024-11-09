package com.ww.user.base.api.service;

import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "用户示例接口")
@FeignClient("ww-user-base")
public interface DemoService {
    @ApiOperation(value = "说你好")
    @PostMapping("/sayHello")
    RpcResult<String> sayHelloByName(@RequestBody SayHelloByNameRequestDto requestDto);
}