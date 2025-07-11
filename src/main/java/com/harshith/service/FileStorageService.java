package com.harshith.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.harshith.validation.FileUploadValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap; // Import HashMap
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
            String originalFilename = file.getOriginalFilename();
            String sanitizedFilename = fileValidator.sanitizeFilename(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "_" + sanitizedFilename;

            // Use a mutable map to add conditional parameters
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("resource_type", resourceType);
            uploadParams.put("public_id", uniqueFilename);
            uploadParams.put("use_filename", false);
            uploadParams.put("unique_filename", true);

            // --- THE FIX ---
            // For raw files like assignments, explicitly set the access type to public.
            // This makes the returned URL directly downloadable by anyone with the link.
            if ("raw".equals(resourceType)) {
                uploadParams.put("access_mode", "public");
            }

            Map<?, ?> uploadResult = getCloudinary().uploader().upload(file.getBytes(), uploadParams);
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    // This legacy method is fine as is
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
        return uploadDocumentFile(file);
    }
}