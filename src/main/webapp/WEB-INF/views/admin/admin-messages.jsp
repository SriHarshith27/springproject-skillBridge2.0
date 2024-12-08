<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Support Messages</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-messages.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
</head>
<body>
    <%@ include file="admin-navbar.jsp" %>

    <div class="container">
        <h1>Support Messages</h1>

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
                        <th>Name</th>
                        <th>Subject</th>
                        <th>Message</th>
                        <th>Reply</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="message" items="${messages}">
                        <tr>
                            <td>${message.name}</td>
                            <td>${message.subject}</td>
                            <td>${message.message}</td>
                            <td>${message.adminReply != null ? message.adminReply : "No reply yet"}</td>
                            <td>
                                <!-- If no reply, show form -->
                                <c:if test="${message.adminReply == null}">
                                    <form action="${pageContext.request.contextPath}/support/admin/reply/${message.id}" method="post">
                                        <input type="text" name="reply" placeholder="Write a reply" required>
                                        <button type="submit">Send</button>
                                    </form>
                                </c:if>
                                
                                <!-- If reply exists, show 'Reply sent' -->
                                <c:if test="${message.adminReply != null}">
                                    <span class="replied-message">Reply sent</span>
                                </c:if>
                            </td>
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
