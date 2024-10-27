package com.ww.video.base.infrastructure.rpc;

import com.ww.common.base.Helper;
import com.ww.common.base.annotation.AccessLog;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class RpcProxy {
    @DubboReference
    private DemoService demoService;

    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public String sayHello(String name) {
        SayHelloByNameRequestDto requestDto = new SayHelloByNameRequestDto();
        requestDto.setName(name);
        RpcResult<String> result = demoService.sayHelloByName(requestDto);
        return Helper.getResultData(result, true);
    }
}
