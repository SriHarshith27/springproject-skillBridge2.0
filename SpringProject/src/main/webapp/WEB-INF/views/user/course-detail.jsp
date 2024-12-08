<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Details - ${course.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/course-detail.css">
</head>
<body>

<%@ include file="/WEB-INF/views/user/user-navbar.jsp" %>

<div class="container">
    <h1 class="page-title">Course: ${course.name}</h1>

    <h2>Sessions</h2>
    <c:forEach var="session" items="${sessions}">
        <div class="session-card">
            <h3>${session.title}</h3>
            <p>Duration: ${session.duration} minutes</p>
            <video width="400" controls>
                <source src="${pageContext.request.contextPath}/uploads/videos/${session.videoPath}" type="video/mp4">
                Your browser does not support the video tag.
            </video>
        </div>
    </c:forEach>

    <h2>Assignments</h2>
    <c:forEach var="assignment" items="${assignments}">
        <div class="assignment-card">
            <h3>${assignment.title}</h3>
            <a href="${pageContext.request.contextPath}/uploads/assignments/${assignment.filePath}" download>Download Assignment</a>

            <!-- Submission form for users -->
            <form action="${pageContext.request.contextPath}/user/submitAssignment" method="post" enctype="multipart/form-data">
                <input type="hidden" name="courseId" value="${course.id}" />
                <input type="hidden" name="assignmentId" value="${assignment.id}" />
                
                <label for="submissionFile">Upload Your Submission:</label>
                <input type="file" name="submissionFile" required />
                
                <button type="submit">Submit Assignment</button>
            </form>
        </div>
    </c:forEach>
</div>

</body>
</html>

