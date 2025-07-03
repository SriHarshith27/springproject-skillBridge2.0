package com.harshith.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    // Injecting credentials from application.properties
    public FileStorageService(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true // Ensures URLs are https
        ));
    }

    /**
     * Uploads a file to Cloudinary and returns its secure URL.
     * @param file The file to upload.
     * @return The public URL of the uploaded file.
     */
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File to upload cannot be null or empty.");
        }

        try {
            // For videos, specify the resource_type. For other files, Cloudinary auto-detects.
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto" // Let Cloudinary determine if it's an image, video, or raw file
            ));

            // The result map contains the secure URL of the uploaded file
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            // In a real app, you'd have more robust error handling
            throw new RuntimeException("Could not upload the file. Error: " + e.getMessage());
        }
    }
}
