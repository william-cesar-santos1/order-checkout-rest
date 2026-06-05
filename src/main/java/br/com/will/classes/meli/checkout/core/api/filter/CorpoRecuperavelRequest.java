package br.com.will.classes.meli.checkout.core.api.filter;

import jakarta.servlet.http.HttpServletRequest;

class CorpoRecuperavelRequest extends jakarta.servlet.http.HttpServletRequestWrapper {
        private final byte[] body;

        CorpoRecuperavelRequest(HttpServletRequest req, byte[] body) {
            super(req);
            this.body = body;
        }

        @Override
        public jakarta.servlet.ServletInputStream getInputStream() {
            java.io.ByteArrayInputStream src = new java.io.ByteArrayInputStream(body);
            return new jakarta.servlet.ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return src.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(jakarta.servlet.ReadListener l) {
                }

                @Override
                public int read() {
                    return src.read();
                }
            };
        }
    }