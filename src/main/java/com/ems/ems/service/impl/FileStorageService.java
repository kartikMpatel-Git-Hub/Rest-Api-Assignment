package com.ems.ems.service.impl;

import com.ems.ems.dto.response.ImageResponseDto;
import com.ems.ems.exception.SomethingWentWrongException;
import com.ems.ems.service.FileStorageInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString()+"_"+originalFileName;
        try {
            if(fileName.contains("..")) {
                throw new SomethingWentWrongException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = path.resolve(fileName).normalize();
            if(!targetLocation.startsWith(path)){
                throw new SomethingWentWrongException("can not store outside of directory !");
            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new SomethingWentWrongException("Could not store file " + fileName + ". Please try again!");
        }
    }

    @Override
    public byte[] getImage(String image) throws IOException {
        Path path = Paths.get(filePath+"/"+image);
        return Files.readAllBytes(path);
    }

    @Override
    public ImageResponseDto getImageResponse(String image) throws IOException {

        Path path = Paths.get(filePath, image);
        byte[] bytes = Files.readAllBytes(path);

        String detected = Files.probeContentType(path);
        if (detected == null) {
            String lowered = image.toLowerCase();
            if (lowered.endsWith(".png")) detected = "image/png";
            else if (lowered.endsWith(".jpg") || lowered.endsWith(".jpeg")) detected = "image/jpeg";
            else if (lowered.endsWith(".gif")) detected = "image/gif";
            else detected = "application/octet-stream";
        }
        return new ImageResponseDto(bytes, detected, image);
    }
}
