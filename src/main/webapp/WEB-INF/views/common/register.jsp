<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Form</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
    <div class="container">
        <h1 class="title">Create Account</h1>
        <form id="registrationForm">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" required placeholder="Enter your email">
                <div class="error-message" id="emailError"></div>
            </div>
            <div class="form-group">
                <label for="phone">Phone Number</label>
                <input type="tel" id="phone" name="phone" required placeholder="Enter your phone number" pattern="[0-9]{10}">
                <div class="error-message" id="phoneError"></div>
            </div>
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required placeholder="Choose a username" minlength="4">
                <div class="error-message" id="usernameError"></div>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required placeholder="Create a password">
                <div class="password-strength" id="passwordStrength"></div>
                <div class="password-strength-text" id="passwordStrengthText"></div>
                <div class="error-message" id="passwordError"></div>
            </div>
            <div class="form-group">
                <label for="role">Select Role</label>
                <select id="role" name="role" required>
                    <option value="">Choose a role</option>
                    <option value="USER">User</option>
                    <option value="MENTOR">Mentor</option>
                </select>
                <div class="error-message" id="roleError"></div>
            </div>
            <button type="submit">Register Now</button>
        </form>
    </div>

    <div class="success-message" id="successMessage">Registration Successful!</div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('registrationForm');
            const passwordInput = document.getElementById('password');
            const passwordStrength = document.getElementById('passwordStrength');
            const passwordStrengthText = document.getElementById('passwordStrengthText');
            const successMessage = document.getElementById('successMessage');

            // Password strength checker
            passwordInput.addEventListener('input', function() {
                const password = this.value;
                let strength = 0;
                let strengthText = '';

                if (password.length >= 8) strength += 1;
                if (/\d/.test(password)) strength += 1;
                if (/[!@#$%^&*]/.test(password)) strength += 1;
                if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength += 1;

                passwordStrength.className = 'password-strength';
                switch(strength) {
                    case 0:
                    case 1:
                        passwordStrength.classList.add('strength-weak');
                        strengthText = 'Weak';
                        break;
                    case 2:
                    case 3:
                        passwordStrength.classList.add('strength-medium');
                        strengthText = 'Medium';
                        break;
                    case 4:
                        passwordStrength.classList.add('strength-strong');
                        strengthText = 'Strong';
                        break;
                }
                
                passwordStrengthText.textContent = strengthText;
            });

            // Form validation and submission
            form.onsubmit = async function(e) {
                e.preventDefault();

                document.querySelectorAll('.error-message').forEach(error => {
                    error.textContent = '';
                    error.classList.remove('show');
                });

                let isValid = true;

                const email = document.getElementById('email').value;
                if (!email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
                    document.getElementById('emailError').textContent = 'Please enter a valid email address';
                    document.getElementById('emailError').classList.add('show');
                    isValid = false;
                }

                const phone = document.getElementById('phone').value;
                if (!phone.match(/^[0-9]{10}$/)) {
                    document.getElementById('phoneError').textContent = 'Please enter a valid 10-digit phone number';
                    document.getElementById('phoneError').classList.add('show');
                    isValid = false;
                }

                const password = passwordInput.value;
                if (password.length < 8 || 
                    !(/[a-z]/.test(password) && /[A-Z]/.test(password)) ||
                    !/\d/.test(password) ||
                    !/[!@#$%^&*]/.test(password)) {
                    document.getElementById('passwordError').textContent = 
                        'Password must be at least 8 characters long and contain uppercase, lowercase, numbers, and special characters';
                    document.getElementById('passwordError').classList.add('show');
                    isValid = false;
                }

                if (isValid) {
                    const formData = new FormData(form);
                    const data = {
                        username: formData.get('username'),
                        email: formData.get('email'),
                        password: formData.get('password'),
                        phone: formData.get('phone'),
                        role: formData.get('role')
                    };

                    try {
                        const response = await fetch('${pageContext.request.contextPath}/auth/register', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(data)
                        });

                        if (response.ok) {
                            successMessage.classList.add('show');
                            setTimeout(() => {
                                successMessage.classList.remove('show');
                                window.location.href = '${pageContext.request.contextPath}/auth/login';
                            }, 3000);
                        } else {
                            const errorText = await response.text();
                            alert('Registration failed: ' + errorText);
                        }
                    } catch (error) {
                        console.error('Error during registration:', error);
                    }
                }
            };
        });
    </script>
</body>
</html>
