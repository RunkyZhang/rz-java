package com.vf.common.architecture.log;

import lombok.Data;

@Data
public class AccessLogContext {
    private String url;
    private long startTimePoint;
    private String startLogMessage;
    private boolean shouldLog;
    private String traceId;
}
