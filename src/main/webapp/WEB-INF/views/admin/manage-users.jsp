<%@ page import="java.util.List" %>
<%@ page import="com.harshith.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>

<%
    String role = (String) session.getAttribute("role");
    if (role == null || !role.equals("ADMIN")) {
        response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        return;
    }

    List<User> users = (List<User>) request.getAttribute("users");
    if (users == null) {
        System.out.println("No users found or users attribute is not set.");
    } else {
        System.out.println("Number of users retrieved: " + users.size());
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/manage-users.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users</title>
</head>
<body>
    <!-- Include the sidebar -->
    <%@ include file="/WEB-INF/views/admin/admin-navbar.jsp" %>

    <div class="content-container">
        <h1>Manage Users</h1>
        <div id="users-table" class="user-table-container">
            <% if (users != null && !users.isEmpty()) { %>
                <table class="user-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (User user : users) { %>
                        <tr id="user-<%= user.getId() %>">
                            <td><%= user.getId() %></td>
                            <td><%= user.getUsername() %></td>
                            <td><%= user.getEmail() %></td>
                            <td><%= user.getRole() %></td>
                            <td>
                                <!-- View Button -->
                                <form method="get" action="<%= request.getContextPath() %>/admin/view-user" style="display:inline;">
                                    <input type="hidden" name="userId" value="<%= user.getId() %>">
                                    <button type="submit" class="view-btn">View</button>
                                </form>
                                
                                <!-- Delete Button with Custom Modal -->
                                <button type="button" class="delete-btn" onclick="showDeleteModal(<%= user.getId() %>)">Delete</button>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p>No users found.</p>
            <% } %>
        </div>
    </div>

    <!-- Custom Delete Confirmation Modal -->
    <div id="deleteModal" class="modal-overlay" style="display: none;">
        <div class="modal-content">
            <h3>Confirm Delete</h3>
            <p>Are you sure you want to delete this user?</p>
            <div class="modal-actions">
                <form id="deleteForm" method="post" action="<%= request.getContextPath() %>/admin/delete-user">
                    <input type="hidden" id="deleteUserId" name="userId">
                    <button type="submit" class="modal-btn confirm-btn">Yes</button>
                </form>
                <button onclick="hideDeleteModal()" class="modal-btn cancel-btn">Cancel</button>
            </div>
        </div>
    </div>

    <script>
        // Show Delete Confirmation Modal
        function showDeleteModal(userId) {
            document.getElementById('deleteUserId').value = userId;
            document.getElementById('deleteModal').style.display = 'flex';
        }

        // Hide Delete Confirmation Modal
        function hideDeleteModal() {
            document.getElementById('deleteModal').style.display = 'none';
        }

        // Custom Logout Function
        function logout() {
            fetch('<%= request.getContextPath() %>/auth/logout', {
                method: 'POST',
                credentials: 'include'
            }).then(response => {
                if (response.ok) {
                    window.location.href = '<%= request.getContextPath() %>/common/login.jsp';
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
