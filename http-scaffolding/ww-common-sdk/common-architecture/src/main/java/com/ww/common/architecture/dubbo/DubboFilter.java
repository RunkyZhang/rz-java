package com.ww.common.architecture.dubbo;

import com.ww.common.architecture.log.AccessLogContext;
import com.ww.common.architecture.log.AccessLogStrategy;
import com.ww.common.base.BusinessException;
import com.ww.common.base.dto.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Objects;

@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = Integer.MAX_VALUE)
public class DubboFilter implements Filter, Filter.Listener {
    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (!appResponse.hasException()) {
            return;
        }

        // get accessLogContext
        RpcContext context = RpcContext.getContext();
        AccessLogContext accessLogContext = null == context.get("accessLogContext") ? new AccessLogContext() : (AccessLogContext) context.get("accessLogContext");
        String application = context.getAttachment("dubboApplication");
        // get parameters
        String parameters = formatParameters(invocation.getArguments());
        String apiPath = invoker.getInterface().getSimpleName() + "#" + invocation.getMethodName();
        long currentTimeMillis = System.currentTimeMillis();
        long duration = currentTimeMillis - accessLogContext.getStartTimePoint();

        // RT monitor
        DubboFilterProperties.rtMonitor.push(apiPath, accessLogContext.getTraceId(), accessLogContext.getStartTimePoint(), duration, application);

        // retrieve start log
        String startLogMessage = StringUtils.isBlank(accessLogContext.getStartLogMessage()) ? String.format("Start(%d) - ???", currentTimeMillis) : accessLogContext.getStartLogMessage();
        if (!accessLogContext.isShouldLog()) {
            log.info(startLogMessage);
        }

        // error log
        Throwable throwable = appResponse.getException();
        Long code = -1L;
        boolean errorLogLevel = true;
        if (throwable instanceof BusinessException) {
            BusinessException businessException = (BusinessException) throwable;
            code = businessException.getCode();
            errorLogLevel = businessException.isErrorLogLevel();
        }

        if (errorLogLevel) {
            log.error("End-Failed({}) - spend: {}; api: {}; bucket: {}; local: {}; remote({}): {}; CODE:{}; parameter: {}; ERROR: {}",
                    accessLogContext.getStartTimePoint(),
                    duration + " ms",
                    apiPath,
                    DubboFilterProperties.rtMonitor.getBucket(duration),
                    context.getLocalAddressString(),
                    application,
                    context.getRemoteAddressString(),
                    code,
                    parameters,
                    throwable);
        } else {
            log.warn("End-Failed({}) - spend: {}; api: {}; bucket: {}; local: {}; remote({}): {}; CODE:{}; parameter: {}; ERROR: {}",
                    accessLogContext.getStartTimePoint(),
                    duration + " ms",
                    apiPath,
                    DubboFilterProperties.rtMonitor.getBucket(duration),
                    context.getLocalAddressString(),
                    application,
                    context.getRemoteAddressString(),
                    code,
                    parameters,
                    throwable);
        }

        RpcResult result = RpcResult.error(DubboFilterProperties.applicationName, code, throwable.getMessage());
        appResponse.setValue(result);
        appResponse.setException(null);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long startTimePoint = System.currentTimeMillis();
        RpcContext context = RpcContext.getContext();
        // get parameters
        String parameters = formatParameters(invocation.getArguments());
        String traceId = "";
        String application = context.getAttachment("dubboApplication");

        // accessLogContext
        AccessLogContext accessLogContext = new AccessLogContext();
        context.set("accessLogContext", accessLogContext);
        accessLogContext.setTraceId(traceId);
        accessLogContext.setUrl(String.format("%s#%s", invoker.getInterface().getName(), invocation.getMethodName()));
        accessLogContext.setStartTimePoint(startTimePoint);
        accessLogContext.setStartLogMessage(String.format("Start(%d) - api: %s local: %s; remote(%s): %s; parameter: %s",
                startTimePoint,
                context.getMethodName(),
                context.getLocalAddressString(),
                application,
                context.getRemoteAddressString(),
                parameters));

        // should log
        AccessLogStrategy accessLogStrategy = DubboFilterProperties.accessLogStrategySelector.select(accessLogContext.getUrl());
        accessLogContext.setShouldLog(null == accessLogStrategy || accessLogStrategy.shouldLog());

        if (accessLogContext.isShouldLog()) {
            log.info(accessLogContext.getStartLogMessage());
        }

        Result result = invoker.invoke(invocation);
        Object value = result.getValue();

        if (value instanceof RpcResult) {
            ((RpcResult) value).setDomain(DubboFilterProperties.applicationName);
        }

        String apiPath = invoker.getInterface().getSimpleName() + "#" + invocation.getMethodName();
        long duration = System.currentTimeMillis() - startTimePoint;

        // RT monitor
        boolean isHighRT = DubboFilterProperties.rtMonitor.push(apiPath, traceId, startTimePoint, duration, application);

        // retrieve start log
        if (!accessLogContext.isShouldLog() && isHighRT) {
            log.info(accessLogContext.getStartLogMessage());
        }

        if (accessLogContext.isShouldLog() || isHighRT) {
            log.info("End({}) - spend: {}; api: {}; bucket: {}; local: {}; remote({}): {}; parameter: {}; result: {}",
                    startTimePoint,
                    duration + " ms",
                    apiPath,
                    DubboFilterProperties.rtMonitor.getBucket(duration),
                    context.getLocalAddressString(),
                    application,
                    context.getRemoteAddressString(),
                    parameters,
                    null == value ? "" : value.toString());
        }

        return result;
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
    }

    private String formatParameters(Object[] parameters) {
        if (null == parameters || 0 == parameters.length) {
            return StringUtils.EMPTY;
        }
        StringBuilder values = new StringBuilder("[");
        for (Object parameter : parameters) {
            String value = Objects.isNull(parameter) ? StringUtils.EMPTY : String.valueOf(parameter);
            values.append(value).append(",");
        }
        values.append("]");
        return values.toString();
    }
}
