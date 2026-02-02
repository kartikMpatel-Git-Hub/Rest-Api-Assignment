package com.ems.ems.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageInterface {
    String storeFile(MultipartFile file) throws IOException;

    byte[] getImage(String image) throws IOException;
}
