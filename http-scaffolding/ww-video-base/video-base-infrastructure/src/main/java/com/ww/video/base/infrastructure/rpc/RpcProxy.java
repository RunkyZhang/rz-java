package com.ww.video.base.infrastructure.rpc;

import com.ww.common.base.annotation.AccessLog;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class RpcProxy {
    @Resource
    private RestTemplate restTemplate;

    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public String sayHello(String name) {
        return restTemplate.getForObject("http://ww-user-base/echo/" + name, String.class);

//        RpcResult<String> result = (RpcResult<String>)restTemplate.getForObject("http://ww-user-base/hello", RpcResult.class);
//
//        return name + result.toString();
    }
}
