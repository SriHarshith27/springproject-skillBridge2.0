<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Progress - ${course.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/course-progress.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <script>
        // Section navigation script
        document.addEventListener('DOMContentLoaded', () => {
            document.querySelectorAll('.section-nav button').forEach(button => {
                button.addEventListener('click', () => {
                    // Remove active class from all buttons and sections
                    document.querySelectorAll('.section-nav button').forEach(b => b.classList.remove('active'));
                    document.querySelectorAll('.section').forEach(s => {
                        s.classList.remove('active');
                        s.classList.add('inactive');
                    });
                    
                    // Add active class to clicked button and corresponding section
                    button.classList.add('active');
                    const sectionId = button.dataset.section;
                    document.getElementById(sectionId).classList.remove('inactive');
                    document.getElementById(sectionId).classList.add('active');
                });
            });
        });
    </script>
</head>
<body>
    <%@ include file="user-navbar.jsp" %>

    <div class="container">
        <!-- Success and Error Messages -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <h1 class="page-title">Course Progress: ${course.name}</h1>

        <!-- Section Navigation -->
        <div class="section-nav">
            <button class="active" data-section="video-sessions">Video Sessions</button>
            <button data-section="assignments">Assignments</button>
            <button data-section="grades">Grades</button>
        </div>

		<!-- De-enroll Button -->
<form action="${pageContext.request.contextPath}/user/de-enroll-course/${course.id}" method="post" class="de-enroll-form">
    <button type="submit" class="de-enroll-button" 
            onclick="return confirm('Are you sure you want to de-enroll from this course?')">
        De-enroll from Course
    </button>
</form>
		

<!-- Video Sessions Section -->
<div id="video-sessions" class="section active">
    <h2>Video Sessions</h2>
    <c:if test="${not empty modules}">
        <ul>
            <c:forEach items="${modules}" var="module">
                <li>
                    <h3>${module.title} (${module.duration} minutes)</h3>
                    <video width="600" controls>
                        <source src="${pageContext.request.contextPath}/courses/video-session/${module.id}" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${empty modules}">
        <p>No sessions available yet.</p>
    </c:if>
</div>



     <!-- Assignments Section -->
<div id="assignments" class="section inactive">
    <h2>Assignments</h2>
    <c:if test="${not empty assignments}">
        <ul>
            <c:forEach items="${assignments}" var="assignment">
                <li>
                    <h3>${assignment.title}</h3>
                    <a href="${pageContext.request.contextPath}/user/download-assignment/${assignment.id}" class="download-button">
                        Download Assignment
                    </a>
                    <form action="${pageContext.request.contextPath}/user/upload-assignment" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="assignmentId" value="${assignment.id}" />
                        <label for="answerFile-${assignment.id}">Upload Answer:</label>
                        <input type="file" name="answerFile" id="answerFile-${assignment.id}" required />
                        <button type="submit" class="submit-button">Submit Answer</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${empty assignments}">
        <p>No assignments available yet.</p>
    </c:if>
</div>


<div id="grades" class="section inactive">
    <h2>Grades</h2>
    <c:if test="${not empty assignments}">
        <ul>
            <c:forEach items="${assignments}" var="assignment">
                <li>
                    <h3>${assignment.title}</h3>
                    <p>Grade: ${assignment.grade != null ? assignment.grade : "Not graded yet"}</p>
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${empty assignments}">
        <p>No grades available yet.</p>
    </c:if>
</div>


        </div>
    </div>
</body>
</html>