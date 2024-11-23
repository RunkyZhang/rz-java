package com.ww.common.architecture.controller;

import com.ww.common.architecture.log.AccessLogContext;
import com.ww.common.architecture.log.AccessLogStrategy;
import com.ww.common.architecture.log.AccessLogStrategySelector;
import com.ww.common.architecture.rt.RTMonitor;
import com.ww.common.base.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        context.setStartLogMessage(String.format("Start(%d) - url(%s): %s localIp: %s; remoteIp(%s): %s; parameters: %s",
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
            log.info("End({}) - spend: {}; url({}-{}): {}; bucket: {}; localIp: {}; remoteIp({}): {}; parameter: {}; result: {}",
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
                    // TODO: body
                    "null == value ? '' : value.toString()");
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
        parameters.add("ww-body: " + this.formatBody(request));


        return "[" + String.join(",", parameters) + "]";
    }

    private String formatBody(HttpServletRequest request) {
        String body = String.format("length: %d; contextType: %s; error: ", request.getContentLengthLong(), request.getContentType());
        if (ServletFileUpload.isMultipartContent(request)) {
            return body;
        }
        String contentType = StringUtils.isBlank(request.getContentType()) ? "" : request.getContentType().toLowerCase();
        if (!contentType.contains("json") && !contentType.contains("xml") && !contentType.contains("text")) {
            return body;
        }

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            body += e.getMessage();
            return body;
        }

        return stringBuilder.toString();
    }
}