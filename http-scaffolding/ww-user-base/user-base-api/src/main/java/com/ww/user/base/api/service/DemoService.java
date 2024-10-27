package com.ww.user.base.api.service;

import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;

public interface DemoService {
    /**
     * 根据名字say hello
     *
     * @return name + hello
     * telnet localhost 15511
     * invoke com.ww.user.base.api.service.DemoService.sayHelloByName({name:"111"})
     */
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto);
}