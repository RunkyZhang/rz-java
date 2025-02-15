package com.ww.user.base.application.job;

import com.ww.common.base.annotation.AccessLog;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoJobHandler {
    @AccessLog(sampleRate = 1000, strategyName = "DefaultAccessLogStrategy", prefix = "job")
    @XxlJob("DemoJobHandler")
    public ReturnT<String> execute(String parameter) {
        return ReturnT.SUCCESS;
    }
}
