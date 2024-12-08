<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/profile.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/edit-profile.css">
</head>
<body>

<%@ include file="/WEB-INF/views/admin/admin-navbar.jsp" %>

<div class="container">
    <h1>Edit Profile</h1>

    <form action="<%= request.getContextPath() %>/profile/update" method="post">
        <!-- Success and Error Messages -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <!-- Username -->
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" value="${username}">
        </div>

        <!-- Email -->
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${email}">
        </div>

        <!-- Phone -->
        <div class="form-group">
            <label for="phone">Phone</label>
            <input type="text" id="phone" name="phone" value="${phone}">
        </div>

        <!-- Current Password -->
        <div class="form-group">
            <label for="currentPassword">Current Password</label>
            <input type="password" id="currentPassword" name="currentPassword" required>
        </div>

        <!-- New Password -->
        <div class="form-group">
            <label for="newPassword">New Password</label>
            <input type="password" id="newPassword" name="newPassword">
            <p class="note">New password is optional and can be left empty.</p>
        </div>

        <button type="submit" class="btn-save">Save Changes</button>
    </form>
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
</script>

</body>
</html>
