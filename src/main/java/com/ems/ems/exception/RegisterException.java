package com.ems.ems.exception;

import lombok.Data;

import java.util.List;

@Data
public class RegisterException extends RuntimeException {
    private List<String> errors;
    public RegisterException(List<String> errors) {
        this.errors = errors;
    }
}
