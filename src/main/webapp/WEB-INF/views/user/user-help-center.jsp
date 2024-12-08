<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Help Center</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/help-center.css">
</head>
<body>

<%-- Include the navbar based on the user's role --%>
<%
    String role = (String) session.getAttribute("role");
    if ("USER".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/user/user-navbar.jsp").include(request, response);
    } else if ("MENTOR".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/mentor/mentor-navbar.jsp").include(request, response);
    }
%>

<div class="container">
    <header>
        <h1>Student Help Center</h1>
        <p>Your comprehensive guide to mastering the learning platform. Find answers, get support, and excel in your educational journey.</p>
    </header>

    <section class="quick-start">
        <h2>Quick Start Guide</h2>
        <p>New to the platform? Our quick start guide will help you understand the essentials and get you started on your learning journey in minutes.</p>
    </section>

    <div class="accordion">
        <div class="accordion-item">
            <div class="accordion-header">
                Account & Profile Management
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Creating an Account</h3>
                    <p>Simple steps to create and set up your learning account with our platform.</p>
                </div>
                <div class="sub-section">
                    <h3>Profile Updates</h3>
                    <p>Customize your profile and keep your information current.</p>
                </div>
                <div class="sub-section">
                    <h3>Password Recovery</h3>
                    <p>Secure steps to recover or reset your account password.</p>
                </div>
            </div>
        </div>

        <div class="accordion-item">
            <div class="accordion-header">
                Enrolling in Courses
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Browse Courses</h3>
                    <p>Discover and explore our extensive course catalog.</p>
                </div>
                <div class="sub-section">
                    <h3>Enrollment Process</h3>
                    <p>Easy-to-follow guide for course enrollment and registration.</p>
                </div>
            </div>
        </div>

        <div class="accordion-item">
            <div class="accordion-header">
                Course Navigation & Learning Path
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Accessing Courses</h3>
                    <p>Learn to navigate and access your enrolled courses efficiently.</p>
                </div>
                <div class="sub-section">
                    <h3>Course Components</h3>
                    <p>Understanding course materials, assignments, and assessments.</p>
                </div>
            </div>
        </div>

        <div class="accordion-item">
            <div class="accordion-header">
                Progress Tracking
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>View Progress</h3>
                    <p>Monitor your learning progress and course completion status.</p>
                </div>
                <div class="sub-section">
                    <h3>Certificates</h3>
                    <p>Guidelines for earning and downloading course certificates.</p>
                </div>
            </div>
        </div>
    </div>

    <section class="contact-section">
        <h2>Need Additional Help?</h2>
        <p>Our dedicated support team is ready to assist you with any questions or concerns you may have.</p>
        <a href="<%= request.getContextPath() %>/support/form" class="btn">Contact Support</a>
    </section>
</div>

<script>
    document.querySelectorAll('.accordion-header').forEach(button => {
        button.addEventListener('click', () => {
            const content = button.nextElementSibling;
            const icon = button.querySelector('.icon');
            
            // Close all other accordion items
            document.querySelectorAll('.accordion-content').forEach(item => {
                if (item !== content && item.classList.contains('active')) {
                    item.classList.remove('active');
                    item.previousElementSibling.querySelector('.icon').classList.remove('rotate');
                }
            });
            
            // Toggle current accordion item
            content.classList.toggle('active');
            icon.classList.toggle('rotate');
        });
    });
</script>

</body>
</html>
