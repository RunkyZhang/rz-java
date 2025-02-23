package com.ww.video.base.infrastructure.rpc;

import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.dto.SayHouHouByNameRequestDto;
import com.ww.user.base.api.service.DemoApi;
import com.ww.user.base.api.service.HouHouApi;
import com.ww.video.base.infrastructure.ConfigSource;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
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
        // 模拟偶发超时，50%几率超时。因为一个使用默认的feign配置的1000ms超时，一个使用自定义1500ms超时时间
        if (0 == System.currentTimeMillis() % 2) {
            log.warn("may time out for rpc");
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
