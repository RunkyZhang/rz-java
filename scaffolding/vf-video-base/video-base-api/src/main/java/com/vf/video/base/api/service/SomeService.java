package com.vf.video.base.api.service;

import com.vf.common.base.annotation.AccessLog;
import com.vf.common.base.dto.RpcResult;
import com.vf.video.base.api.dto.SayHelloByNameRequestDto;

public interface SomeService {
    /**
     * 根据名字say hello
     *
     * @return name + hello
     * telnet localhost 16622
     * invoke com.vf.video.base.api.service.SomeService.sayHelloByName({name:"111"})
     */
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto);
}