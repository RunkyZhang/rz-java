package com.ww.user.base.infrastructure.rpc;

import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class RpcProxy {
//    @Resource
//    private RestTemplate restTemplate;
//
//    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
//    public String sayHello(String name) {
//        RpcResult<String> result = (RpcResult<String>) restTemplate.getForObject("http://ww-user-base/hello?name=" + name, RpcResult.class);
//        return name + result;
//    }
}
