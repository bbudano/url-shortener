package com.example.urlshortener.service;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
import com.example.urlshortener.exception.UrlShortenerException;
import com.google.common.hash.Hashing;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final RedisTemplate<String, String> redisTemplate;

    public ShortenUrlResponse shorten(ShortenUrlRequest request) {
        if (!isValidURL(request.getUrl())) throw new UrlShortenerException("Invalid url", HttpStatus.BAD_REQUEST);

        String key = hashUrl(request.getUrl());

        redisTemplate.opsForValue().set(key, request.getUrl());

        String shortenedUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{key}")
                .buildAndExpand(key)
                .toUriString();

        String base64QRCode = generateQRCode(shortenedUrl);

        return new ShortenUrlResponse(
                shortenedUrl,
                base64QRCode
        );
    }

    public URI resolveForKey(String key) {
        Optional<String> keyOptional = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        if (keyOptional.isEmpty()) throw new UrlShortenerException("Key not found: " + key, HttpStatus.NOT_FOUND);
        return URI.create(keyOptional.get());
    }

    public String generateQRCode(String url) {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", bos);
        } catch (IOException | WriterException e) {
            throw new UrlShortenerException("Error generating QR code", HttpStatus.BAD_REQUEST);
        }

        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
        return true;
    }

    private String hashUrl(String url) {
        return Hashing
                .murmur3_32_fixed()
                .hashString(url, StandardCharsets.UTF_8)
                .toString();
    }

}
