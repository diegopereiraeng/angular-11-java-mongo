package com.bezkoder.spring.data.mongodb.dto;

import lombok.Value;

@Value
public class ApiResponse {
    private Boolean success;
    private String message;
}