package com.rz.s.boot.demo.common.exception;


public class BusinessException extends RuntimeException {
    private boolean errorLogLevel = false;

    public BusinessException(Long code, String message, Throwable e) {
        this(code, message, e, false);
    }

    public BusinessException(Long code, String message, Throwable e, boolean errorLogLevel) {
        this.errorLogLevel = errorLogLevel;
    }

    public boolean isErrorLogLevel() {
        return errorLogLevel;
    }


    public static BusinessException FAILED_DESERIALIZATION_JSON(Throwable throwable, String className) {
        return new BusinessException(800006L, String.format("failed to deserialize json to object(%s)", className), throwable);
    }

    public static BusinessException FAILED_SERIALIZE_JSON(Throwable throwable, String className) {
        return new BusinessException(800007L, String.format("failed to serialize object(%s) to json", className), throwable);
    }
}