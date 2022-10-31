package com.example.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrlResponse {

    @JsonProperty("shortenedUrl")
    private String shortenedUrl;

    @JsonProperty("base64QRCode")
    private String base64QRCode;

}
