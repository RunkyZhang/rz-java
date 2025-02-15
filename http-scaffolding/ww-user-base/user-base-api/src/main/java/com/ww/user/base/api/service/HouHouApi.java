package com.ww.user.base.api.service;

import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.dto.SayHouHouByNameRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "HouHou示例接口")
@FeignClient("ww-user-base")
public interface HouHouApi {
    @ApiOperation(value = "说HouHou")
    @PostMapping("/houhou")
    RpcResult<String> sayHouHouByName(@RequestBody SayHouHouByNameRequestDto requestDto);
}