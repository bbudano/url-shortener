package com.example.urlshortener.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ UrlShortenerException.class })
    public ResponseEntity<ErrorResponse> handleUrlShortenerException(final UrlShortenerException ex,
                                                                              WebRequest request) {
        log.error("Unhandled exception", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus(),
                Collections.singletonList(ex.getMessage()),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

}
