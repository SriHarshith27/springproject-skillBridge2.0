<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <title>Admin Dashboard</title>
    <!-- Chart.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="admin-navbar.jsp" />

    <div class="content">
        <h1>Admin Dashboard</h1>
        <p style="margin-top: 1rem; color: #666;">This is the admin analysis dashboard.</p>

        <!-- Section for User Stats Pie Chart -->
        <div class="chart-container">
            <h2>User Statistics</h2>
            <canvas id="userStatsChart" width="400" height="400"></canvas>
        </div>

        <!-- Section for Enrolled Students Table -->
        <div class="table-container">
            <h2>Students Enrolled Per Course</h2>
            <table id="enrollmentTable">
                <thead>
                    <tr>
                        <th>Course Name</th>
                        <th>Number of Students</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Data will be dynamically populated -->
                </tbody>
            </table>
        </div>
    </div>

    <script>
    
    
    
    async function logout() {
        try {
            const response = await fetch('<%= request.getContextPath() %>/auth/logout', {
                method: 'POST',
                credentials: 'include'
            });
            if (response.ok) {
                window.location.href = '<%= request.getContextPath() %>/auth/login'; // Updated URL for redirection
            } else {
                alert('Error during logout');
            }
        } catch (error) {
            console.error('Logout failed:', error);
        }
    }
    
        // Fetch Admin Analysis Data
        async function fetchAdminData() {
            try {
                // Fetch user stats for the pie chart
                const userStatsResponse = await fetch('<%= request.getContextPath() %>/analysis/admin/user-stats');
                const userStatsData = await userStatsResponse.json();

                // Populate Pie Chart
                const ctx = document.getElementById('userStatsChart').getContext('2d');
                const userStatsChart = new Chart(ctx, {
                    type: 'pie',
                    data: {
                        labels: ['Mentors', 'Users'],
                        datasets: [{
                            label: 'User Distribution',
                            data: [userStatsData.mentors, userStatsData.users],
                            backgroundColor: ['#3498db', '#2ecc71'], // Colors for chart
                        }]
                    }
                });

                // Fetch enrolled students data for the table
                const enrollmentResponse = await fetch('<%= request.getContextPath() %>/analysis/admin/enrolled-students');
                const enrollmentData = await enrollmentResponse.json();

                // Populate Enrollment Table
                const tableBody = document.querySelector('#enrollmentTable tbody');
                enrollmentData.forEach(row => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `<td>${row.courseName}</td><td>${row.studentCount}</td>`;
                    tableBody.appendChild(tr);
                });
            } catch (error) {
                console.error('Error fetching analysis data:', error);
            }
        }

        // Initialize Dashboard Data
        fetchAdminData();
    </script>
</body>
</html>
