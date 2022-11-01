package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
import com.example.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
class UrlShortenerRestController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    ResponseEntity<ShortenUrlResponse> shorten(final @RequestBody ShortenUrlRequest request,
                                               HttpServletRequest httpServletRequest) {
        log.info("scheme: {}", httpServletRequest.getScheme());

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
