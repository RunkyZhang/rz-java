package com.ww.common.architecture.log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ww.common.base.JacksonHelper;
import com.ww.common.base.annotation.AccessLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

// ark > annotation > default
@Slf4j
public class AccessLogStrategySelector {
    public static boolean emergencyEnable = false;
    private final Map<String, AbstractAccessLogStrategy> accessLogStrategies = new HashMap<>();
    private static final Map<String, Function<AccessLogPolicy, AbstractAccessLogStrategy>> accessLogStrategyBuilders = new HashMap<>();
    private final Set<String> ignoreMethods = new HashSet<>(Arrays.asList("hashCode", "getClass", "equals", "toString", "notify", "wait", "notifyAll"));
    private String logPoliciesJson;
    private String lastLogPoliciesJson;

    @Value("${accessLog.defaultSampleRate:10}")
    private int defaultSampleRate;

    @Value("${accessLog.emergencyEnable:false}")
    private void setEmergencyEnable(boolean emergencyEnable) {
        AccessLogStrategySelector.emergencyEnable = emergencyEnable;
    }

    @Value("${accessLog.policies.json:}")
    private void setLogPoliciesJson(String logPoliciesJson) {
        this.logPoliciesJson = logPoliciesJson;
        refresh();
    }

    public AccessLogStrategySelector(Set<Class<?>> classes) {
        Assert.notEmpty(classes, "Assert.notEmpty: classes");
        accessLogStrategyBuilders.put(DefaultAccessLogStrategy.class.getSimpleName().toLowerCase(), DefaultAccessLogStrategy::new);

        scan(classes);

        lastLogPoliciesJson = "";
        refresh();
    }

    public AccessLogStrategy select(String url) {
        return accessLogStrategies.get(url);
    }

    public Map<String, AbstractAccessLogStrategy> getAll() {
        return accessLogStrategies;
    }

    private void scan(Set<Class<?>> beanClasses) {
        for (Class<?> clazz : beanClasses) {
            if (null == clazz) {
                continue;
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (ignoreMethods.contains(method.getName())) {
                    continue;
                }

                // AccessLogPolicy
                AccessLogPolicy accessLogPolicy = new AccessLogPolicy();
                accessLogPolicy.setUrl(String.format("%s#%s", clazz.getName(), method.getName()));

                // AccessLog
                AccessLog accessLog = method.getAnnotation(AccessLog.class);
                accessLogPolicy.setSampleRate(null == accessLog ? defaultSampleRate : accessLog.sampleRate());
                accessLogPolicy.setStrategyName(null == accessLog ? null : accessLog.strategyName());

                // AbstractAccessLogStrategy
                AbstractAccessLogStrategy accessLogStrategy;
                Function<AccessLogPolicy, AbstractAccessLogStrategy> accessLogStrategyBuilder;
                if (null == accessLogPolicy.getStrategyName() ||
                        null == (accessLogStrategyBuilder = accessLogStrategyBuilders.get(accessLogPolicy.getStrategyName().toLowerCase()))) {
                    accessLogStrategy = new DefaultAccessLogStrategy(accessLogPolicy);
                } else {
                    accessLogStrategy = accessLogStrategyBuilder.apply(accessLogPolicy);
                }

                accessLogStrategies.put(accessLogPolicy.getUrl(), accessLogStrategy);
            }
        }
    }

    private void refresh() {
        if (StringUtils.isBlank(logPoliciesJson) || StringUtils.equals(lastLogPoliciesJson, logPoliciesJson)) {
            return;
        }
        lastLogPoliciesJson = logPoliciesJson;

        List<AccessLogPolicy> accessLogPolicies = JacksonHelper.toObj(logPoliciesJson, new TypeReference<List<AccessLogPolicy>>() {
        }, false);
        if (null == accessLogPolicies) {
            return;
        }

        for (AccessLogPolicy accessLogPolicy : accessLogPolicies) {
            if (null == accessLogPolicy || null == accessLogStrategies.get(accessLogPolicy.getUrl())) {
                continue;
            }

            // AbstractAccessLogStrategy
            Function<AccessLogPolicy, AbstractAccessLogStrategy> accessLogStrategyBuilder;
            if (null == accessLogPolicy.getStrategyName() ||
                    null == (accessLogStrategyBuilder = accessLogStrategyBuilders.get(accessLogPolicy.getStrategyName().toLowerCase()))) {
                log.warn("invalid accessLogStrategy name({})", accessLogPolicy.getStrategyName());
                return;
            }

            accessLogStrategies.put(accessLogPolicy.getUrl(), accessLogStrategyBuilder.apply(accessLogPolicy));
        }
    }
}
