package com.ww.common.architecture.log;

import lombok.Data;

@Data
public class AccessLogPolicy {
    private String url;
    private int sampleRate;
    private String strategyName;
}
