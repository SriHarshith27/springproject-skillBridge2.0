<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Courses</title>
    
    <!-- Link to stylesheets using pageContext.request.contextPath -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage-courses.css">
    
    <!-- Include GSAP for animations -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.2/gsap.min.js"></script>
</head>
<body>
    
    <%@ include file="mentor-navbar.jsp" %>

    <div class="container">
        <h1 class="page-title">Your Courses</h1>

        <!-- Display success or error messages -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <c:if test="${not empty courses}">
            <div class="courses-grid">
                <c:forEach var="course" items="${courses}">
                    <div class="course-card">
                        <h2 class="course-name">${course.name}</h2>
                        <p class="course-description">${course.description}</p>
                        <div class="course-duration">
                            <svg class="duration-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                            ${course.duration} Hours
                        </div>
                        <a href="${pageContext.request.contextPath}/mentor/manage-course/${course.id}" class="btn-manage-course">Manage Course</a>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${empty courses}">
            <p class="empty-message">You have not created any courses yet.</p>
            <a href="${pageContext.request.contextPath}/mentor/create-course" class="btn-create-course">Create a New Course</a>
        </c:if>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            // Animate title
            gsap.to('.page-title', {
                duration: 1,
                opacity: 1,
                y: 0,
                ease: 'power3.out'
            });

            // Animate course cards
            gsap.to('.course-card', {
                duration: 0.8,
                opacity: 1,
                y: 0,
                stagger: 0.2,
                ease: 'power3.out',
                delay: 0.3
            });

            // Add hover animation to cards
            document.querySelectorAll('.course-card').forEach(card => {
                card.addEventListener('mouseenter', () => {
                    gsap.to(card, {
                        duration: 0.3,
                        scale: 1.02,
                        y: -5,
                        ease: 'power2.out'
                    });
                });

                card.addEventListener('mouseleave', () => {
                    gsap.to(card, {
                        duration: 0.3,
                        scale: 1,
                        y: 0,
                        ease: 'power2.out'
                    });
                });
            });
        });
    </script>
</body>
</html>
