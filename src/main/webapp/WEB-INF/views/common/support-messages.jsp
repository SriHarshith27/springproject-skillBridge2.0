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
<html>
<head>
    <title>My Support Messages</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/support-messages.css">
</head>
<body>
	
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
        <h1>My Support Messages</h1>

        <!-- Success and Error Messages -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <!-- Display Messages -->
        <c:if test="${not empty messages}">
            <table>
                <thead>
                    <tr>
                        <th>Subject</th>
                        <th>Message</th>
                        <th>Admin Reply</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="message" items="${messages}">
                        <tr>
                            <td>${message.subject}</td>
                            <td>${message.message}</td>
                            <td>${message.adminReply != null ? message.adminReply : "No reply yet"}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- No Messages Available -->
        <c:if test="${empty messages}">
            <p>No messages found.</p>
        </c:if>
    </div>
</body>
</html>
