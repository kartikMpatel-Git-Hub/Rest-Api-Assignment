package com.ems.ems.service.impl;

import com.ems.ems.service.FileStorageInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService implements FileStorageInterface {

    @Value("${file.upload-dir}")
    private String filePath;

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        Path path = Paths.get(filePath);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString()+"_"+originalFileName;
        try {
            if(fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = path.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
