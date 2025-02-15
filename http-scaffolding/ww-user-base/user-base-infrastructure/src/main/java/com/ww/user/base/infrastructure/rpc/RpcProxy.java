package com.ww.user.base.infrastructure.rpc;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHouHouByNameRequestDto;
import com.ww.user.base.api.service.HouHouApi;
import com.ww.user.base.infrastructure.ConfigSource;
import feign.Request;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class RpcProxy {
    private final Map<String, Request.Options> options = new HashMap<>();

    @Resource
    private ConfigSource configSource;
    @Resource
    private HouHouApiFeignClient houHouApiFeignClient;
    @Resource
    private HouHouApi houHouApi;

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

    @SentinelResource(value = "RpcProxy.getName", blockHandler = "getNameBroken", blockHandlerClass = InvokeFlowBreaking.class)
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public String getName(String name) {
        // 用jemter才能触发
        int value = 80 + ThreadLocalRandom.current().nextInt(30);
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return name;
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
