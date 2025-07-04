package com.harshith.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.harshith.validation.FileUploadValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileUploadValidator fileValidator;
    
    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    
    @Value("${cloudinary.api_key}")
    private String apiKey;
    
    @Value("${cloudinary.api_secret}")
    private String apiSecret;
    
    private Cloudinary cloudinary;

    // Initialize Cloudinary after dependency injection
    private Cloudinary getCloudinary() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true
            ));
        }
        return cloudinary;
    }

    public String uploadVideoFile(MultipartFile file) {
        fileValidator.validateVideoFile(file);
        return uploadToCloudinary(file, "video");
    }
    
    public String uploadDocumentFile(MultipartFile file) {
        fileValidator.validateDocumentFile(file);
        return uploadToCloudinary(file, "raw");
    }
    
    public String uploadImageFile(MultipartFile file) {
        fileValidator.validateImageFile(file);
        return uploadToCloudinary(file, "image");
    }

    private String uploadToCloudinary(MultipartFile file, String resourceType) {
        try {
            // Generate unique filename to prevent conflicts
            String originalFilename = file.getOriginalFilename();
            String sanitizedFilename = fileValidator.sanitizeFilename(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "_" + sanitizedFilename;

            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "public_id", uniqueFilename,
                    "use_filename", false,
                    "unique_filename", true
            );

            // Add additional security for videos
            if ("video".equals(resourceType)) {
                uploadParams.put("quality", "auto");
                uploadParams.put("fetch_format", "auto");
            }

            Map<?, ?> uploadResult = getCloudinary().uploader().upload(file.getBytes(), uploadParams);
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    // Legacy method for backward compatibility
    public String uploadFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("video/")) {
                return uploadVideoFile(file);
            } else if (contentType.startsWith("image/")) {
                return uploadImageFile(file);
            } else {
                return uploadDocumentFile(file);
            }
        }
        return uploadDocumentFile(file); // Default fallback
    }
}