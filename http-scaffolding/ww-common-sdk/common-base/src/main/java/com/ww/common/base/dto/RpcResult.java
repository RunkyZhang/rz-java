package com.ww.common.base.dto;

import com.ww.common.base.BusinessException;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class RpcResult<T> implements Serializable {
    private static final long serialVersionUID = -1L;
    public static final long SUCCESS_CODE = 200L;
    public static final String DEFAULT_SUCCESS_MESSAGE = "success";

    private String domain;
    private long code;
    private String message;
    private T data;
    private Map<String, String> extra;
    private List<BusinessException> exceptions;

    public RpcResult() {
        this.setCode(SUCCESS_CODE);
        this.setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public RpcResult(T data) {
        this.setData(data);
        this.setCode(SUCCESS_CODE);
        this.setMessage("success");
    }

    public RpcResult(T data, Map<String, String> extra) {
        this(SUCCESS_CODE, data, extra);
    }

    public RpcResult(long code) {
        this.code = code;
    }

    public RpcResult(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public RpcResult(long code, T data) {
        this(code, data, null);
    }

    public RpcResult(long code, T data, Map<String, String> extra) {
        this((String)null, code, (String)null, data, extra);
    }

    public RpcResult(String domain, long code) {
        this(domain, code, null);
    }

    public RpcResult(String domain, long code, String message) {
        this(domain, code, message, null);
    }

    public RpcResult(String domain, long code, String message, T data) {
        this(domain, code, message, data, null);
    }

    public RpcResult(String domain, long code, String message, T data, Map<String, String> extra) {
        this.domain = domain;
        this.code = code;
        this.message = message;
        this.data = data;
        this.extra = extra;
    }

    public static <T> RpcResult<T> success() {
        return new RpcResult();
    }

    public static <T> RpcResult<T> success(T data) {
        return new RpcResult(data);
    }

    public static <T> RpcResult<T> error(long code) {
        RpcResult<T> result = new RpcResult<>(code);
        result.setDomain(null);
        return result;
    }

    public static <T> RpcResult<T> error(long code, String message) {
        RpcResult<T> result = new RpcResult<>(code, message);
        result.setDomain(null);
        return result;
    }

    public static <T> RpcResult<T> error(String domain, long code) {
        return new RpcResult<>(domain, code);
    }

    public static <T> RpcResult<T> error(String domain, long code, String message) {
        return new RpcResult<>(domain, code, message);
    }

    public static <T> boolean isSuccess(RpcResult<T> result) {
        return null != result && SUCCESS_CODE == result.getCode();
    }

    public void addError(long code, String message) {
        if (this.exceptions == null) {
            this.exceptions = new ArrayList<>();
        }

        this.exceptions.add(new BusinessException(code, message));
    }

    public void addError(BusinessException businessException) {
        if (null == businessException) {
            return;
        }

        this.exceptions.add(businessException);
    }
}
