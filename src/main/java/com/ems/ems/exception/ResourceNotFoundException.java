package com.ems.ems.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    private String resource;
    private String field;
    private String value;

}
