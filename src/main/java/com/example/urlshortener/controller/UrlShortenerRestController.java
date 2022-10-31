package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
import com.example.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
class UrlShortenerRestController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    ResponseEntity<ShortenUrlResponse> shorten(final @RequestBody ShortenUrlRequest request) {
        return ResponseEntity
                .ok()
                .body(urlShortenerService.shorten(request));
    }

    @GetMapping("/{key}")
    ResponseEntity<Void> resolve(final @PathVariable("key") String key) {
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(urlShortenerService.resolveForKey(key))
                .build();
    }

}
