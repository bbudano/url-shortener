package com.example.urlshortener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
class UrlShortenerRestController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    ResponseEntity<?> shortenUrl(final @RequestBody String url) {
        return ResponseEntity
                .ok()
                .body(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/{key}")
                        .buildAndExpand(urlShortenerService.shorten(url))
                        .toUriString());
    }

    @GetMapping("/{key}")
    ResponseEntity<Void> resolve(final @PathVariable("key") String key) {
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(urlShortenerService.resolveForKey(key))
                .build();
    }

}
