package com.ww.common.architecture.log;

import lombok.Data;

@Data
public class AccessLogContext {
    private String traceId;
    private String spanId;

    private String requestURL;
    private String httpMethod;
    private String localAddress;
    private String localApplicationName;
    private String remoteAddress;
    private String remoteApplicationName;
    private String parameters;

    private long startTimePoint;
    private String startLogMessage;
    private boolean shouldLog;
}
