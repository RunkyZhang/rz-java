package com.ww.user.base.infrastructure.rpc;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ww.common.base.annotation.AccessLog;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RpcProxy {
    private Random random = new Random();

    @SentinelResource(value = "RpcProxy.getName", blockHandler = "getNameBroken", blockHandlerClass = InvokeFlowBreaking.class)
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public String getName(String name) {
        // 用jemter才能触发
        int value = 80 + random.nextInt(30);
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return name;
    }
}
