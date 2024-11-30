package com.ww.common.architecture.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Service
public class RequestResponseWrapperFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest cacheHttpServletRequestWrapper = null;
        ServletResponse cacheHttpServletResponseWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            cacheHttpServletRequestWrapper = new CacheHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (servletResponse instanceof HttpServletResponse) {
            cacheHttpServletResponseWrapper = new CacheHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        }

        filterChain.doFilter(null == cacheHttpServletRequestWrapper ? servletRequest : cacheHttpServletRequestWrapper,
                null == cacheHttpServletResponseWrapper ? servletResponse : cacheHttpServletResponseWrapper);
    }
}