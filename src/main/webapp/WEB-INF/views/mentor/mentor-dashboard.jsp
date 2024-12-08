<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="java.io.IOException" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);


    if (session == null || session.getAttribute("username") == null || !"MENTOR".equalsIgnoreCase((String) session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mentor Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

    <%-- Include Mentor Navbar --%>
    <jsp:include page="/WEB-INF/views/mentor/mentor-navbar.jsp" />

    <div class="content">
        <h1>Welcome to the Mentor Dashboard</h1>
        <p>Here you can manage courses and interact with students.</p>
    </div>

    <script>
        // Ensure that `sidebar` and `toggleBtn` elements exist
        const sidebar = document.getElementById('sidebar');
        const toggleBtn = document.getElementById('toggleBtn');

        if (sidebar && toggleBtn) {
            toggleBtn.addEventListener('click', () => {
                sidebar.classList.toggle('collapsed');
                toggleBtn.classList.toggle('collapsed');
            });
        }
    </script>

    <script>
        // Logout function
        async function logout() {
            try {
                const response = await fetch('<%= request.getContextPath() %>/auth/logout', {
                    method: 'POST',
                    credentials: 'include'
                });
                if (response.ok) {
                    window.location.href = '<%= request.getContextPath() %>/auth/login';
                } else {
                    alert('Error during logout');
                }
            } catch (error) {
                console.error('Logout failed:', error);
            }
        }
    </script>

</body>
</html>

