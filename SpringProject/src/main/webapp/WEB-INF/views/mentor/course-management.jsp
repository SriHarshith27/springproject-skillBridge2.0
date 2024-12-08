<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Course - ${course.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/course-management.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.2/gsap.min.js"></script>
</head>
<body>
    
    <%@ include file="mentor-navbar.jsp" %>

    <div class="container">
        <h1 class="page-title">Manage Course: ${course.name}</h1>

        <!-- Display success or error messages -->
        <c:if test="${not empty message}">
            <div class="success-message">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <a href="${pageContext.request.contextPath}/mentor/manage-courses" class="btn-back">Back to Manage Courses</a>
        
        <!-- Add Delete Course Button -->
        <div class="delete-course-section">
            <form action="${pageContext.request.contextPath}/mentor/delete-course/${course.id}" method="post" class="delete-course-form">
                <button type="submit" class="btn-delete" 
                    onclick="return confirm('Are you sure you want to delete this course? This action cannot be undone.')">
                    Delete Course
                </button>
            </form>
        </div>
        
        <!-- Navigation tabs for managing different sections of the course -->
        <div class="tabs">
            <button onclick="showSection('sessions')" class="active">Schedule Sessions</button>
            <button onclick="showSection('assignments')">Assignments</button>
            <button onclick="showSection('grading')">Grade Submissions</button>
        </div>

        <!-- Section for scheduling sessions -->
        <div id="sessions" class="section">
            <h2>Schedule Sessions</h2>
            <form action="${pageContext.request.contextPath}/mentor/uploadVideoSession" method="post" enctype="multipart/form-data">
                <input type="hidden" name="courseId" value="${course.id}" />
                
                <div class="form-group">
                    <label for="moduleTitle">Session Title:</label>
                    <input type="text" id="moduleTitle" name="moduleTitle" placeholder="Enter session title" required />
                </div>

                <div class="form-group">
                    <label for="videoFile">Upload Video:</label>
                    <input type="file" id="videoFile" name="videoFile" accept="video/*" required />
                </div>

                <div class="form-group">
                    <label for="moduleDuration">Duration (minutes):</label>
                    <input type="number" id="moduleDuration" name="moduleDuration" placeholder="Enter duration" required />
                </div>

                <button type="submit" class="btn-upload">Upload Session</button>
            </form>

            <!-- Existing Video Sessions -->
            <h3>Existing Video Sessions</h3>
            <c:if test="${not empty modules}">
                <ul>
                    <c:forEach items="${modules}" var="module">
                        <li>
                            <h4>${module.title} (${module.duration} minutes)</h4>
                            <video width="600" controls>
                                <source src="${pageContext.request.contextPath}/courses/video-session/${module.id}" type="video/mp4">
                                Your browser does not support the video tag.
                            </video>
					 <form action="${pageContext.request.contextPath}/mentor/deleteVideoSession/${module.id}" method="post" class="delete-form">
					    <button type="submit" class="btn-delete" 
					        onclick="return confirm('Are you sure you want to delete this video session?')">
					        Delete Session
					    </button>
					</form>

                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${empty modules}">
                <p>No sessions available yet.</p>
            </c:if>
        </div>

        <!-- Section for adding assignments -->
        <div id="assignments" class="section" style="display: none;">
            <h2>Assignments</h2>
            <form action="${pageContext.request.contextPath}/mentor/uploadAssignment" method="post" enctype="multipart/form-data">
                <input type="hidden" name="courseId" value="${course.id}" />
                
                <div class="form-group">
                    <label for="assignmentTitle">Assignment Title:</label>
                    <input type="text" id="assignmentTitle" name="assignmentTitle" placeholder="Enter assignment title" required />
                </div>

                <div class="form-group">
                    <label for="assignmentFile">Upload Assignment:</label>
                    <input type="file" id="assignmentFile" name="assignmentFile" required />
                </div>

                <button type="submit" class="btn-upload">Add Assignment</button>
            </form>



            <!-- Existing Assignments -->
            <h3>Existing Assignments</h3>
            <c:if test="${not empty assignments}">
                <ul>
                    <c:forEach items="${assignments}" var="assignment">
                        <li>
                            <h4>${assignment.title}</h4>
                            <a href="${pageContext.request.contextPath}/mentor/download-assignment/${assignment.id}" class="btn-download">Download Assignment</a>
                            <form action="${pageContext.request.contextPath}/mentor/edit-assignment/${assignment.id}" method="post" enctype="multipart/form-data" class="edit-form">
                                <label for="newAssignmentFile-${assignment.id}">Replace File:</label>
                                <input type="file" id="newAssignmentFile-${assignment.id}" name="assignmentFile" required />
                                <button type="submit" class="btn-edit">Update Assignment</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/mentor/delete-assignment/${assignment.id}" method="post" class="delete-form">
                                <button type="submit" class="btn-delete" 
                                    onclick="return confirm('Are you sure you want to delete this assignment?')">
                                    Delete Assignment
                                </button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${empty assignments}">
                <p>No assignments available yet.</p>
            </c:if>
        </div>

        <div id="grading" class="section">
            <h2>Grade Submissions</h2>
            <c:if test="${not empty submittedAssignments}">
                <ul>
                    <c:forEach items="${submittedAssignments}" var="assignment">
                        <li class="submission-item">
                            <h3 class="submission-title">${assignment.title}</h3>
                            <p class="submission-info">Submitted By: User ID: ${assignment.submittedByUserId}</p>

                            <div class="submission-actions">
                                <a href="${pageContext.request.contextPath}/mentor/download-assignment/${assignment.id}" 
                                   class="btn-download" 
                                   title="Download Assignment">
                                    <i class="fa fa-download"></i> Download Submission
                                </a>
                                
                                <form action="${pageContext.request.contextPath}/mentor/grade-assignment" 
                                      method="post" 
                                      class="grade-form">
                                    <input type="hidden" name="assignmentId" value="${assignment.id}" />
                                    <input type="hidden" name="courseId" value="${course.id}" />
                                    <label for="grade-${assignment.id}">Grade:</label>
                                    <input type="number" 
                                           id="grade-${assignment.id}" 
                                           name="grade" 
                                           min="0" 
                                           max="10" 
                                           required />
                                    <button type="submit" class="submit-grade-btn">Submit Grade</button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${empty submittedAssignments}">
                <p>No assignments submitted yet.</p>
            </c:if>
        </div>
    </div>

    <script>
        // Initial animations
        gsap.to(".page-title", {
            opacity: 1,
            y: 0,
            duration: 1,
            ease: "power3.out"
        });

        gsap.to(".tabs", {
            opacity: 1,
            duration: 1,
            delay: 0.3,
            ease: "power3.out"
        });

        gsap.to(".section", {
            opacity: 1,
            y: 0,
            duration: 1,
            delay: 0.5,
            ease: "power3.out"
        });

        // Tab switching functionality with animations
        function showSection(sectionId) {
            const sections = document.querySelectorAll('.section');
            const buttons = document.querySelectorAll('.tabs button');
            
            buttons.forEach(button => {
                if (button.textContent.toLowerCase().includes(sectionId)) {
                    button.classList.add('active');
                } else {
                    button.classList.remove('active');
                }
            });

            sections.forEach(section => {
                if (section.id === sectionId) {
                    section.style.display = 'block';
                    gsap.fromTo(section, 
                        { opacity: 0, y: 20 },
                        { opacity: 1, y: 0, duration: 0.5, ease: "power3.out" }
                    );
                } else {
                    section.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>
