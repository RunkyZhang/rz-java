package com.ww.user.base.infrastructure.rpc;

import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHouHouByNameRequestDto;
import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient("ww-user-base")
public interface HouHouApiFeignClient {
    @PostMapping({"/houhou"})
    RpcResult<String> sayHouHouByName(@RequestBody SayHouHouByNameRequestDto RequestDto, Request.Options options);
}