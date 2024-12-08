<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- Check if the user has the 'USER' role --%>
<%
    String role = (String) session.getAttribute("role");
    if (!"USER".equalsIgnoreCase(role)) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
</head>
<body>

<%@ include file="user-navbar.jsp" %>

<div class="profile-container">
    <div class="profile-card">
        <div class="profile-header">
            <img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" alt="Profile Picture" class="profile-pic">
            <h2 class="profile-name">${username}</h2>
            <p class="profile-role">${role}</p>
            <button class="edit-profile-btn" onclick="editProfile()" style="position: relative; z-index: 1000;">Edit Profile</button>
        </div>
        <div class="profile-info">
            <h3>Information</h3>
            <div class="info-section">
                <p>Email: ${email != null ? email : "N/A"}</p>
                <p>Phone: ${phone != null ? phone : "N/A"}</p>
            </div>
        </div>
    </div>
</div>

<script>
    function logout() {
        fetch('<%= request.getContextPath() %>/auth/logout', {
            method: 'POST',
            credentials: 'include'
        }).then(response => {
            if (response.ok) {
                window.location.href = '<%= request.getContextPath() %>/auth/login';
            } else {
                alert('Error during logout');
            }
        }).catch(error => {
            console.error('Logout failed:', error);
        });
    }

    function editProfile() {
        window.location.href = '<%= request.getContextPath() %>/profile/user/edit-profile';
    }
</script>
</body>
</html>
