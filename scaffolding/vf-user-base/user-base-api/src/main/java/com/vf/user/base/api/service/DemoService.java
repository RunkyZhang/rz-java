package com.vf.user.base.api.service;

import com.vf.common.base.annotation.AccessLog;
import com.vf.common.base.dto.RpcResult;
import com.vf.user.base.api.dto.SayHelloByNameRequestDto;

public interface DemoService {
    /**
     * 根据名字say hello
     *
     * @return name + hello
     * telnet localhost 15511
     * invoke com.vf.user.base.api.service.DemoService.sayHelloByName({name:"111"})
     */
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto);
}