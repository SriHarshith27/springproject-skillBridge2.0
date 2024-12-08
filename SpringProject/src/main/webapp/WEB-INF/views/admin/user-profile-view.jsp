<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/profile.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
</head>
<body>

<%@ include file="admin-navbar.jsp" %>

<div class="profile-container">
    <div class="profile-card">
        <div class="profile-header">
            <img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" alt="Profile Picture" class="profile-pic">
            <h2 class="profile-name">${username}</h2>
            <p class="profile-role">${role}</p>
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

</body>
</html>
