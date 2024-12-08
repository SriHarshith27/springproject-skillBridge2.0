<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Enrolled Courses</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/enrolled-courses.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.2/gsap.min.js"></script>
</head>
<body>

<%-- Include User Navbar --%>
<%@ include file="/WEB-INF/views/user/user-navbar.jsp" %>

<div class="container">
    <h1 class="page-title">Your Enrolled Courses</h1>
    
		    <%-- Success and Error Messages --%>
		<c:if test="${not empty successMessage}">
		    <div class="success-message">${successMessage}</div>
		</c:if>
		<c:if test="${not empty errorMessage}">
		    <div class="error-message">${errorMessage}</div>
		</c:if>
		    

    <c:if test="${empty enrolledCourses}">
        <p class="empty-message">You haven't enrolled in any courses yet.</p>
    </c:if>

    <c:if test="${not empty enrolledCourses}">
        <div class="courses-grid">
            <c:forEach var="course" items="${enrolledCourses}">
                <div class="course-card">
                    <h2 class="course-name">${course.name}</h2>
                    <p class="course-description">${course.description}</p>
                    <div class="course-duration">
                        <svg class="duration-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                        ${course.duration} Hours
                    </div>
                    <!-- Link to View Course Progress -->
                    <a href="${pageContext.request.contextPath}/user/course-progress/${course.id}" class="btn-manage-course">View Course</a>
                </div>
            </c:forEach>
        </div>
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
