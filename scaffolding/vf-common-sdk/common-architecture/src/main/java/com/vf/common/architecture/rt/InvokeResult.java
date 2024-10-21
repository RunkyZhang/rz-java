package com.vf.common.architecture.rt;

import lombok.Data;

import java.io.Serializable;

@Data
public class InvokeResult implements Serializable, Comparable<InvokeResult> {
    private String apiPath;
    private String traceId;
    private long startPoint;
    private long duration;

    @Override
    public int compareTo(InvokeResult o) {
        return (int) (this.duration - o.getDuration());
    }
}
