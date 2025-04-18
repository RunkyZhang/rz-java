package com.ww.common.architecture.controller.aop;

import com.ww.common.architecture.log.AccessLogContext;
import com.ww.common.architecture.rt.RTMonitor;
import com.ww.common.base.BusinessException;
import com.ww.common.base.dto.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;

// http接口的aop。处理http接口调用期间抛出异常的日志
@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {
    @Resource
    private RTMonitor rtMonitor;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RpcResult<Object> bindExceptionHandler(Exception e) {
        // get accessLogContext
        AccessLogContext accessLogContext = ControllerHandlerInterceptor.accessLogContextThreadLocal.get();

        long currentTimeMillis = System.currentTimeMillis();
        long duration = currentTimeMillis - accessLogContext.getStartTimePoint();

        // RT monitor
        rtMonitor.push(accessLogContext.getRequestURL(),
                accessLogContext.getTraceId(),
                accessLogContext.getStartTimePoint(),
                duration,
                accessLogContext.getRemoteApplicationName());

        // retrieve start log
        String startLogMessage = StringUtils.isBlank(accessLogContext.getStartLogMessage()) ? String.format("Start(%d) - ???", currentTimeMillis) : accessLogContext.getStartLogMessage();
        if (!accessLogContext.isShouldLog()) {
            log.info(startLogMessage);
        }

        // error log
        Long code = -1L;
        boolean errorLogLevel = true;
        String exceptionMessage = e.getMessage();
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            code = businessException.getCode();
            errorLogLevel = businessException.isErrorLogLevel();
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            if (null != methodArgumentNotValidException.getBindingResult() && null != methodArgumentNotValidException.getBindingResult().getFieldError()) {
                exceptionMessage = methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage();
            }
        } else {
            exceptionMessage = "系统异常，请联系管理员。error: " + e.getMessage();
        }

        if (errorLogLevel) {
            log.error("Api-Failed({}) - spend: {}; url({}): {}; bucket: {}; localIp: {}; remoteIp({}): {}; CODE:{}; parameters: {}; ERROR:",
                    accessLogContext.getStartTimePoint(),
                    duration + " ms",
                    accessLogContext.getHttpMethod(),
                    accessLogContext.getRequestURL(),
                    rtMonitor.getBucket(duration),
                    accessLogContext.getLocalAddress(),
                    accessLogContext.getRemoteApplicationName(),
                    accessLogContext.getRemoteAddress(),
                    code,
                    accessLogContext.getParameters(),
                    e);
        } else {
            log.warn("Api-Failed({}) - spend: {}; url({}): {}; bucket: {}; localIp: {}; remoteIp({}): {}; CODE:{}; parameters: {}; ERROR:",
                    accessLogContext.getStartTimePoint(),
                    duration + " ms",
                    accessLogContext.getHttpMethod(),
                    accessLogContext.getRequestURL(),
                    rtMonitor.getBucket(duration),
                    accessLogContext.getLocalAddress(),
                    accessLogContext.getRemoteApplicationName(),
                    accessLogContext.getRemoteAddress(),
                    code,
                    accessLogContext.getParameters(),
                    e);
        }

        ControllerHandlerInterceptor.accessLogContextThreadLocal.remove();
        return RpcResult.error(accessLogContext.getLocalApplicationName(), code, exceptionMessage);
    }
}