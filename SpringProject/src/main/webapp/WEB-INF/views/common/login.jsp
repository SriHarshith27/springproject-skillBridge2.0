<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
    <div class="container">
        <h1 class="title">Welcome Back</h1>
        <form id="loginForm">
            <div class="form-group">
                <input type="text" id="username" name="username" class="floating-input" required placeholder=" ">
                <label for="username" class="floating-label">Username</label>
                <div class="error-message" id="usernameError"></div>
            </div>
            <div class="form-group">
                <input type="password" id="password" name="password" class="floating-input" required placeholder=" ">
                <label for="password" class="floating-label">Password</label>
                <div class="error-message" id="passwordError"></div>
            </div>
            <button type="submit" class="login-button">Login</button>
            <div class="register-link">
                Don't have an account? <a href="${pageContext.request.contextPath}/auth/register">Register Now</a>
            </div>
	<div class="forgot-password-link">
	    <a href="${pageContext.request.contextPath}/auth/forgot-password">Forgot Password?</a>
	</div>

        </form>
    </div>

    <!-- Forgot Password Modal -->
    <div id="forgotPasswordModal" class="modal">
        <div class="modal-content">
            <span class="close" id="closeModal">&times;</span>
            <h2>Forgot Password</h2>
            <form id="forgotPasswordForm">
                <input type="text" id="forgotUsername" name="username" placeholder="Enter your username" required />
                <button type="submit">Send Password Reset Email</button>
                <div class="success-message" id="forgotSuccessMessage"></div>
                <div class="error-message" id="forgotErrorMessage"></div>
            </form>
        </div>
    </div>

    <div class="success-message" id="successMessage">
        Login Successful!
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('loginForm');
            const successMessage = document.getElementById('successMessage');

            const forgotPasswordModal = document.getElementById('forgotPasswordModal');
            const forgotPasswordForm = document.getElementById('forgotPasswordForm');
            const forgotSuccessMessage = document.getElementById('forgotSuccessMessage');
            const forgotErrorMessage = document.getElementById('forgotErrorMessage');
            const forgotPasswordLink = document.getElementById('forgotPasswordLink');
            const closeModal = document.getElementById('closeModal');

            form.onsubmit = async function(event) {
                event.preventDefault();

                // Reset error messages
                document.querySelectorAll('.error-message').forEach(error => {
                    error.textContent = '';
                    error.classList.remove('show');
                });

                const formData = new FormData(form);
                const params = new URLSearchParams();
                params.append('username', formData.get('username'));
                params.append('password', formData.get('password'));

                let isValid = true;
                const username = formData.get('username');
                const password = formData.get('password');

                if (username.length < 4) {
                    document.getElementById('usernameError').textContent = 'Username must be at least 4 characters';
                    document.getElementById('usernameError').classList.add('show');
                    isValid = false;
                }

                if (password.length < 6) {
                    document.getElementById('passwordError').textContent = 'Password must be at least 6 characters';
                    document.getElementById('passwordError').classList.add('show');
                    isValid = false;
                }

                if (isValid) {
                    try {
                        const response = await fetch('${pageContext.request.contextPath}/auth/login?' + params.toString(), {
                            method: 'POST',
                            credentials: 'include'
                        });

                        if (response.ok) {
                            const redirectUrl = await response.text();
                            successMessage.classList.add('show');
                            setTimeout(() => {
                                successMessage.classList.remove('show');
                                if (redirectUrl === '/admin/dashboard') {
                                    window.location.href = '${pageContext.request.contextPath}/admin/dashboard';
                                } else if (redirectUrl === '/user/dashboard') {
                                    window.location.href = '${pageContext.request.contextPath}/user/dashboard';
                                } else if (redirectUrl === '/mentor/dashboard') {
                                    window.location.href = '${pageContext.request.contextPath}/mentor/dashboard';
                                }
                            }, 1000);
                        } else {
                            alert('Invalid credentials!');
                        }
                    } catch (error) {
                        console.error('Login failed:', error);
                    }
                }
            };

            // Open the Forgot Password Modal
            forgotPasswordLink.addEventListener('click', function(event) {
                event.preventDefault();
                forgotPasswordModal.style.display = 'block';
            });

            // Close the Forgot Password Modal
            closeModal.addEventListener('click', function() {
                forgotPasswordModal.style.display = 'none';
            });

            // Submit Forgot Password Form
            forgotPasswordForm.onsubmit = async function(event) {
                event.preventDefault();
                console.log("Forgot Password form submitted"); // Debugging log

                const username = document.getElementById('forgotUsername').value;

                // Reset messages
                forgotSuccessMessage.textContent = '';
                forgotErrorMessage.textContent = '';

                try {
                    const response = await fetch('${pageContext.request.contextPath}/auth/forgot-password', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ username }),
                    });

                    if (response.ok) {
                        forgotSuccessMessage.textContent = 'Password reset email sent successfully!';
                    } else {
                        forgotErrorMessage.textContent = 'Failed to send reset email. Please try again.';
                    }
                } catch (error) {
                    forgotErrorMessage.textContent = 'An error occurred. Please try again later.';
                    console.error('Forgot password failed:', error);
                }
            };

            // Close modal when clicking outside of it
            window.onclick = function(event) {
                if (event.target === forgotPasswordModal) {
                    forgotPasswordModal.style.display = 'none';
                }
            };
        });
    </script>
</body>
</html>
