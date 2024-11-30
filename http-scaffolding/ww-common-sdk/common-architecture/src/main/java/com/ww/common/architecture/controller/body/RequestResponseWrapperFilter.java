package com.ww.common.architecture.controller.body;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 把处理body的wrapper注入进去
@Service
public class RequestResponseWrapperFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest cachingHttpServletRequestWrapper = null;
        ServletResponse copyingHttpServletResponseWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            cachingHttpServletRequestWrapper = new CachingHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (servletResponse instanceof HttpServletResponse) {
            copyingHttpServletResponseWrapper = new CopyingHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        }

        filterChain.doFilter(null == cachingHttpServletRequestWrapper ? servletRequest : cachingHttpServletRequestWrapper,
                null == copyingHttpServletResponseWrapper ? servletResponse : copyingHttpServletResponseWrapper);
    }
}