package com.example.urlshortener.service;

import com.example.urlshortener.exception.KeyNotFoundException;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final RedisTemplate<String, String> redisTemplate;

    public String shorten(String url) {
        String key = hashUrl(url);
        redisTemplate.opsForValue().set(key, url);
        return key;
    }

    public URI resolveForKey(String key) {
        Optional<String> keyOptional = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        if (keyOptional.isEmpty()) throw new KeyNotFoundException("Key not found: " + key);
        return URI.create(keyOptional.get());
    }

    private String hashUrl(String url) {
        return Hashing
                .murmur3_32_fixed()
                .hashString(url, StandardCharsets.UTF_8)
                .toString();
    }

}
