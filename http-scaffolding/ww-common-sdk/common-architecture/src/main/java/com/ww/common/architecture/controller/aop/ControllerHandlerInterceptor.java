package com.ww.common.architecture.controller.aop;

import com.ww.common.architecture.controller.body.CachingHttpServletRequestWrapper;
import com.ww.common.architecture.controller.body.CopyingHttpServletResponseWrapper;
import com.ww.common.architecture.log.AccessLogContext;
import com.ww.common.architecture.log.AccessLogStrategy;
import com.ww.common.architecture.log.AccessLogStrategySelector;
import com.ww.common.architecture.rt.RTMonitor;
import com.ww.common.base.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// http接口的aop。处理http接口调用开始和结束日志。不包括异常情况
@Slf4j
@Service
public class ControllerHandlerInterceptor implements HandlerInterceptor {
    public ThreadLocal<AccessLogContext> accessLogContextThreadLocal = new ThreadLocal<>();

    @Resource
    private AccessLogStrategySelector accessLogStrategySelector;
    @Resource
    private RTMonitor rtMonitor;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AccessLogContext context = new AccessLogContext();
        accessLogContextThreadLocal.set(context);

        context.setTraceId(request.getHeader("ww-trace-id"));
        context.setSpanId("s_" + UUID.randomUUID().toString());

        context.setRequestURL(request.getRequestURL().toString());
        context.setHttpMethod(request.getMethod());
        context.setRemoteAddress(request.getRemoteAddr());
        context.setRemoteApplicationName(request.getHeader("ww-application-name"));
        context.setLocalAddress(Helper.getIpV4());
        context.setLocalApplicationName(applicationName);
        context.setParameters(this.formatParameters(request));

        context.setStartTimePoint(System.currentTimeMillis());
        context.setStartLogMessage(String.format("Api-Start(%d) - url(%s): %s localIp: %s; remoteIp(%s): %s; parameters: %s",
                context.getStartTimePoint(),
                context.getHttpMethod(),
                context.getRequestURL(),
                context.getLocalAddress(),
                context.getRemoteApplicationName(),
                context.getRemoteAddress(),
                context.getParameters()));

        // should log
        AccessLogStrategy accessLogStrategy = accessLogStrategySelector.select(context.getRequestURL());
        context.setShouldLog(null == accessLogStrategy || accessLogStrategy.shouldLog());
        if (context.isShouldLog()) {
            log.info(context.getStartLogMessage());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        AccessLogContext accessLogContext = accessLogContextThreadLocal.get();

        long duration = System.currentTimeMillis() - accessLogContext.getStartTimePoint();
        // RT monitor
        boolean isHighRT = rtMonitor.push(accessLogContext.getRequestURL(),
                accessLogContext.getTraceId(),
                accessLogContext.getStartTimePoint(),
                duration,
                accessLogContext.getRemoteApplicationName());

        // retrieve start log
        if (!accessLogContext.isShouldLog() && isHighRT) {
            log.info(accessLogContext.getStartLogMessage());
        }

        if (accessLogContext.isShouldLog() || isHighRT) {
            log.info("Api-End({}) - spend: {}; url({}-{}): {}; bucket: {}; localIp: {}; remoteIp({}): {}; parameter: {}; result: {}",
                    accessLogContext.getStartTimePoint(),
                    duration + " ms",
                    accessLogContext.getHttpMethod(),
                    response.getStatus(),
                    accessLogContext.getRequestURL(),
                    rtMonitor.getBucket(duration),
                    accessLogContext.getLocalAddress(),
                    accessLogContext.getRemoteApplicationName(),
                    accessLogContext.getRemoteAddress(),
                    accessLogContext.getParameters(),
                    this.formatResponseBody(response));
        }

        accessLogContextThreadLocal.remove();
    }

    private String formatParameters(HttpServletRequest request) {
        List<String> parameters = new ArrayList<>();

        if (!CollectionUtils.isEmpty(request.getParameterMap())) {
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                if (null == entry || StringUtils.isBlank(entry.getKey())) {
                    continue;
                }

                String parameter = String.format(
                        "%s: %s", entry.getKey(), null == entry.getValue() ? "" : String.join("|", entry.getValue()));
                parameters.add(parameter);
            }
        }
        parameters.add("ww-body: " + this.formatRequestBody(request));

        return "[" + String.join(",", parameters) + "]";
    }

    private String formatRequestBody(HttpServletRequest request) {
        String body = String.format("length: %d; charset: %s; contextType: %s; error: ", request.getContentLengthLong(), request.getCharacterEncoding(), request.getContentType());
        String charset = StringUtils.isBlank(request.getCharacterEncoding()) ? "" : request.getCharacterEncoding().toUpperCase();
        String contentType = StringUtils.isBlank(request.getContentType()) ? "" : request.getContentType().toLowerCase();
        // 检查body是否可以转utf-8
        if (!contentType.contains("json") && !charset.contains("ISO-8859-1") && !charset.contains("UTF-8")) {
            return body;
        }
        if (!(request instanceof CachingHttpServletRequestWrapper)) {
            return body;
        }

        return ((CachingHttpServletRequestWrapper) request).getBodyWithUTF8();
    }

    private String formatResponseBody(HttpServletResponse response) {
        String body = String.format("charset: %s; contextType: %s; error: ", response.getCharacterEncoding(), response.getContentType());
        String contentType = StringUtils.isBlank(response.getContentType()) ? "" : response.getContentType().toLowerCase();
        String charset = StringUtils.isBlank(response.getCharacterEncoding()) ? "" : response.getCharacterEncoding().toUpperCase();
        // 检查body是否可以转utf-8
        if (!contentType.contains("json") && !charset.contains("ISO-8859-1") && !charset.contains("UTF-8")) {
            return body;
        }

        if (!(response instanceof CopyingHttpServletResponseWrapper)) {
            return body;
        }

        return ((CopyingHttpServletResponseWrapper) response).getBodyWithUTF8();
    }
}