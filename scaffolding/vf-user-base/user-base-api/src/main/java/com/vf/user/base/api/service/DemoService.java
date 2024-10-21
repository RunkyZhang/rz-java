package com.vf.user.base.api.service;

import com.vf.common.base.dto.RpcResult;
import com.vf.user.base.api.dto.SayHelloByNameRequestDto;

public interface DemoService {
    /**
     * 根据名字say hello
     *
     * @param name 名字
     * @return name + hello
     * telnet localhost 15511
     * invoke com.vf.user.base.api.service.DemoService.sayHelloByName("111")
     */
    RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto);
}