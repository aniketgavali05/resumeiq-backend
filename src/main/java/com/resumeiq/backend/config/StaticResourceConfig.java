package com.resumeiq.backend.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig
        implements WebMvcConfigurer {

    @Value("${app.upload.profile-dir:uploads/profile-images}")
    private String profileImageDirectory;

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        String location =
                Paths.get(profileImageDirectory)
                        .toAbsolutePath()
                        .normalize()
                        .toUri()
                        .toString();

        registry
                .addResourceHandler(
                        "/uploads/profile-images/**"
                )
                .addResourceLocations(location);
    }
}