package com.harshith.validation;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class FileUploadValidator {

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final long MAX_VIDEO_SIZE = 500 * 1024 * 1024; // 500MB for videos
    
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    
    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
        "video/mp4", "video/avi", "video/mov", "video/wmv", "video/webm"
    );
    
    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
        "application/pdf", "application/msword", 
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "text/plain"
    );
    
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    public void validateImageFile(MultipartFile file) {
        validateFile(file, ALLOWED_IMAGE_TYPES, MAX_FILE_SIZE, "image");
    }
    
    public void validateVideoFile(MultipartFile file) {
        validateFile(file, ALLOWED_VIDEO_TYPES, MAX_VIDEO_SIZE, "video");
    }
    
    public void validateDocumentFile(MultipartFile file) {
        validateFile(file, ALLOWED_DOCUMENT_TYPES, MAX_FILE_SIZE, "document");
    }

    private void validateFile(MultipartFile file, List<String> allowedTypes, long maxSize, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Check file size
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                String.format("%s file size cannot exceed %d MB", fileType, maxSize / (1024 * 1024))
            );
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                String.format("Invalid %s file type. Allowed types: %s", fileType, allowedTypes)
            );
        }

        // Validate filename
        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        // Check for directory traversal attacks
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename: contains illegal characters");
        }

        // Additional security: check filename pattern
        if (!SAFE_FILENAME_PATTERN.matcher(filename).matches()) {
            throw new IllegalArgumentException("Filename contains invalid characters");
        }

        // Check file extension matches content type
        validateFileExtension(filename, contentType);
    }

    private void validateFileExtension(String filename, String contentType) {
        String extension = getFileExtension(filename).toLowerCase();
        
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
            case "image/jpg":
                if (!extension.equals("jpg") && !extension.equals("jpeg")) {
                    throw new IllegalArgumentException("File extension doesn't match content type");
                }
                break;
            case "image/png":
                if (!extension.equals("png")) {
                    throw new IllegalArgumentException("File extension doesn't match content type");
                }
                break;
            case "video/mp4":
                if (!extension.equals("mp4")) {
                    throw new IllegalArgumentException("File extension doesn't match content type");
                }
                break;
            case "application/pdf":
                if (!extension.equals("pdf")) {
                    throw new IllegalArgumentException("File extension doesn't match content type");
                }
                break;
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    public String sanitizeFilename(String filename) {
        if (filename == null) return "unnamed_file";
        
        // Remove path separators and other dangerous characters
        String sanitized = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        // Ensure filename isn't too long
        if (sanitized.length() > 100) {
            String extension = getFileExtension(sanitized);
            String nameWithoutExt = sanitized.substring(0, sanitized.lastIndexOf('.'));
            sanitized = nameWithoutExt.substring(0, 95 - extension.length()) + "." + extension;
        }
        
        return sanitized;
    }
}