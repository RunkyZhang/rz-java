package com.ww.common.architecture.controller;

import com.ww.common.base.BusinessException;
import com.ww.common.base.dto.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {
    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private ControllerHandlerInterceptor controllerHandlerInterceptor;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RpcResult<Object> bindExceptionHandler(Exception e) {
        HandlerContext endpointHandlerContext = controllerHandlerInterceptor.contextThreadLocal.get();

        Long code = -1L;
        boolean errorLogLevel = false;
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            code = businessException.getCode();
            errorLogLevel = businessException.isErrorLogLevel();
        }

        if (errorLogLevel) {
            log.error("End-Failed({}) - SPEND TIME: {}; URL: {}; METHOD: {}; IP: {}; CODE:{}; ERROR: {}",
                    endpointHandlerContext.getStartTimePoint(),
                    System.currentTimeMillis() - endpointHandlerContext.getStartTimePoint() + " ms",
                    endpointHandlerContext.getRequestURL(),
                    endpointHandlerContext.getRequestMethod(),
                    code,
                    endpointHandlerContext.getRemoteAddress(),
                    e);
        } else {
            log.warn("End-Failed({}) - SPEND TIME: {}; URL: {}; METHOD: {}; IP: {}; CODE:{}; ERROR: {}",
                    endpointHandlerContext.getStartTimePoint(),
                    System.currentTimeMillis() - endpointHandlerContext.getStartTimePoint() + " ms",
                    endpointHandlerContext.getRequestURL(),
                    endpointHandlerContext.getRequestMethod(),
                    code,
                    endpointHandlerContext.getRemoteAddress(),
                    e);
        }

        return RpcResult.error(applicationName, code, e.getMessage());
    }
}