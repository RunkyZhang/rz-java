package com.ww.common.architecture.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/*
 *
 * */
public class CacheHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private ServletOutputStream servletOutputStream;
    private PrintWriter printWriter;
    private ServletOutputStreamCopier servletOutputStreamCopier;

    public CacheHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (printWriter != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (servletOutputStream == null) {
            servletOutputStream = getResponse().getOutputStream();
            servletOutputStreamCopier = new ServletOutputStreamCopier(servletOutputStream);
        }

        return servletOutputStreamCopier;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (servletOutputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (printWriter == null) {
            servletOutputStreamCopier = new ServletOutputStreamCopier(getResponse().getOutputStream());
            printWriter = new PrintWriter(new OutputStreamWriter(servletOutputStreamCopier, getResponse().getCharacterEncoding()), true);
        }

        return printWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (printWriter != null) {
            printWriter.flush();
        } else if (servletOutputStream != null) {
            servletOutputStreamCopier.flush();
        }
    }

    public String getBodyWithUTF8() {
        byte[] bytes;
        if (servletOutputStreamCopier != null) {
            bytes = servletOutputStreamCopier.getCopyByteArrayOutputStream();
        } else {
            bytes = new byte[0];
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static class ServletOutputStreamCopier extends ServletOutputStream {
        private final OutputStream outputStream;
        private final ByteArrayOutputStream copyByteArrayOutputStream;

        public ServletOutputStreamCopier(OutputStream outputStream) {
            this.outputStream = outputStream;
            this.copyByteArrayOutputStream = new ByteArrayOutputStream(1024);
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
            copyByteArrayOutputStream.write(b);
        }

        public byte[] getCopyByteArrayOutputStream() {
            return copyByteArrayOutputStream.toByteArray();
        }

        @Override
        public boolean isReady() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            // TODO Auto-generated method stub
        }
    }
}