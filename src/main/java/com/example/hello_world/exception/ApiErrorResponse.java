package com.example.hello_world.exception;

public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        String requestId
) {
}
