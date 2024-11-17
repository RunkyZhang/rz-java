package com.ww.common.architecture.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class ControllerHandlerInterceptor implements HandlerInterceptor {
    public ThreadLocal<HandlerContext> contextThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerContext context = new HandlerContext();
        context.setStartTimePoint(System.currentTimeMillis());
        context.setRemoteAddress(request.getRemoteAddr());
        context.setRequestURL(request.getRequestURL().toString());
        context.setRequestMethod(request.getMethod());

        contextThreadLocal.set(context);
        log.info("Start({}) - URL: {}; METHOD: {}; IP: {}",
                contextThreadLocal.get().getStartTimePoint(),
                request.getRequestURL().toString(),
                request.getMethod(),
                request.getRemoteAddr());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        log.info("End({}) - SPEND TIME: {}; URL: {}; METHOD: {}; IP: {}; STATUS: {}; ",
                contextThreadLocal.get().getStartTimePoint(),
                System.currentTimeMillis() - contextThreadLocal.get().getStartTimePoint() + " ms",
                request.getRequestURL().toString(),
                request.getMethod(),
                request.getRemoteAddr(),
                response.getStatus());


        contextThreadLocal.remove();
    }
}