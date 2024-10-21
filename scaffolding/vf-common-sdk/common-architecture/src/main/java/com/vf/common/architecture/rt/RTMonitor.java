package com.vf.common.architecture.rt;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.vf.common.base.DateTimeUtility;
import com.vf.common.base.JacksonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
public class RTMonitor {
    private final Map<String, PriorityBlockingQueue<InvokeResult>> invokeResultsByApiPath = new HashMap<>();
    private final Map<String, Set<String>> invokerNamesByApiPath = new HashMap<>();
    private ThreadPoolExecutor threadPoolExecutor;
    private int today = DateTimeUtility.getDay(new Date());
    private final int[] bucketSettings = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};

    @Value("${rt.monitor.master.switch:false}")
    private boolean masterSwitch = false;
    @Value("${rt.monitor.pool.size:3}")
    private int poolSize = 3;
    // 50万
    @Value("${rt.monitor.queue.max.size:500000}")
    private int queueMaxSize = 500000;
    @Value("${rt.monitor.invoke.result.size:100}")
    private int invokeResultSize = 100;

    @PostConstruct
    private void init() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("RTMonitor-ThreadPool-%d").build();
        threadPoolExecutor = new ThreadPoolExecutor(
                poolSize,
                poolSize,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueMaxSize),
                namedThreadFactory);
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
    }

    public boolean push(String apiPath, String traceId, long startPoint, long duration, String invokerName) {
        if (!masterSwitch) {
            return false;
        }

        try {
            // show by day, and clear
            int day = DateTimeUtility.getDay(new Date());
            if (today != day) {
                today = day;
                log.info("RTMonitor: {}", JacksonHelper.toJson(invokeResultsByApiPath, false));
                for (PriorityBlockingQueue<InvokeResult> invokeResults : invokeResultsByApiPath.values()) {
                    invokeResults.clear();
                }
            }

            // remote
            Set<String> invokerNames = invokerNamesByApiPath.computeIfAbsent(apiPath, o -> new HashSet<>());
            if (!invokerNames.contains(invokerName)) {
                invokerNames.add(invokerName);
            }

            // rt
            PriorityBlockingQueue<InvokeResult> invokeResults = invokeResultsByApiPath.get(apiPath);
            if (null == invokeResults) {
                invokeResults = new PriorityBlockingQueue<>();
                invokeResultsByApiPath.put(apiPath, invokeResults);
            }

            InvokeResult minInvokeResult = invokeResults.peek();
            if (invokeResultSize <= invokeResults.size() && null != minInvokeResult && minInvokeResult.getDuration() > duration) {
                return false;
            }

            InvokeResult invokeResult = new InvokeResult();
            invokeResult.setApiPath(apiPath);
            invokeResult.setTraceId(traceId);
            invokeResult.setStartPoint(startPoint);
            invokeResult.setDuration(duration);
            this.execute(invokeResults, invokeResult);

            return true;
        } catch (Throwable throwable) {
            log.warn("I have no idea in RTMonitor-push; error: ", throwable);
            return false;
        }
    }

    public String getBucket(long duration) {
        int start = 0;
        int end = -1;
        for (int bucketSetting : this.bucketSettings) {
            if (duration < bucketSetting) {
                end = bucketSetting;
                break;
            } else {
                start = bucketSetting;
            }
        }

        return String.format("%dxxx%d", start, end);
    }

    public Map<String, PriorityBlockingQueue<InvokeResult>> show() {
        return invokeResultsByApiPath;
    }

    public int getQueueSize() {
        return threadPoolExecutor.getQueue().size();
    }

    private void execute(PriorityBlockingQueue<InvokeResult> invokeResults, InvokeResult invokeResult) {
        threadPoolExecutor.execute(() -> {
            try {
                if (invokeResultSize > invokeResults.size()) {
                    invokeResults.add(invokeResult);
                } else {
                    invokeResults.poll();
                    invokeResults.add(invokeResult);
                }
            } catch (Throwable throwable) {
                log.warn("I have no idea in RTMonitor-execute; error: ", throwable);
            }
        });
    }
}
