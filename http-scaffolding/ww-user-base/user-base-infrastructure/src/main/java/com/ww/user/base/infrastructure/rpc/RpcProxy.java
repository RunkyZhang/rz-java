package com.ww.user.base.infrastructure.rpc;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ww.common.base.annotation.AccessLog;
import org.springframework.stereotype.Service;

@Service
public class RpcProxy {
    @SentinelResource(value = "RpcProxy.getName", blockHandler = "getNameBroken", blockHandlerClass = InvokeFlowBreaking.class)
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy")
    public String getName(String name) {
        if (0 == System.currentTimeMillis() % 2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return name;
    }
}
