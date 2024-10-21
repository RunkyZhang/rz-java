package com.rz.s.boot.demo.application.block;

import lombok.Data;

@Data
public class FailedRecord {
    private long startPoint;
    private int errorCount;
    private long lastRetryPoint;
    private long successPoint;
    private boolean blocking = false;
}
