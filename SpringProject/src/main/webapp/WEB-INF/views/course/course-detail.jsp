<%
    String role = (String) session.getAttribute("role");
    if (role == null) {
        // Redirect if role is missing from the session
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Detail</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/courses.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    
</head>
<body>

<%-- Conditionally include the navbar based on the role --%>
<%
    if ("admin".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-navbar.jsp").include(request, response);
    } else if ("user".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/user/user-navbar.jsp").include(request, response);
    }
%>

<div class="container course-detail">
    <h2>Course Details</h2>
    <p><strong>Name:</strong> ${course.name}</p>
    <p><strong>Description:</strong> ${course.description}</p>
    <p><strong>Duration:</strong> ${course.duration} hours</p>
</div>

</body>
</html>
  
  
  
  