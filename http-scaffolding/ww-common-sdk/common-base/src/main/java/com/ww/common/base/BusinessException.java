package com.ww.common.base;

import java.util.Objects;

public class BusinessException extends RuntimeException {
    private boolean errorLogLevel = false;
    private final long code;
    private final String message;

    public BusinessException(long code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.errorLogLevel = false;
    }

    public BusinessException(long code, String message, boolean errorLogLevel) {
        super(message);
        this.code = code;
        this.message = message;
        this.errorLogLevel = errorLogLevel;
    }

    public BusinessException(long code, String message, Throwable throwable) {
        super(message);
        this.code = code;
        this.message = message;
        this.errorLogLevel = false;
    }

    public BusinessException(long code, String message, Throwable throwable, boolean errorLogLevel) {
        super(message, throwable);
        this.code = code;
        this.message = message;
        this.errorLogLevel = errorLogLevel;
    }

    public Long getCode() {
        return this.code;
    }

    public String getMessage() {
        return Objects.isNull(this.message) ? super.getMessage() : this.message;
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
        return new BusinessException(800012L, String.format("failed to invoke rpc api; result: code(%d); message(%s); domain(%s)", code, message, domain), null);
    }

    public static BusinessException API_FLOW_LIMITING(String source, String parameters, Throwable throwable) {
        return new BusinessException(800013L, String.format("api(%s) rate was limited; parameters(%s)", source, parameters), throwable, true);
    }

    public static BusinessException INVOKE_FLOW_BREAKING(String source, String parameters, Throwable throwable) {
        return new BusinessException(800014L, String.format("invoke(%s) was broken; parameters(%s)", source, parameters), throwable, true);
    }
}