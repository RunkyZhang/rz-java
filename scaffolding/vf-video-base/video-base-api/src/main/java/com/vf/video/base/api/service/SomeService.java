package com.vf.video.base.api.service;

import com.vf.common.base.dto.RpcResult;

public interface SomeService {
    /**
     * 根据名字say hello
     *
     * @param name 名字
     * @return name + hello
     * telnet localhost 15511
     * invoke com.vf.video.base.api.service.SomeService.sayHelloByName("111")
     */
    RpcResult<String> sayHelloByName(String name);
}