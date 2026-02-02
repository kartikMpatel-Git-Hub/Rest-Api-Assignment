package com.ems.ems.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationErrorResponseDto {
    private Map<String,String> errors;
}
