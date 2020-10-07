package com.stn.ester.core.filters.wrapper;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);

        try {
            this.body = IOUtils.toByteArray(request.getInputStream());
        } catch (IOException ex) {
            this.body = new byte[0];
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);

        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new RuntimeException("Not implemented");
            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    public byte[] getBody() {
        return this.body;
    }
}
