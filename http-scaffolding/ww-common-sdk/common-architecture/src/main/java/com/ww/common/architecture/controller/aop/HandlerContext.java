package com.ww.common.architecture.controller.aop;

import lombok.Data;

import java.io.Serializable;

@Data
public class HandlerContext implements Serializable {
    private long startTimePoint;
    private String requestURL;
    private String requestMethod;
    private String remoteAddress;
}
