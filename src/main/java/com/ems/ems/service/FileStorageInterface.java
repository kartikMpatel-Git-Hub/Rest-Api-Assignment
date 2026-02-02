package com.ems.ems.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageInterface {
    String storeFile(MultipartFile file) throws IOException;
//    MultipartFile getFile(String fileName);
}
