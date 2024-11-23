package com.ww.common.architecture.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Service
public class CacheServletRequestResponseWrapperFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest cacheHttpServletRequestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            cacheHttpServletRequestWrapper = new CacheHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (null == cacheHttpServletRequestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(cacheHttpServletRequestWrapper, servletResponse);
        }
    }
}