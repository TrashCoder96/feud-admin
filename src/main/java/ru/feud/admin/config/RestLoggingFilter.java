package ru.feud.admin.config;

import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public class RestLoggingFilter extends CommonsRequestLoggingFilter {

    private boolean includeOnlyJsonPayload = true;

    public void includeOnlyJsonPayload(boolean onlyJsonPayload) {
        this.includeOnlyJsonPayload = onlyJsonPayload;
    }

    private static class TeeServletResponseWrapper extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream byteArrayOutputStream;
        private final OutputStream teeOutputStream;
        private PrintWriter writer;

        TeeServletResponseWrapper(HttpServletResponse response) {
            super(response);
            this.byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                this.teeOutputStream = new TeeOutputStream(response.getOutputStream(), byteArrayOutputStream);
            } catch (IOException iox) {
                throw new RuntimeException("Can't initialize response wrapper for filter.", iox);
            }
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new SimpleServletOutputStream(this.teeOutputStream);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (this.writer == null) {
                this.writer = new PrintWriter(this.teeOutputStream);
            }
            return this.writer;
        }


        byte[] getContentAsByteArray() {
            return this.byteArrayOutputStream.toByteArray();
        }
    }

    private static class SimpleServletOutputStream extends ServletOutputStream {
        private final OutputStream outputStream;

        private SimpleServletOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            this.outputStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            this.outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.outputStream.close();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        boolean shouldLog = shouldLog(request);
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;

        if (shouldLog && isFirstRequest) {
            if (isIncludePayload() && !(request instanceof ContentCachingRequestWrapper)) {
                requestToUse = new ContentCachingRequestWrapper(request);
            }
            if (isIncludePayload()) {
                responseToUse = new TeeServletResponseWrapper(response);
            }
        }
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            if (shouldLog) {
                logAsString(requestToUse, responseToUse);
            }
        }
    }

    private void logAsString(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder msg = new StringBuilder();
        msg.append("\nRequest info");
        msg.append("\n==================================");
        msg.append("\n").append(request.getMethod()).append(" ").append(request.getRequestURI());
        if (isIncludeQueryString()) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }
        if (isIncludeClientInfo()) {
            String client = request.getRemoteAddr();
            if (StringUtils.hasLength(client)) {
                msg.append("\n\tclient:").append(client);
            }
            HttpSession session = request.getSession(false);
            if (session != null) {
                msg.append("\n\tsession: ").append(session.getId());
            }
            String user = request.getRemoteUser();
            if (user != null) {
                msg.append("\n\tuser: ").append(user);
            }
        }
        if (isIncludeHeaders()) {
            CollectionUtils.toIterator(request.getHeaderNames())
                .forEachRemaining(headerName -> {
                    if (!StringUtils.isEmpty(headerName)) {
                        msg.append("\n").append(headerName).append(": ").append(request.getHeader(headerName));
                    }
                });
        }
        if (isIncludePayload()) {
            ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
            if (wrapper != null) {
                appendPayload(msg, wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            }
        }
        msg.append("\nResponse info");
        msg.append("\n==================================");
        msg.append("\nHTTP/1.1 ").append(response.getStatus());
        if (isIncludeHeaders()) {
            String headers = response.getHeaderNames().stream()
                .map(header -> header + ": " + response.getHeader(header))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(joining("\n"));
            msg.append("\n").append(headers);
        }
        if (isIncludePayload()) {
            String contentType = response.getHeader("Content-Type");
            TeeServletResponseWrapper wrapper = WebUtils.getNativeResponse(response, TeeServletResponseWrapper.class);
            if (wrapper != null) {
                if (!isIncludeOnlyJsonPayload() || isIncludeOnlyJsonPayload() && contentType != null && contentType.toLowerCase().contains("json")) {
                    appendPayload(msg, wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
                } else {
                    msg.append("\n\n[Non JSON or empty response]");
                }
            }
        }
        logger.debug(msg.toString());
    }

    private void appendPayload(StringBuilder msg, byte[] buf, String characterEncoding) {
        String payload = "[Empty]";
        msg.append("\n\n");
        if (buf.length > 0) {
            try {
                payload = new String(buf, 0, buf.length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }

        }
        msg.append(payload).append("\n");
    }

    public boolean isIncludeOnlyJsonPayload() {
        return includeOnlyJsonPayload;
    }

}
