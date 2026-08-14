package com.resumeiq.backend.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.resumeiq.backend.response.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(
                    GlobalExceptionHandler.class
            );

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse>
    handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse>
    handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    /**
     * Invalid login credentials.
     *
     * Do NOT return HTTP 500 for this case.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse>
    handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        "Invalid email or password.",
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse>
    handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors =
                new HashMap<>();

        for (var error :
                ex.getBindingResult().getFieldErrors()) {

            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse>
    handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error(
                "Unhandled exception at [{}]: {}",
                request.getRequestURI(),
                ex.getMessage(),
                ex
        );

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}