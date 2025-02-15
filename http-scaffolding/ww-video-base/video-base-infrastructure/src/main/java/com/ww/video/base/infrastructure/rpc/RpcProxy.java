package com.ww.video.base.infrastructure.rpc;

import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.dto.SayHouHouByNameRequestDto;
import com.ww.user.base.api.service.DemoApi;
import com.ww.user.base.api.service.HouHouApi;
import com.ww.video.base.infrastructure.ConfigSource;
import feign.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RpcProxy {
    private final Map<String, Request.Options> options = new HashMap<>();

    @Resource
    private ConfigSource configSource;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private DemoApi demoApi;
    @Resource
    private HouHouApiFeignClient houHouApiFeignClient;
    @Resource
    private HouHouApi houHouApi;

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
        return demoApi.sayHelloByName(requestDto);
    }

    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public RpcResult<String> sayHouHou(String name) {
        SayHouHouByNameRequestDto requestDto = new SayHouHouByNameRequestDto();
        requestDto.setName(name);
        if (0 == System.currentTimeMillis() % 2) {
            return houHouApi.sayHouHouByName(requestDto);
        } else {
            return houHouApiFeignClient.sayHouHouByName(requestDto, buildOptions(1500));
        }
    }

    protected Request.Options buildOptions(long readTimeoutMillis) {
        String key = configSource.getFeignClientConnectTimeout() + "+" + readTimeoutMillis;
        if (this.options.containsKey(key)) {
            return this.options.get(key);
        }

        Request.Options options = new Request.Options(configSource.getFeignClientConnectTimeout(), TimeUnit.MILLISECONDS, readTimeoutMillis, TimeUnit.MILLISECONDS, true);
        this.options.put(key, options);
        return options;
    }
}
