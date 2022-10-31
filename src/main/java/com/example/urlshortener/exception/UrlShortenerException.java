package com.example.urlshortener.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UrlShortenerException extends RuntimeException {

    private final HttpStatus httpStatus;

    public UrlShortenerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
