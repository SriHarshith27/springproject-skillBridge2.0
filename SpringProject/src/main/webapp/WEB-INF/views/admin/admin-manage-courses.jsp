<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Manage Courses</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-manage-courses.css">
</head>
<body>
    <%@ include file="admin-navbar.jsp" %>

    <div class="container">
        <h1>Manage Courses</h1>

        <!-- Success and Error Messages -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                <strong>Success:</strong> ${successMessage}
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                <strong>Error:</strong> ${errorMessage}
            </div>
        </c:if>

        <!-- Courses Table -->
        <c:if test="${not empty courses}">
            <table class="courses-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Mentor</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="course" items="${courses}">
                        <tr>
                            <td>${course.name}</td>
                            <td>${course.description}</td>
                            <td>${course.mentor != null ? course.mentor.username : 'No Mentor Assigned'}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/delete-course/${course.id}" method="post">
                                    <input type="text" name="reason" placeholder="Reason for deletion" required>
                                    <button type="submit">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- No Courses Available -->
        <c:if test="${empty courses}">
            <p>No courses found.</p>
        </c:if>
    </div>

    <script>
        // Optional: Automatically hide success or error messages after a few seconds
        setTimeout(() => {
            document.querySelectorAll('.success-message, .error-message').forEach(el => el.style.display = 'none');
        }, 5000);
    </script>
</body>
</html>
