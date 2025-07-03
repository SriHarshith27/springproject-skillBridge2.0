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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    // --- NEWLY IMPLEMENTED METHODS ---

    @Override
    @Transactional
    public Course createCourse(CourseRequest courseRequest, User mentor) {
        Course course = new Course();
        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        course.setCategory(courseRequest.getCategory());
        course.setDuration(courseRequest.getDuration());
        course.setMentor(mentor);
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void gradeAssignment(Long assignmentId, int grade) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with id: " + assignmentId));
        assignment.setGrade(grade);
        assignmentRepository.save(assignment);
    }

    @Override
    public Course updateCourse(Long id, CourseRequest courseRequest) {
        Course course = getCourseById(id);
        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        course.setCategory(courseRequest.getCategory());
        course.setDuration(courseRequest.getDuration());
        return courseRepository.save(course);
    }

    // --- EXISTING REFACTORED METHODS ---

    @Override
    @Transactional
    public void addModuleToCourse(Long courseId, String moduleTitle, MultipartFile videoFile, int duration) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        String videoUrl = fileStorageService.uploadFile(videoFile);
        CourseModule module = new CourseModule();
        module.setCourse(course);
        module.setTitle(moduleTitle);
        module.setDuration(duration);
        module.setSessionVideoUrl(videoUrl);
        course.getModules().add(module);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void addAssignmentToCourse(Long courseId, String assignmentTitle, MultipartFile assignmentFile) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found for ID: " + courseId));
        String assignmentUrl = fileStorageService.uploadFile(assignmentFile);
        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setTitle(assignmentTitle);
        assignment.setAssignmentFileUrl(assignmentUrl);
        course.getAssignments().add(assignment);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String answerFileUrl = fileStorageService.uploadFile(answerFile);
        assignment.setStudentAnswerFileUrl(answerFileUrl);
        assignment.setSubmittedByUserId(userId);
        assignmentRepository.save(assignment);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
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
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void enrollUserInCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        user.getEnrolledCourses().add(course);
        course.getEnrolledUsers().add(user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deEnrollStudentFromCourse(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        student.getEnrolledCourses().remove(course);
        course.getEnrolledUsers().remove(student);
        userRepository.save(student);
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
