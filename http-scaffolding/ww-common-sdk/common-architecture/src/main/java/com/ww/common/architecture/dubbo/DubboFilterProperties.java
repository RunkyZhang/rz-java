package com.ww.common.architecture.dubbo;

import com.ww.common.architecture.log.AccessLogStrategySelector;
import com.ww.common.architecture.rt.RTMonitor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Data
@Service
public class DubboFilterProperties {
    public static AccessLogStrategySelector accessLogStrategySelector;
    public static RTMonitor rtMonitor;
    public static String applicationName;

    @Value("${spring.application.name:}")
    public void setApplicationName(String applicationName) {
        DubboFilterProperties.applicationName = applicationName;
    }

    @Resource
    public void setAccessLogStrategySelector(AccessLogStrategySelector accessLogStrategySelector) {
        DubboFilterProperties.accessLogStrategySelector = accessLogStrategySelector;
    }

    @Resource
    public void setRTMonitor(RTMonitor rtMonitor) {
        DubboFilterProperties.rtMonitor = rtMonitor;
    }
}
