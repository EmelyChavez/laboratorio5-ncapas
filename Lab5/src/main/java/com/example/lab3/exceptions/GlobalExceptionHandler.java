package com.example.lab3.exceptions;


import com.example.lab3.domain.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlerResourceNotFound(ResourceNotFoundException e){
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }
    public ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, Object message){
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity
                .status(status)
                .body(ApiErrorResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDate.now())
                        .build()
                );
    }
}