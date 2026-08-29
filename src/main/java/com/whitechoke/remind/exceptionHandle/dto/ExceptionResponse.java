package com.whitechoke.remind.exceptionHandle.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ExceptionResponse(
        String message,
        String detailMessage,
        Instant errorTime
) {
}
