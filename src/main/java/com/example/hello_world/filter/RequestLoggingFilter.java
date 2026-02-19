package com.example.hello_world.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 受信ヘッダーの requestId を優先し、なければ採番する
        String requestId = resolveRequestId(request);
        // 処理時間計測用
        long startNanos = System.nanoTime();

        // 後続ログから参照できるよう MDC に格納する
        MDC.put(REQUEST_ID_KEY, requestId);
        // 呼び出し元が追跡できるようレスポンスヘッダーへも返す
        response.setHeader(REQUEST_ID_HEADER, requestId);

        String path = request.getRequestURI();
        String query = request.getQueryString();
        String requestPath = query == null ? path : path + "?" + query;

        // リクエスト開始ログ
        log.info("request started. method={}, path={}", request.getMethod(), requestPath);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 正常/例外どちらでも終了ログを出して MDC を掃除する
            long durationMs = (System.nanoTime() - startNanos) / 1_000_000;
            log.info(
                    "request finished. method={}, path={}, status={}, durationMs={}",
                    request.getMethod(),
                    requestPath,
                    response.getStatus(),
                    durationMs
            );
            MDC.remove(REQUEST_ID_KEY);
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return requestId;
    }
}
