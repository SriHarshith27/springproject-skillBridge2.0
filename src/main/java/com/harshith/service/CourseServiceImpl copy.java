package com.harshith.service;

import com.harshith.dto.CourseRequest;
import com.harshith.model.Assignment;
import com.harshith.model.Course;
import com.harshith.model.CourseModule;
import com.harshith.model.User;
import com.harshith.repository.AssignmentRepository;
import com.harshith.repository.CourseModuleRepository;
import com.harshith.repository.CourseRepository;
import com.harshith.repository.UserRepository;
import com.harshith.validation.InputValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final AssignmentRepository assignmentRepository;
    private final FileStorageService fileStorageService;
    private final InputValidator inputValidator;
    private final AuditService auditService;

    @Override
    @Transactional
    public Course createCourse(CourseRequest courseRequest, User mentor) {
        // Validate input
        inputValidator.validateCourseTitle(courseRequest.getName());
        inputValidator.validateCourseDescription(courseRequest.getDescription());
        
        Course course = new Course();
        course.setName(inputValidator.sanitizeInput(courseRequest.getName()));
        course.setDescription(inputValidator.sanitizeInput(courseRequest.getDescription()));
        course.setCategory(inputValidator.sanitizeInput(courseRequest.getCategory()));
        course.setDuration(courseRequest.getDuration());
        course.setMentor(mentor);
        
        Course savedCourse = courseRepository.save(course);
        
        // Audit log
        auditService.logCourseCreation(mentor.getId(), savedCourse.getId());
        
        return savedCourse;
    }

    @Override
    @Transactional
    public void addModuleToCourse(Long courseId, String moduleTitle, MultipartFile videoFile, int duration, User currentUser) {
        Course course = getCourseById(courseId);
        
        // Authorization check - only course mentor can add modules
        if (!course.getMentor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the course mentor can add modules");
        }
        
        inputValidator.validateCourseTitle(moduleTitle);
        
        String videoUrl = fileStorageService.uploadVideoFile(videoFile);
        
        CourseModule module = new CourseModule();
        module.setCourse(course);
        module.setTitle(inputValidator.sanitizeInput(moduleTitle));
        module.setDuration(duration);
        module.setSessionVideoUrl(videoUrl);
        
        courseModuleRepository.save(module);
        
        // Audit log
        auditService.logModuleAddition(currentUser.getId(), courseId, module.getId());
    }

    @Override
    @Transactional
    public void addAssignmentToCourse(Long courseId, String assignmentTitle, MultipartFile assignmentFile, User currentUser) {
        Course course = getCourseById(courseId);
        
        // Authorization check
        if (!course.getMentor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the course mentor can add assignments");
        }
        
        inputValidator.validateCourseTitle(assignmentTitle);
        
        String assignmentUrl = fileStorageService.uploadDocumentFile(assignmentFile);
        
        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setTitle(inputValidator.sanitizeInput(assignmentTitle));
        assignment.setAssignmentFileUrl(assignmentUrl);
        
        assignmentRepository.save(assignment);
        
        // Audit log
        auditService.logAssignmentAddition(currentUser.getId(), courseId, assignment.getId());
    }

    @Override
    @Transactional
    public void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        // Check if user is enrolled in the course
        if (!user.getEnrolledCourses().contains(assignment.getCourse())) {
            throw new AccessDeniedException("User is not enrolled in this course");
        }
        
        String answerFileUrl = fileStorageService.uploadDocumentFile(answerFile);
        assignment.setStudentAnswerFileUrl(answerFileUrl);
        assignment.setSubmittedByUserId(userId);
        
        assignmentRepository.save(assignment);
        
        // Audit log
        auditService.logAssignmentSubmission(userId, assignmentId);
    }

    @Override
    @Transactional
    public void gradeAssignment(Long assignmentId, int grade, User currentUser) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        
        // Authorization check - only course mentor can grade
        if (!assignment.getCourse().getMentor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the course mentor can grade assignments");
        }
        
        inputValidator.validateGrade(grade);
        
        Integer oldGrade = assignment.getGrade();
        assignment.setGrade(grade);
        assignmentRepository.save(assignment);
        
        // Audit log
        auditService.logGradeChange(currentUser.getId(), assignmentId, oldGrade, grade);
    }

    @Override
    @Transactional
    public void enrollUserInCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Course course = getCourseById(courseId);
        
        // Check if already enrolled
        if (user.getEnrolledCourses().contains(course)) {
            throw new IllegalStateException("User is already enrolled in this course");
        }
        
        user.getEnrolledCourses().add(course);
        course.getEnrolledUsers().add(user);
        
        userRepository.save(user);
        
        // Audit log
        auditService.logEnrollment(userId, courseId);
    }

    @Override
    @Transactional
    public void deEnrollStudentFromCourse(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = getCourseById(courseId);
        
        student.getEnrolledCourses().remove(course);
        course.getEnrolledUsers().remove(student);
        
        userRepository.save(student);
        
        // Audit log
        auditService.logDeEnrollment(studentId, courseId);
    }

    @Override
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
    }

    @Override
    public List<Course> getCoursesByMentorId(Long mentorId) {
        return courseRepository.findByMentorId(mentorId);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id, User currentUser) {
        Course course = getCourseById(id);
        
        // Authorization check
        if (!course.getMentor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the course mentor can delete the course");
        }
        
        // Soft delete implementation
        course.setDeleted(true);
        course.setDeletedAt(java.time.LocalDateTime.now());
        course.setDeletedBy(currentUser.getId());
        
        courseRepository.save(course);
        
        // Audit log
        auditService.logCourseDeletion(currentUser.getId(), id);
    }

    @Override
    public Course updateCourse(Long id, CourseRequest courseRequest, User currentUser) {
        Course course = getCourseById(id);
        
        // Authorization check
        if (!course.getMentor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the course mentor can update the course");
        }
        
        inputValidator.validateCourseTitle(courseRequest.getName());
        inputValidator.validateCourseDescription(courseRequest.getDescription());
        
        course.setName(inputValidator.sanitizeInput(courseRequest.getName()));
        course.setDescription(inputValidator.sanitizeInput(courseRequest.getDescription()));
        course.setCategory(inputValidator.sanitizeInput(courseRequest.getCategory()));
        course.setDuration(courseRequest.getDuration());
        
        Course updatedCourse = courseRepository.save(course);
        
        // Audit log
        auditService.logCourseUpdate(currentUser.getId(), id);
        
        return updatedCourse;
    }

    @Override
    public List<Map<String, Object>> getEnrollmentStats() {
        return courseRepository.getEnrollmentStats();
    }

    @Override
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    @Override
    public List<Course> searchCoursesByName(String keyword) {
        return courseRepository.findByNameContainingIgnoreCase(keyword);
    }
}