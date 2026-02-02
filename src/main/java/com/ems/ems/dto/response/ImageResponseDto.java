package com.ems.ems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ImageResponseDto {
    private byte[] bytes;
    private String contentType;
    private String fileName;

}
