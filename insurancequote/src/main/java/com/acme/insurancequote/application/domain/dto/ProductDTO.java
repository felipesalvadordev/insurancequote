package com.acme.insurancequote.application.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDTO {
    private String id;
    private String name;
    private Instant createdAt;
    private boolean active;
}
