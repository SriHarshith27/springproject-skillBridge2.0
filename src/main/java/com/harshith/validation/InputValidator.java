package com.harshith.validation;
import org.owasp.html.Sanitizers;
import org.owasp.html.PolicyFactory;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Component
public class InputValidator {


    private static final PolicyFactory POLICY_FACTORY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);


    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    public void validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (email.length() > 100) {
            throw new IllegalArgumentException("Email too long");
        }
    }

    public String sanitizeInput(String input) {
        if (input == null) return null;

        // Use the OWASP sanitizer to clean the input
        return POLICY_FACTORY.sanitize(input).trim();
    }

    public void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Username must be 3-20 characters and contain only letters, numbers, and underscores");
        }
    }

    public void validatePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                "Password must be at least 8 characters long and contain at least one uppercase letter, " +
                "one lowercase letter, one digit, and one special character"
            );
        }
    }

    public void validatePhone(String phone) {
        if (StringUtils.hasText(phone) && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }



    public void validateCourseTitle(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Course title too long (max 200 characters)");
        }
    }

    public void validateCourseDescription(String description) {
        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("Course description cannot be empty");
        }
        if (description.length() > 2000) {
            throw new IllegalArgumentException("Course description too long (max 2000 characters)");
        }
    }

    public void validateGrade(Integer grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }
    }
}