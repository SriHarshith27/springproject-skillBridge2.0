<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
String role = (String) session.getAttribute("role");

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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Support Form</title>

    <!-- Existing Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/support-form.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
</head>
<body>
    <%-- Navbar inclusion based on the role --%>
    <%
    if ("ADMIN".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-navbar.jsp").include(request, response);
    } else if ("USER".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/user/user-navbar.jsp").include(request, response);
    } else if ("MENTOR".equalsIgnoreCase(role)) {
        request.getRequestDispatcher("/WEB-INF/views/mentor/mentor-navbar.jsp").include(request, response);
    }
    %>

    <div class="support-container">
        <h1>Support Form</h1>

        <%-- Success Message Display --%>
        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>

        <%-- Error Message Display --%>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <%-- Form Submission --%>
        <form action="${pageContext.request.contextPath}/support/submit" method="post">
            <div class="form-group">
                <label for="name">Your Name</label>
                <input type="text" id="name" name="name" required 
                       placeholder="Enter your full name" 
                       pattern="[A-Za-z\s]+" 
                       title="Name should contain only letters and spaces">
            </div>

            <div class="form-group">
                <label for="subject">Subject</label>
                <input type="text" id="subject" name="subject" required 
                       placeholder="Brief description of your issue" 
                       maxlength="100">
            </div>

            <div class="form-group">
                <label for="message">Message</label>
                <textarea id="message" name="message" required 
                          placeholder="Provide detailed information about your support request" 
                          minlength="10" 
                          maxlength="500"></textarea>
            </div>

            <button type="submit">Submit Support Ticket</button>
        </form>
    </div>

    <!-- JavaScript for real-time success message display -->
    <script>
        const urlParams = new URLSearchParams(window.location.search);
        const successMessage = urlParams.get('successMessage');

        if (successMessage) {
            const successDiv = document.createElement('div');
            successDiv.className = 'success-message';
            successDiv.innerText = successMessage;

            document.querySelector('.support-container').prepend(successDiv);

            // Optionally remove the message after a timeout
            setTimeout(() => successDiv.remove(), 5000);
        }
    </script>
</body>
</html>
