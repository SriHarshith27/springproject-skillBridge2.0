<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%
    // Check if the user has the 'MENTOR' role
    String role = (String) session.getAttribute("role");
    if (role == null || !"MENTOR".equalsIgnoreCase(role)) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Course </title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/create-course.css">
</head>
<body>

<%-- Include Mentor Navbar --%>
<%@ include file="/WEB-INF/views/mentor/mentor-navbar.jsp" %>

<div class="container animate__animated animate__fadeIn">
    <h1 class="animate__animated animate__slideInDown">Create a New Course</h1>
    <form action="<%= request.getContextPath() %>/mentor/create-course" method="post">
        <div class="form-group">
            <label for="name">Course Name</label>
            <input type="text" id="name" name="name" required placeholder="Enter course name">
        </div>
        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" required placeholder="Enter course description"></textarea>
        </div>
        <div class="form-group">
            <label for="duration">Duration (hours)</label>
            <input type="number" id="duration" name="duration" required placeholder="Enter course duration">
        </div>
        <button type="submit" class="create-course-btn">Create Course</button>
    </form>
</div>

</body>
</html>
