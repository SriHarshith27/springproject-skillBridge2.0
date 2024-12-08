<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mentor Help Center</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/help-center.css">
</head>
<body>

<%-- Include the mentor-specific navbar --%>
<%
    request.getRequestDispatcher("/WEB-INF/views/mentor/mentor-navbar.jsp").include(request, response);
%>

<div class="container">
    <header>
        <h1>Mentor Help Center</h1>
        <p>Your resource for effectively managing courses, interacting with students, and maximizing your impact on the platform.</p>
    </header>

    <section class="quick-start">
        <h2>Quick Start Guide</h2>
        <p>New to mentoring? Our quick start guide will introduce you to platform features and help you start managing courses with ease.</p>
    </section>

    <div class="accordion">
        <div class="accordion-item">
            <div class="accordion-header">
                Account & Profile Management
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Creating a Mentor Profile</h3>
                    <p>Steps to create and set up your mentor profile on our platform.</p>
                </div>
                <div class="sub-section">
                    <h3>Updating Profile Information</h3>
                    <p>How to update your profile, experience, and qualifications.</p>
                </div>
                <div class="sub-section">
                    <h3>Password Recovery</h3>
                    <p>Secure steps to recover or reset your account password.</p>
                </div>
            </div>
        </div>

        <div class="accordion-item">
            <div class="accordion-header">
                Course Management
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Creating and Uploading Courses</h3>
                    <p>Guidelines for creating, uploading, and structuring course content.</p>
                </div>
                <div class="sub-section">
                    <h3>Updating or Deleting Courses</h3>
                    <p>Steps for editing or removing existing courses on the platform.</p>
                </div>
            </div>
        </div>

        <div class="accordion-item">
            <div class="accordion-header">
                Student Interaction
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Viewing Enrolled Students</h3>
                    <p>Access the list of students who have enrolled in your courses.</p>
                </div>
                <div class="sub-section">
                    <h3>Communicating with Students</h3>
                    <p>Tips for providing feedback and answering student questions.</p>
                </div>
            </div>
        </div>

        <div class="accordion-item">
            <div class="accordion-header">
                Assessments & Feedback
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Creating Quizzes and Assignments</h3>
                    <p>How to create and manage quizzes and assignments within courses.</p>
                </div>
                <div class="sub-section">
                    <h3>Providing Feedback on Submissions</h3>
                    <p>Best practices for reviewing and providing feedback to students.</p>
                </div>
            </div>
        </div>

        <div class="accordion-item">
            <div class="accordion-header">
                Earnings & Withdrawal
                <i class="fas fa-chevron-down icon"></i>
            </div>
            <div class="accordion-content">
                <div class="sub-section">
                    <h3>Understanding the Earnings Structure</h3>
                    <p>Information on how earnings are calculated based on enrollments.</p>
                </div>
                <div class="sub-section">
                    <h3>Requesting Withdrawals</h3>
                    <p>Steps to check your balance and request withdrawals securely.</p>
                </div>
            </div>
        </div>
    </div>

    <section class="contact-section">
        <h2>Need Additional Help?</h2>
        <p>Our support team is available to assist you with any questions or concerns you may have as a mentor.</p>
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
