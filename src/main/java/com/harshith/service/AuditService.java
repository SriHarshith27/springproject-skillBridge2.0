package com.harshith.service;

import com.harshith.model.AuditLog;
import com.harshith.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void logCourseCreation(Long userId, Long courseId) {
        createAuditLog(userId, "COURSE_CREATED", "Course created with ID: " + courseId, courseId);
    }

    public void logCourseUpdate(Long userId, Long courseId) {
        createAuditLog(userId, "COURSE_UPDATED", "Course updated with ID: " + courseId, courseId);
    }

    public void logCourseDeletion(Long userId, Long courseId) {
        createAuditLog(userId, "COURSE_DELETED", "Course deleted with ID: " + courseId, courseId);
    }

    public void logEnrollment(Long userId, Long courseId) {
        createAuditLog(userId, "USER_ENROLLED", "User enrolled in course ID: " + courseId, courseId);
    }

    public void logDeEnrollment(Long userId, Long courseId) {
        createAuditLog(userId, "USER_DE_ENROLLED", "User de-enrolled from course ID: " + courseId, courseId);
    }

    public void logModuleAddition(Long userId, Long courseId, Long moduleId) {
        createAuditLog(userId, "MODULE_ADDED", "Module " + moduleId + " added to course " + courseId, courseId);
    }

    public void logAssignmentAddition(Long userId, Long courseId, Long assignmentId) {
        createAuditLog(userId, "ASSIGNMENT_ADDED", "Assignment " + assignmentId + " added to course " + courseId, courseId);
    }

    public void logAssignmentSubmission(Long userId, Long assignmentId) {
        createAuditLog(userId, "ASSIGNMENT_SUBMITTED", "Assignment submitted with ID: " + assignmentId, null);
    }

    public void logGradeChange(Long userId, Long assignmentId, Integer oldGrade, Integer newGrade) {
        String details = String.format("Grade changed for assignment %d from %s to %d", 
                                     assignmentId, oldGrade != null ? oldGrade.toString() : "ungraded", newGrade);
        createAuditLog(userId, "GRADE_CHANGED", details, null);
    }

    public void logUserDeletion(Long adminId, Long deletedUserId) {
        createAuditLog(adminId, "USER_DELETED", "User deleted with ID: " + deletedUserId, null);
    }

    public void logLogin(Long userId, String ipAddress) {
        createAuditLog(userId, "USER_LOGIN", "User logged in from IP: " + ipAddress, null);
    }

    public void logLogout(Long userId) {
        createAuditLog(userId, "USER_LOGOUT", "User logged out", null);
    }

    // Add missing methods
    public void logUserRegistration(Long userId) {
        createAuditLog(userId, "USER_REGISTERED", "User registered with ID: " + userId, null);
    }

    public void logFailedLogin(Long userId, String reason) {
        createAuditLog(userId, "LOGIN_FAILED", "Login failed: " + reason, null);
    }

    public void logUserUpdate(Long currentUserId, Long updatedUserId) {
        createAuditLog(currentUserId, "USER_UPDATED", "User updated with ID: " + updatedUserId, null);
    }

    public void logPasswordChange(Long userId) {
        createAuditLog(userId, "PASSWORD_CHANGED", "Password changed for user ID: " + userId, null);
    }

    private void createAuditLog(Long userId, String action, String details, Long entityId) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setAction(action);
            auditLog.setDetails(details);
            auditLog.setEntityId(entityId);
            auditLog.setTimestamp(LocalDateTime.now());
            
            auditLogRepository.save(auditLog);
            
            // Also log to application logs for monitoring
            log.info("AUDIT: User {} performed action {} - {}", userId, action, details);
        } catch (Exception e) {
            // Don't let audit logging failures break the main functionality
            log.error("Failed to create audit log: {}", e.getMessage(), e);
        }
    }
}