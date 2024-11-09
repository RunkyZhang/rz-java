package com.ww.video.base.api.service;

import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import com.ww.video.base.api.dto.SayHelloByNameRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@Api(tags = "视频示例接口")
@FeignClient("ww-video-base")
public interface SomeService {
    /**
     * 根据名字say hello
     *
     * @return name + hello
     * telnet localhost 16622
     * invoke com.ww.video.base.api.service.SomeService.sayHelloByName({name:"111"})
     */
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    @ApiOperation(value = "说你好")
    @PostMapping("/sayHello")
    RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto);
}