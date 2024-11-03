package com.ww.video.base.infrastructure.rpc;

import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.service.DemoService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class RpcProxy {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private DemoService demoService;

    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public RpcResult<String> sayHelloForRestTemplate(String name) {
        SayHelloByNameRequestDto requestDto = new SayHelloByNameRequestDto();
        requestDto.setName(name);
        return restTemplate.postForObject("http://ww-user-base/sayHello", requestDto, RpcResult.class);
    }

    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public RpcResult<String> sayHello(String name) {
        SayHelloByNameRequestDto requestDto = new SayHelloByNameRequestDto();
        requestDto.setName(name);
        return demoService.sayHelloByName(requestDto);
    }
}
