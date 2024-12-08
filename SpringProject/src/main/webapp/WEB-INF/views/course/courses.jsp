<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String role = (String) session.getAttribute("role");
    System.out.println("Role in session: " + role);

    if (role == null) {
        // Redirect to login page if role is not set
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Courses</title>
    <!-- Include both the navbar and courses CSS files -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/courses.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>

<%-- Conditionally include the navbar based on the role using JSP scriptlets --%>
<%
    if ("ADMIN".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-navbar.jsp").include(request, response);
    } else if ("USER".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/user/user-navbar.jsp").include(request, response);
    } else if ("MENTOR".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/mentor/mentor-navbar.jsp").include(request, response);
    }
%>

<div class="container">
    <h1>Available Courses</h1>

    <!-- Category Filter -->
    <div class="category-filter">
        <label for="categorySelect">Filter by Category:</label>
        <form action="${pageContext.request.contextPath}/courses" method="get" id="categoryFilterForm">
            <select name="category" id="categorySelect" onchange="document.getElementById('categoryFilterForm').submit();">
                <option value="" ${empty selectedCategory ? 'selected' : ''}>All Categories</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category}" ${selectedCategory == category ? 'selected' : ''}>
                        ${category}
                    </option>
                </c:forEach>
            </select>
        </form>
    </div>

    <!-- Courses Table -->
    <c:if test="${empty courses}">
        <p>No courses available for the selected category right now.</p>
    </c:if>

    <c:if test="${role == 'USER'}">
        <a href="${pageContext.request.contextPath}/courses/enrolled-courses" class="view-enrolled-btn">View Enrolled Courses</a>
    </c:if>

    <c:if test="${not empty courses}">
        <table>
            <tr>
                <th>Course Name</th>
                <th>Description</th>
                <th>Category</th>
                <th>Duration (hours)</th>
                <c:if test="${role == 'USER'}">
                    <th>Action</th>
                </c:if>
            </tr>
            <c:forEach var="course" items="${courses}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/courses/${course.id}">${course.name}</a></td>
                    <td>${course.description}</td>
                    <td>${course.category}</td>
                    <td>${course.duration}</td>
                    <c:if test="${role == 'USER'}">
                        <td>
                            <form action="${pageContext.request.contextPath}/courses/enroll" method="post">
                                <input type="hidden" name="courseId" value="${course.id}" />
                                <button type="submit" class="enroll-btn">Enroll</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</div>

</body>
</html>
