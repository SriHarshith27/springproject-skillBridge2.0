<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Learnify - Empower Your Learning</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg-primary: #121212;
            --bg-secondary: #1E1E1E;
            --text-primary: #FFFFFF;
            --text-secondary: #B0B0B0;
            --accent-color: #4A90E2;
            --glass-bg: rgba(255, 255, 255, 0.1);
            --glass-border: rgba(255, 255, 255, 0.2);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            transition: all 0.3s ease;
        }

        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--bg-primary);
            color: var(--text-primary);
            line-height: 1.6;
        }

        /* Navbar Styling */
        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px 5%;
            background: var(--glass-bg);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid var(--glass-border);
        }

        .logo h1 {
            font-size: 24px;
            font-weight: 700;
            color: var(--accent-color);
        }

        .nav-links {
            display: flex;
            gap: 20px;
        }

        .nav-links a {
            text-decoration: none;
            color: var(--text-primary);
            padding: 10px 15px;
            border-radius: 8px;
            background: var(--glass-bg);
            backdrop-filter: blur(10px);
            border: 1px solid var(--glass-border);
            transition: all 0.3s ease;
        }

        .nav-links a:hover {
            transform: scale(1.05);
            background: var(--accent-color);
            color: var(--bg-primary);
        }

        /* Hero Section */
        .hero {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 50px 5%;
            background: linear-gradient(135deg, var(--bg-primary), var(--bg-secondary));
        }

        .hero-content {
            flex: 1;
            max-width: 50%;
        }

        .hero-content h1 {
            font-size: 48px;
            margin-bottom: 20px;
            color: var(--accent-color);
        }

        .hero-image {
            flex: 1;
            text-align: right;
        }

        .hero-image img {
            max-width: 80%;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
        }

        /* Stats Section */
        .stats {
            display: flex;
            justify-content: space-around;
            padding: 50px 5%;
            background: var(--bg-secondary);
        }

        .stat {
            text-align: center;
            background: var(--glass-bg);
            backdrop-filter: blur(10px);
            padding: 20px;
            border-radius: 12px;
            border: 1px solid var(--glass-border);
            transition: transform 0.3s ease;
        }

        .stat:hover {
            transform: translateY(-10px);
        }

        .stat h2 {
            font-size: 36px;
            color: var(--accent-color);
        }

        /* Why Us Section */
        .why-us {
            padding: 50px 5%;
            text-align: center;
        }

        .why-us h2 {
            margin-bottom: 40px;
            color: var(--accent-color);
        }

        .reasons {
            display: flex;
            justify-content: space-between;
            gap: 30px;
        }

        .reason {
            flex: 1;
            background: var(--glass-bg);
            backdrop-filter: blur(10px);
            border-radius: 12px;
            padding: 20px;
            border: 1px solid var(--glass-border);
            transition: transform 0.3s ease;
        }

        .reason:hover {
            transform: scale(1.05);
        }

        .reason img {
            max-width: 100%;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        /* Featured Courses */
        .featured-courses {
            padding: 50px 5%;
            background: var(--bg-secondary);
            text-align: center;
        }

        .featured-courses h2 {
            margin-bottom: 40px;
            color: var(--accent-color);
        }

        .courses {
            display: flex;
            justify-content: space-between;
            gap: 30px;
        }

        .course {
            flex: 1;
            background: var(--glass-bg);
            backdrop-filter: blur(10px);
            border-radius: 12px;
            padding: 20px;
            border: 1px solid var(--glass-border);
            transition: transform 0.3s ease;
        }

        .course:hover {
            transform: scale(1.05);
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }

        .course img {
            max-width: 100%;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        /* Footer */
        footer {
            text-align: center;
            padding: 20px;
            background: var(--glass-bg);
            backdrop-filter: blur(10px);
            border-top: 1px solid var(--glass-border);
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .hero, .reasons, .courses {
                flex-direction: column;
            }

            .hero-content, .hero-image {
                max-width: 100%;
                text-align: center;
            }

            .hero-image img {
                max-width: 100%;
            }
        }
    </style>
</head>
<body>
    <header>
        <div class="navbar">
            <div class="logo">
                <h1>Learnify</h1>
            </div>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/auth/login" class="btn-login">Login</a>
                <a href="${pageContext.request.contextPath}/auth/register" class="btn-signup">Sign Up</a>
            </div>
        </div>
    </header>

    <section class="hero">
        <div class="hero-content">
            <h1>Empower Your Learning Journey</h1>
            <p>Join our platform to learn from the best mentors and take your skills to the next level.</p>
        </div>
        <div class="hero-image">
            <img src="https://images.unsplash.com/photo-1516397281756-7a69c719ff09?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80" alt="Learning Illustration">
        </div>
    </section>

    <section class="stats">
        <div class="stat">
            <h2>500+</h2>
            <p>Courses</p>
        </div>
        <div class="stat">
            <h2>200+</h2>
            <p>Mentors</p>
        </div>
        <div class="stat">
            <h2>10,000+</h2>
            <p>Happy Learners</p>
        </div>
    </section>

    <section class="why-us">
        <h2>Why Choose Us?</h2>
        <div class="reasons">
            <div class="reason">
                <img src="https://images.unsplash.com/photo-1616216795485-1fa3d4dcdee5?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Expert Mentors">
                <h3>Expert Mentors</h3>
                <p>Learn from industry-leading mentors with years of experience.</p>
            </div>
            <div class="reason">
                <img src="https://images.unsplash.com/photo-1521790362395-9a556acc5b5b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Affordable Learning">
                <h3>Affordable Learning</h3>
                <p>Access premium content at the most competitive prices.</p>
            </div>
            <div class="reason">
                <img src="https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Certification">
                <h3>Certification</h3>
                <p>Earn certificates to showcase your skills and expertise.</p>
            </div>
        </div>
    </section>

    <section class="featured-courses">
        <h2>Our Popular Courses</h2>
        <div class="courses">
            <div class="course">
                <img src="https://images.unsplash.com/photo-1498050108023-c5249f4df085?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Spring Boot Development">
                <h3>Spring Boot Development</h3>
                <p>Master backend development with Spring Boot.</p>
            </div>
            <div class="course">
                <img src="https://images.unsplash.com/photo-1580130544977-624d0e30b923?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Python Programming">
                <h3>Python Programming</h3>
                <p>Learn Python from beginner to advanced level.</p>
            </div>
            <div class="course">
                <img src="https://images.unsplash.com/photo-1542744173-8e7e53415bb0?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Full Stack Web Development">
                <h3>Full Stack Web Development</h3>
                <p>Become a full-stack developer with our comprehensive course.</p>
            </div>
        </div>
    </section>

    <footer>
        <p>&copy; 2024 Learnify. All Rights Reserved.</p>
    </footer>
</body>
</html>