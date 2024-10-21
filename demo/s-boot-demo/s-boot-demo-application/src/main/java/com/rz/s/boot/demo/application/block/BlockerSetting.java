package com.rz.s.boot.demo.application.block;

import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
public class BlockerSetting {
    private int errorTimes;
    private int errorDuration;
    private int retryDuration;
    private Function<List<Object>, Object> failBack;
    private Function<List<Object>, Object> runner;
}
