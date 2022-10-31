package com.example.urlshortener.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private final Instant timestamp;

    private final int status;

    private final List<String> errors;

    private final String path;

    public ErrorResponse(final HttpStatus httpStatus, final String path) {
        this.timestamp = Instant.now();
        this.status = httpStatus.value();
        this.errors = new ArrayList<>();
        this.path = path;
    }

    public ErrorResponse(final HttpStatus httpStatus, final List<String> errors, final String path) {
        this.timestamp = Instant.now();
        this.status = httpStatus.value();
        this.errors = errors;
        this.path = path;
    }

}
