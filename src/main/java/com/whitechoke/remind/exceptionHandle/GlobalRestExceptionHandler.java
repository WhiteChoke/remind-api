package com.whitechoke.remind.exceptionHandle;

import com.whitechoke.remind.exceptionHandle.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> invalidArgumentExceptionHandle(
            MethodArgumentNotValidException exception
    ) {
        log.error("Handle invalid argument exception: ", exception);

        var response = ExceptionResponse.builder()
                .message("Invalid argument")
                .detailMessage(exception.getMessage())
                .errorTime(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
