package com.ll.stopnoise.global;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // POST 요청일 경우에만 본문 로깅
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // 요청 본문을 여러 번 읽을 수 있도록 래퍼 클래스 사용
            HttpServletRequest wrappedRequest = new ReadableRequestWrapper(request);
            String body = StreamUtils.copyToString(wrappedRequest.getInputStream(), StandardCharsets.UTF_8);
            System.out.println("Request Body: " + body); // 콘솔에 요청 본문 출력

            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    // 요청 본문을 다시 읽을 수 있게 해주는 래퍼 클래스
    private static class ReadableRequestWrapper extends HttpServletRequestWrapper {
        private final byte[] body;

        public ReadableRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.body = StreamUtils.copyToByteArray(request.getInputStream());
        }

        @Override
        public ServletInputStream getInputStream() {
            return new DelegatingServletInputStream(new ByteArrayInputStream(this.body));
        }
    }
}