package br.com.will.classes.meli.checkout.core.api.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

class CapturaResponse extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(buffer);
    private final ServletOutputStream outputStream = new ServletOutputStream() {
        @Override
        public boolean isReady() { return true; }

        @Override
        public void setWriteListener(WriteListener writeListener) {}

        @Override
        public void write(int b) {
            buffer.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) {
            buffer.write(b, off, len);
        }
    };

    CapturaResponse(HttpServletResponse r) {
        super(r);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    String getCapturado() {
        writer.flush();
        byte[] bytes = buffer.toByteArray();
        try {
            getResponse().getOutputStream().write(bytes);
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}