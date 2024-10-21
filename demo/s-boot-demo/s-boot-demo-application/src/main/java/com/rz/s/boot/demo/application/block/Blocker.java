package com.rz.s.boot.demo.application.block;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Blocker {
    private final Map<BlockerSetting, FailedRecord> failedRecords = new HashMap<>();

    public void addSetting(BlockerSetting setting) {
        Assert.notNull(setting, "setting");
        // 检查setting参数

        if (!failedRecords.containsKey(setting)) {
            failedRecords.put(setting, new FailedRecord());
        }
    }

    public Object run(BlockerSetting setting, List<Object> parameters) {
        Assert.notNull(setting, "setting");

        if (!failedRecords.containsKey(setting)) {
            return setting.getRunner().apply(parameters);
        }

        Object result = null;
        FailedRecord failedRecord = failedRecords.get(setting);
        if (0 < failedRecord.getStartPoint()) {
            long duration;
            if (0 == failedRecord.getLastRetryPoint()) {
                duration = System.currentTimeMillis() - failedRecord.getStartPoint();
            } else {
                duration = System.currentTimeMillis() - failedRecord.getLastRetryPoint();
            }
            if (duration >= setting.getRetryDuration()) {
                failedRecord.setLastRetryPoint(System.currentTimeMillis());

                try {
                    result = setting.getRunner().apply(parameters);
                } catch (Exception e) {
                    // TODO: 并发问题使用Atomic
                    failedRecord.setErrorCount(failedRecord.getErrorCount() + 1);

                    throw e;
                }

                failedRecord.setSuccessPoint(System.currentTimeMillis());
                this.reset(failedRecord);
                return result;
            }

            if ((setting.getErrorDuration() > System.currentTimeMillis() - failedRecord.getStartPoint() &&
                    setting.getErrorTimes() < failedRecord.getErrorCount()) || failedRecord.isBlocking())  {
                failedRecord.setBlocking(true);
                return setting.getFailBack().apply(parameters);
            }
        }

        try {
            result = setting.getRunner().apply(parameters);
        } catch (Exception e) {
            if (0 == failedRecord.getStartPoint()) {
                failedRecord.setStartPoint(System.currentTimeMillis());
                // TODO: 并发问题使用Atomic
                failedRecord.setErrorCount(1);
            } else {
                // TODO: 并发问题使用Atomic
                failedRecord.setErrorCount(failedRecord.getErrorCount() + 1);
            }

            throw e;
        }

        return result;
    }

    private void reset(FailedRecord failedRecord) {
        failedRecord.setLastRetryPoint(0);
        failedRecord.setErrorCount(0);
        failedRecord.setStartPoint(0);
        failedRecord.setBlocking(false);
    }
}
