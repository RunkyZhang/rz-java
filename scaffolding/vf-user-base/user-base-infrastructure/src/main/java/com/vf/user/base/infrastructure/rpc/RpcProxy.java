package com.vf.user.base.infrastructure.rpc;

import com.vf.common.base.Helper;
import com.vf.common.base.annotation.AccessLog;
import com.vf.common.base.dto.RpcResult;
import com.vf.user.base.api.dto.SayHelloByNameRequestDto;
import com.vf.user.base.api.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class RpcProxy {
//    @DubboReference
//    private DemoService demoService;
//
//    @AccessLog(sampleRate = 20, strategyName = "DefaultAccessLogStrategy")
//    public String sayHello(String name) {
//        SayHelloByNameRequestDto requestDto = new SayHelloByNameRequestDto();
//        requestDto.setName(name);
//        RpcResult<String> result = demoService.sayHelloByName(requestDto);
//        return Helper.getResultData(result, true);
//    }


}
