package com.resumeiq.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ResumeStorageService {

    @Value("${resume.storage.path:uploads/resumes}")
    private String uploadDirectory;

    private Path storageLocation;

    @PostConstruct
    public void init() throws IOException {

        storageLocation = Paths.get(uploadDirectory)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(storageLocation);
    }

    public String storeFile(MultipartFile file) throws IOException {

        String originalFilename =
                StringUtils.cleanPath(file.getOriginalFilename());

        String extension = "";

        int index = originalFilename.lastIndexOf(".");

        if (index > 0) {
            extension = originalFilename.substring(index);
        }

        String storedFilename =
                UUID.randomUUID() + extension;

        Path targetLocation =
                storageLocation.resolve(storedFilename);

        Files.copy(
                file.getInputStream(),
                targetLocation,
                StandardCopyOption.REPLACE_EXISTING
        );

        return storedFilename;
    }

    public Path loadFile(String filename) {

        return storageLocation.resolve(filename);

    }

    public void deleteFile(String filename) throws IOException {

        Files.deleteIfExists(
                storageLocation.resolve(filename)
        );

    }

}