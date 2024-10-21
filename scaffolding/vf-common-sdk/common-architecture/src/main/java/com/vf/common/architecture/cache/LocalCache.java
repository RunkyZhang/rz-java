package com.vf.common.architecture.cache;

import org.aspectj.lang.ProceedingJoinPoint;

public class LocalCache {
    private final String key;
    private Object value;
    private long refreshTime;
    private int errorCount;
    private ProceedingJoinPoint proceedingJoinPoint;

    public LocalCache(String key) {
        this.key = key;
    }

    public void refresh() throws Throwable {
        try {
            this.value = this.proceedingJoinPoint.proceed();
            this.refreshTime = System.currentTimeMillis();
            this.errorCount = 0;
        } catch (Throwable throwable) {
            this.errorCount++;
            throw throwable;
        }
    }

    public void expire() {
        this.refreshTime = -1;
    }

    public void fuse() {
        this.refreshTime = System.currentTimeMillis();
        this.errorCount = 0;
    }

    public void setProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    public String getKey() {
        return key;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public Object getValue() {
        return value;
    }

    public int getErrorCount() {
        return errorCount;
    }
}
