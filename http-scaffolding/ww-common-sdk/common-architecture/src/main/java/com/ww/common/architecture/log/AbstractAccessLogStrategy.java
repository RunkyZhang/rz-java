package com.ww.common.architecture.log;

import java.util.Random;

public abstract class AbstractAccessLogStrategy implements AccessLogStrategy {
    protected final Random random = new Random();
    protected final AccessLogPolicy accessLogPolicy;


    protected AbstractAccessLogStrategy(AccessLogPolicy accessLogPolicy) {
        if (null == accessLogPolicy) {
            throw new IllegalArgumentException("accessLogPolicy can not be null");
        }

        this.accessLogPolicy = accessLogPolicy;
    }

    @Override
    public boolean shouldLog() {
        if (AccessLogStrategySelector.emergencyEnable) {
            return true;
        }

        int value = random.nextInt(1000);
        if (value < accessLogPolicy.getSampleRate()) {
            return true;
        }

        return false;
    }

    public AccessLogPolicy getPolicy() {
        return accessLogPolicy;
    }
}
