<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Learnify - Empower Your Learning</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        /* Keyframe Animations */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes pulse {
            0% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.02);
            }
            100% {
                transform: scale(1);
            }
        }

        @keyframes gradientFlow {
            0% {
                background-position: 0% 50%;
            }
            50% {
                background-position: 100% 50%;
            }
            100% {
                background-position: 0% 50%;
            }
        }

        :root {
            --bg-primary: #0F1020;
            --bg-secondary: #1A1A2E;
            --text-primary: #E6E6E6;
            --text-secondary: #A0A0B0;
            --accent-color: #4E79A7;
            --accent-secondary: #6A4C93;
            --glass-bg: rgba(255, 255, 255, 0.05);
            --glass-border: rgba(255, 255, 255, 0.1);
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
            perspective: 1000px;
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
            position: fixed;
            width: 100%;
            top: 0;
            z-index: 1000;
            animation: fadeInUp 0.8s ease;
        }

        .logo h1 {
            font-size: 24px;
            font-weight: 700;
            color: var(--accent-color);
            transition: all 0.5s ease;
        }

        .logo h1:hover {
            transform: scale(1.1) rotate(3deg);
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
            position: relative;
            overflow: hidden;
        }

        .nav-links a::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(120deg, transparent, var(--accent-color), transparent);
            transition: all 0.5s ease;
        }

        .nav-links a:hover::before {
            left: 100%;
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
            padding: 100px 5% 50px;
            background: linear-gradient(135deg, var(--bg-primary), var(--bg-secondary));
            background-size: 200% 200%;
            animation: gradientFlow 15s ease infinite;
        }

        .hero-content {
            flex: 1;
            max-width: 50%;
            opacity: 0;
            animation: fadeInUp 1s ease forwards;
            animation-delay: 0.5s;
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
            transition: all 0.6s ease;
            transform: rotateY(-10deg);
        }

        .hero-image img:hover {
            transform: rotateY(0) scale(1.05);
            box-shadow: 0 15px 40px rgba(0,0,0,0.7);
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
            transition: all 0.3s ease;
            animation: pulse 2s infinite;
        }

        .stat:hover {
            transform: translateY(-10px) scale(1.05);
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
            will-change: transform;
        }

        .reason:hover {
            transform: scale(1.05);
        }

        .reason img {
            max-width: 100%;
            border-radius: 8px;
            margin-bottom: 15px;
            transition: transform 0.5s ease;
        }

        .reason:hover img {
            transform: rotate(5deg);
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
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            will-change: transform, box-shadow;
        }

        .course:hover {
            transform: scale(1.05);
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }

        .course img {
            max-width: 100%;
            border-radius: 8px;
            margin-bottom: 15px;
            transition: transform 0.5s ease;
        }

        .course:hover img {
            transform: scale(1.1);
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
            <img src="https://tinyurl.com/36aenna3" alt="Learning Illustration">
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
                <img src="https://tinyurl.com/5n7h6b9c" alt="Expert Mentors">
                <h3>Expert Mentors</h3>
                <p>Learn from industry-leading mentors with years of experience.</p>
            </div>
            <div class="reason">
                <img src="https://tinyurl.com/4bakxs3u" alt="Affordable Learning">
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
                <img src="https://tinyurl.com/yrzanvtw" alt="Python Programming">
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
        <p>&copy; 2024 Learnify.sriharshith27 & A Pavan & Tulasi Ram  All Rights Reserved.</p>
    </footer>
</body>
</html>