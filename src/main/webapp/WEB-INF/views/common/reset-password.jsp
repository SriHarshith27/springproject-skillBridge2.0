<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgetpassword.css">
</head>
<body>

    <div class="reset-password-container">
        <h1>Reset Password</h1>

        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/reset-password" method="post">
            <input type="hidden" name="token" value="${resetToken}" />
            <div class="form-group">
                <label for="password">New Password:</label>
                <input type="password" id="password" name="password" required placeholder="Enter your new password">
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="Confirm your new password">
            </div>
            <button type="submit" class="btn-submit">Reset Password</button>
        </form>
    </div>
</body>
</html>
