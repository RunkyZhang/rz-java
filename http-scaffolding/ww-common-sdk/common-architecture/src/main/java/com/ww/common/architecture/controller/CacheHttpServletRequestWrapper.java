package com.ww.common.architecture.controller;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/*
 * request的body只能被读取一次。因此在日志中被读取之后，在mvc框架中再次被读取就会抛异常
 * 所以把ServletRequest从新包装。第一次读出body之后缓存起来，下次直接使用缓存中的数据
 * */
public class CacheHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final int BUFFER_SIZE = 1024 * 8;
    private byte[] body;

    public CacheHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        /*
         * 如果请求是【多部分】请求，就直接返回，不进行后续的处理。
         * 【多部分】请求包含了文件数据和其他表单字段数据
         * 因为如果body是文件内容，那么就无需缓存。这样不需要占用本地内存，且日志侧也做相应判断不会打印文件body
         */
        if (ServletFileUpload.isMultipartContent(request)) {
            return;
        }

        BufferedReader reader = request.getReader();
        try (StringWriter writer = new StringWriter()) {
            int read;
            char[] buffer = new char[BUFFER_SIZE];
            while ((read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, read);
            }
            this.body = writer.getBuffer().toString().getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
     * 获取请求体数据
     *
     * @return
     */
    public String getBodyWithUTF8() {
        return new String(this.body, StandardCharsets.UTF_8);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
