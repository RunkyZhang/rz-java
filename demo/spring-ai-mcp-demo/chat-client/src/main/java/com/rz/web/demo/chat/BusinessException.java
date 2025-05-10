package com.rz.web.demo.chat;

import java.util.Objects;

public class BusinessException extends RuntimeException {
    private boolean errorLogLevel = false;
    private final long code;
    private final String message;
    // 问题追踪消息，不展示给用户
    private final String traceMessage;

    public BusinessException(long code, String message) {
        this(code, message, message);
    }

    public BusinessException(long code, String message, String traceMessage) {
        this(code, message, traceMessage, null, false);
    }

    public BusinessException(long code, String message, boolean errorLogLevel) {
        this(code, message, message, errorLogLevel);
    }

    public BusinessException(long code, String message, String traceMessage, boolean errorLogLevel) {
        this(code, message, traceMessage, null, errorLogLevel);
    }

    public BusinessException(long code, String message, Throwable throwable) {
        this(code, message, message, throwable);
    }

    public BusinessException(long code, String message, String traceMessage, Throwable throwable) {
        this(code, message, traceMessage, throwable, false);
    }

    public BusinessException(long code, String message, Throwable throwable, boolean errorLogLevel) {
        this(code, message, message, throwable, errorLogLevel);
    }

    public BusinessException(long code, String message, String traceMessage, Throwable throwable, boolean errorLogLevel) {
        super(message, throwable);
        this.code = code;
        this.message = message;
        this.errorLogLevel = errorLogLevel;
        this.traceMessage = traceMessage;
    }

    public long getCode() {
        return this.code;
    }

    public String getMessage() {
        return Objects.isNull(this.message) ? super.getMessage() : this.message;
    }

    public String getTraceMessage() {
        return this.traceMessage;
    }

    public boolean isErrorLogLevel() {
        return errorLogLevel;
    }

    public static BusinessException FAILED_DESERIALIZATION_JSON(Throwable throwable, String className) {
        return new BusinessException(800006L, String.format("failed to deserialize json to object(%s)", className), throwable, true);
    }

    public static BusinessException FAILED_SERIALIZE_JSON(Throwable throwable, String className) {
        return new BusinessException(800007L, String.format("failed to serialize object(%s) to json", className), throwable, true);
    }

    public static BusinessException FAILED_INVOKE_RPC_API_WITH_RESULT(Long code, String message, String domain) {
        return new BusinessException(800012L, String.format("failed to invoke rpc api; result: code(%d); message(%s); domain(%s)", code, message, domain));
    }

    public static BusinessException FAILED_INVOKE_RPC_API_WITH_RESULT(String code, String message, String domain) {
        return new BusinessException(800012L, String.format("failed to invoke rpc api; result: code(%s); message(%s); domain(%s)", code, message, domain));
    }

    public static BusinessException API_FLOW_LIMITING(String source, String parameters, Throwable throwable) {
        return new BusinessException(800013L, String.format("api(%s) rate was limited; parameters(%s)", source, parameters), throwable, true);
    }

    public static BusinessException INVOKE_FLOW_BREAKING(String source, String parameters, Throwable throwable) {
        return new BusinessException(800014L, String.format("invoke(%s) was broken; parameters(%s)", source, parameters), throwable, true);
    }

    public static BusinessException INVALID_RABBITMQ_DELAY_TIME(int delayTime) {
        return new BusinessException(800015L, String.format("delayTime（%d） must be greater than 0", delayTime), true);
    }
}