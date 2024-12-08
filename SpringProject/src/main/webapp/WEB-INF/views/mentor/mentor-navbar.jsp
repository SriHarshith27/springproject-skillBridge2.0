<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<div class="sidebar" id="sidebar">
    <div class="logo-container">
        <div class="logo">M</div>
        <span class="logo-text">Mentor Panel</span>
    </div>

    <div class="menu-section">
        <div class="menu-title">Main Menu</div>
        
        <a class="menu-item active" href="<%= request.getContextPath() %>/mentor/dashboard" data-tooltip="Dashboard">
            <span class="menu-icon">📊</span>
            <span class="menu-text">Dashboard</span>
        </a>
    </div>

    <div class="menu-section">
        <div class="menu-title">Mentor Settings</div>
        
        <a class="menu-item" href="<%= request.getContextPath() %>/profile/mentor" data-tooltip="Profile">
            <span class="menu-icon">🎭</span>
            <span class="menu-text">Profile</span>
        </a>
        
        <a class="menu-item" href="<%= request.getContextPath() %>/support/my-messages" data-tooltip="My Support">
		    <span class="menu-icon">📨</span>
		    <span class="menu-text">My Support</span>
        </a>
        
    </div>

    <div class="menu-section">
        <div class="menu-title">Course Settings</div>

        
        <a class="menu-item" href="<%= request.getContextPath() %>/mentor/create-course" data-tooltip="Create Course">
            <span class="menu-icon">📝</span>
            <span class="menu-text">Create Course</span>
        </a>
        
        
		<a class="menu-item" href="<%= request.getContextPath() %>/mentor/manage-courses" data-tooltip="Manage Courses">
		    <span class="menu-icon">📂</span>
		    <span class="menu-text">Manage Courses</span>
		</a>

        
    </div>

    <div class="menu-section">
        <div class="menu-title">Help/Support</div>
        
			<a class="menu-item" href="${pageContext.request.contextPath}/mentor/mentor-help-center" data-tooltip="Help Center">
			    <span class="menu-icon">❔</span>
			    <span class="menu-text">Help Center</span>
			</a>


        <a class="menu-item" href="<%= request.getContextPath() %>/support/form" data-tooltip="Support">
            <span class="menu-icon">📞</span>
            <span class="menu-text">Support</span>
        </a>
        
        <a class="menu-item" onclick="logout()" data-tooltip="Logout">
            <span class="menu-icon"><i class="fa-solid fa-right-from-bracket"></i></span>
            <span class="menu-text">Logout</span>
        </a>
    </div>
</div>

<button class="toggle-btn" id="toggleBtn">≡</button>

<script>
    const sidebar = document.getElementById('sidebar');
    const toggleBtn = document.getElementById('toggleBtn');

    toggleBtn.addEventListener('click', () => {
        sidebar.classList.toggle('collapsed');
        toggleBtn.classList.toggle('collapsed');
    });
</script>

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
