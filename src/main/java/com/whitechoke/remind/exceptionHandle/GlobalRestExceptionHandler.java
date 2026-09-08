package com.whitechoke.remind.exceptionHandle;

import com.whitechoke.remind.exceptionHandle.dto.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

        var response = createResponse("Invalid argument", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundException(
            EntityNotFoundException exception
    ) {
        log.error("Handle entity not found exception: ", exception);

        var response = createResponse("Entity not found", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    private ExceptionResponse createResponse(String message, Exception exception) {
        return ExceptionResponse.builder()
                .message(message)
                .detailMessage(exception.getMessage())
                .errorTime(Instant.now())
                .build();
    }
}
