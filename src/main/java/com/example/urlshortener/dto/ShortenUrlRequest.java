package com.example.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShortenUrlRequest {

    @JsonProperty("url")
    private String url;

}
