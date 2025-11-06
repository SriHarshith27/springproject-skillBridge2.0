# SkillBridge 2.0 - Learning Management System Backend

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Contributing](#contributing)

## 🎯 Overview

**SkillBridge 2.0** is a comprehensive Learning Management System (LMS) backend built with Spring Boot. It provides a robust REST API for managing online courses, user enrollments, assignments, and submissions. The platform supports role-based access control with three user types: Students (USER), Mentors (MENTOR), and Administrators (ADMIN).

This system is designed to facilitate online education by providing features for course management, content delivery, assignment submission, grading, and user progress tracking.

## ✨ Features

### 🔐 Authentication & Authorization
- **JWT-based Authentication**: Secure token-based authentication with access and refresh tokens
- **Role-Based Access Control (RBAC)**: Three user roles with different permission levels
- **Password Management**: Secure password encryption using BCrypt
- **User Registration & Login**: Complete authentication flow with validation

### 📚 Course Management
- **Course CRUD Operations**: Create, read, update, and delete courses (Admin/Mentor only)
- **Course Modules**: Organize course content into structured modules with videos and resources
- **Course Categories**: Filter and search courses by category
- **Pagination & Sorting**: Efficient data retrieval with customizable pagination
- **File Upload**: Support for multimedia content (videos, documents) via Cloudinary

### 👥 User Management
- **User Profiles**: Comprehensive user information management
- **Enrollment System**: Students can enroll in courses
- **My Courses**: Users can view their enrolled courses
- **Admin Dashboard**: Manage all users with pagination and sorting
- **Password Change**: Secure password update functionality

### 📝 Assignment & Assessment
- **Assignment Creation**: Mentors can create assignments for courses
- **Assignment Submission**: Students can submit assignments with file uploads
- **Grading System**: Mentors can grade and provide feedback on submissions
- **Submission Tracking**: View all submissions for a course with student details

### 🛡️ Security Features
- **JWT Token Security**: Stateless authentication with secure token generation
- **Password Encryption**: BCrypt hashing for password storage
- **Input Validation**: Comprehensive validation for all user inputs
- **CORS Configuration**: Configurable cross-origin resource sharing
- **Global Exception Handling**: Centralized error handling and user-friendly error messages

### 📊 Additional Features
- **Audit Logging**: Track important system events and user actions
- **Support Messaging**: Built-in support system for user queries
- **File Storage**: Cloudinary integration for media file management
- **Email Notifications**: Spring Mail integration for notifications
- **Redis Caching**: Performance optimization with Redis cache

## 🛠️ Technology Stack

### Backend Framework
- **Spring Boot 3.3.5**: Core framework for building the REST API
- **Spring Data JPA**: Database operations and ORM
- **Spring Security**: Authentication and authorization
- **Spring Validation**: Input validation framework
- **Spring Mail**: Email functionality

### Database & Caching
- **PostgreSQL**: Primary relational database (Supabase hosted)
- **Redis**: Caching layer for improved performance
- **Hibernate**: ORM implementation
- **HikariCP**: High-performance connection pooling

### Security & Authentication
- **JWT (JSON Web Tokens)**: Token-based authentication (v0.12.6)
- **BCrypt**: Password hashing algorithm
- **Spring Security**: Security framework

### File Storage & Cloud Services
- **Cloudinary**: Cloud-based media storage and CDN
- **Apache HTTP Components**: HTTP client for external API calls

### Development Tools
- **Lombok**: Reduce boilerplate code
- **Maven**: Dependency management and build automation
- **MapStruct 1.5.5**: DTO mapping framework

### Others
- **Netty**: Async I/O for network operations
- **Logback**: Logging framework
- **Jakarta Validation**: Bean validation

## 🏗️ Architecture

### Project Structure
```
springproject-skillBridge2.0/
├── src/
│   ├── main/
│   │   ├── java/com/harshith/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── ApplicationConfig.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CourseController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── AssignmentDto.java
│   │   │   │   ├── CourseDto.java
│   │   │   │   ├── CourseModuleDto.java
│   │   │   │   ├── JwtResponse.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   └── UserDto.java
│   │   │   ├── exception/           # Exception handlers
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── UserNotFoundException.java
│   │   │   ├── model/               # JPA Entities
│   │   │   │   ├── Assignment.java
│   │   │   │   ├── AuditLog.java
│   │   │   │   ├── Course.java
│   │   │   │   ├── CourseModule.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── SupportMessage.java
│   │   │   │   └── User.java
│   │   │   ├── repository/          # Data access layer
│   │   │   │   ├── AssignmentRepository.java
│   │   │   │   ├── AuditLogRepository.java
│   │   │   │   ├── CourseRepository.java
│   │   │   │   ├── CourseModuleRepository.java
│   │   │   │   ├── SupportMessageRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/             # Business logic
│   │   │   │   ├── AuditService.java
│   │   │   │   ├── CourseService.java
│   │   │   │   ├── CourseServiceImpl.java
│   │   │   │   ├── DtoMapperService.java
│   │   │   │   ├── FileStorageService.java
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── SupportMessageService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── UserServiceImpl.java
│   │   │   ├── validation/          # Custom validators
│   │   │   └── SpringProjectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                        # Unit and integration tests
├── pom.xml                          # Maven configuration
├── mvnw                             # Maven wrapper (Unix)
└── mvnw.cmd                         # Maven wrapper (Windows)
```

### Design Patterns
- **MVC Pattern**: Separation of concerns with Controller-Service-Repository layers
- **DTO Pattern**: Data transfer objects for API communication
- **Repository Pattern**: Data access abstraction
- **Dependency Injection**: Spring IoC container for loose coupling
- **Builder Pattern**: Lombok builders for object creation
- **Singleton Pattern**: Spring beans are singletons by default

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+** (or use the included Maven wrapper)
- **PostgreSQL** database (or access to Supabase)
- **Redis Server** (optional, for caching)
- **Git** (for version control)

### Optional Tools
- **Postman** or **Insomnia** for API testing
- **IntelliJ IDEA** or **Eclipse** (recommended IDEs)
- **Docker** (for containerized deployment)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/SriHarshith27/springproject-skillBridge2.0.git
cd springproject-skillBridge2.0
```

### 2. Configure Database
Update the `application.properties` file with your database credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://your-db-host:5432/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
```

### 3. Configure Cloudinary (for file uploads)
Add your Cloudinary credentials:

```properties
# Cloudinary Configuration
cloudinary.cloud_name=your-cloud-name
cloudinary.api_key=your-api-key
cloudinary.api_secret=your-api-secret
```

### 4. Configure JWT Secret
Generate a secure JWT secret key:

```properties
# JWT Configuration
jwt.secret=your-base64-encoded-secret-key
jwt.access-token-expiration=900000
jwt.refresh-token-expiration=604800000
```

### 5. Build the Project
Using Maven wrapper (recommended):
```bash
# Windows
mvnw.cmd clean install

# Unix/Linux/Mac
./mvnw clean install
```

Or using Maven directly:
```bash
mvn clean install
```

### 6. Run the Application
```bash
# Using Maven wrapper
mvnw.cmd spring-boot:run  # Windows
./mvnw spring-boot:run    # Unix/Linux/Mac

# Or using Maven
mvn spring-boot:run

# Or run the JAR file
java -jar target/SpringProject-0.0.1-SNAPSHOT.jar
```

### 7. Verify Installation
The application will start on port **8080** by default. You should see:
```
started Success
```

Access the API at: `http://localhost:8080/api/v1`

## 📖 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication Endpoints

#### Register a New User
```http
POST /auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "phone": "1234567890",
  "role": "USER"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "SecurePass123"
}

Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900000
}
```

#### Get Current User
```http
GET /auth/me
Authorization: Bearer {access_token}
```

### Course Endpoints

#### Get All Courses (Public)
```http
GET /courses?page=0&size=10&sortBy=id&sortDir=asc&category=programming
```

#### Get Course by ID (Public)
```http
GET /courses/{id}
```

#### Create Course (Admin/Mentor only)
```http
POST /courses
Authorization: Bearer {access_token}
Content-Type: multipart/form-data

{
  "name": "Spring Boot Masterclass",
  "description": "Complete Spring Boot course",
  "duration": 40,
  "category": "Programming",
  "thumbnail": <file>
}
```

#### Enroll in Course (Authenticated Users)
```http
POST /courses/{courseId}/enroll
Authorization: Bearer {access_token}
```

#### Delete Course (Admin/Mentor only)
```http
DELETE /courses/{courseId}
Authorization: Bearer {access_token}
```

### Module Endpoints

#### Add Module to Course (Admin/Mentor only)
```http
POST /courses/{courseId}/modules
Authorization: Bearer {access_token}
Content-Type: multipart/form-data

{
  "name": "Introduction to Spring Boot",
  "description": "Learn the basics",
  "videoFile": <file>
}
```

#### Delete Module (Admin/Mentor only)
```http
DELETE /courses/modules/{moduleId}
Authorization: Bearer {access_token}
```

### Assignment Endpoints

#### Create Assignment (Admin/Mentor only)
```http
POST /courses/{courseId}/assignments
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "title": "Build a REST API",
  "description": "Create a simple CRUD API",
  "dueDate": "2025-12-31T23:59:59"
}
```

#### Submit Assignment (Students)
```http
POST /courses/assignments/{assignmentId}/submit
Authorization: Bearer {access_token}
Content-Type: multipart/form-data

{
  "submissionFile": <file>
}
```

#### Grade Assignment (Mentor only)
```http
POST /courses/assignments/{assignmentId}/grade
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "userId": 1,
  "grade": 95,
  "feedback": "Excellent work!"
}
```

#### Get Course Submissions (Mentor only)
```http
GET /courses/{courseId}/submissions
Authorization: Bearer {access_token}
```

### User Endpoints

#### Get My Enrolled Courses
```http
GET /user/my-courses
Authorization: Bearer {access_token}
```

#### Change Password
```http
POST /user/change-password
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "currentPassword": "OldPass123",
  "newPassword": "NewPass456"
}
```

### Admin Endpoints

#### Get All Users (Admin only)
```http
GET /admin/users?page=0&size=10&sortBy=id&sortDir=asc
Authorization: Bearer {access_token}
```

#### Delete User (Admin only)
```http
DELETE /admin/users/{id}
Authorization: Bearer {access_token}
```

## 🔐 Security

### JWT Authentication Flow
1. User registers or logs in with credentials
2. Server validates credentials and generates JWT access token and refresh token
3. Client stores tokens securely (localStorage/sessionStorage)
4. Client includes access token in Authorization header for subsequent requests
5. Server validates token on each request using `JwtAuthenticationFilter`
6. If token expires, client can use refresh token to get new access token

### Role-Based Access Control

| Role | Permissions |
|------|------------|
| **USER** | - Enroll in courses<br>- View enrolled courses<br>- Submit assignments<br>- Change own password |
| **MENTOR** | - All USER permissions<br>- Create/update/delete courses<br>- Create modules and assignments<br>- Grade submissions<br>- View course submissions |
| **ADMIN** | - All MENTOR permissions<br>- View all users<br>- Delete users<br>- Full system access |

### Security Best Practices Implemented
- ✅ Password encryption using BCrypt
- ✅ JWT tokens with expiration
- ✅ HTTPS support (configure in production)
- ✅ Input validation and sanitization
- ✅ SQL injection prevention (JPA/Hibernate)
- ✅ CORS configuration
- ✅ Global exception handling
- ✅ Rate limiting ready (Redis support)

## 🗄️ Database Schema

### Main Entities

#### Users Table
```sql
- id (PK)
- username (unique)
- email (unique)
- password (encrypted)
- phone
- role (USER, MENTOR, ADMIN)
- profile_image_url
- bio
- created_at
- updated_at
```

#### Courses Table
```sql
- id (PK)
- name
- description
- duration
- category
- thumbnail_url
- instructor_id (FK -> Users)
- created_at
- updated_at
```

#### Course_Modules Table
```sql
- id (PK)
- course_id (FK -> Courses)
- name
- description
- video_url
- order_index
- created_at
```

#### Assignments Table
```sql
- id (PK)
- course_id (FK -> Courses)
- title
- description
- due_date
- created_at
```

#### User_Courses (Many-to-Many)
```sql
- user_id (FK -> Users)
- course_id (FK -> Courses)
- enrolled_at
```

### Relationships
- User → Courses (Many-to-Many): Students enrolled in courses
- Course → Modules (One-to-Many): Courses have multiple modules
- Course → Assignments (One-to-Many): Courses have multiple assignments
- User → Submissions (One-to-Many): Users submit assignments

## ⚙️ Configuration

### Application Properties Overview

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://host:port/database
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=none  # Use 'update' for development

# File Upload
spring.servlet.multipart.max-file-size=500MB
spring.servlet.multipart.max-request-size=500MB

# JWT
jwt.secret=your-secret-key
jwt.access-token-expiration=900000       # 15 minutes
jwt.refresh-token-expiration=604800000   # 7 days

# Cloudinary
cloudinary.cloud_name=your-cloud-name
cloudinary.api_key=your-api-key
cloudinary.api_secret=your-api-secret

# Logging
logging.level.com.harshith=INFO
logging.level.org.springframework.security=DEBUG
```

### Environment Variables (Recommended for Production)
Create a `.env` file or use system environment variables:
```bash
DB_URL=jdbc:postgresql://host:port/database
DB_USERNAME=username
DB_PASSWORD=password
JWT_SECRET=your-secret-key
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret
```

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=SpringProjectApplicationTests

# Run with coverage
mvn clean test jacoco:report
```

### Test Structure
- Unit tests for services and utilities
- Integration tests for controllers
- Repository tests with in-memory database

## 📦 Deployment

### Build for Production
```bash
mvn clean package -DskipTests
```

### Deploy to Server
1. Copy the JAR file to your server
2. Configure production `application.properties`
3. Run the application:
```bash
java -jar SpringProject-0.0.1-SNAPSHOT.jar
```

### Docker Deployment (Optional)
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t skillbridge-api .
docker run -p 8080:8080 skillbridge-api
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards
- Follow Java naming conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation as needed

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**SriHarshith27**
- GitHub: [@SriHarshith27](https://github.com/SriHarshith27)
- Repository: [springproject-skillBridge2.0](https://github.com/SriHarshith27/springproject-skillBridge2.0)

## 📧 Support

For support, email your queries or create an issue in the GitHub repository.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL for the reliable database
- Cloudinary for media storage solutions
- All contributors and supporters of this project

---

**Built with ❤️ using Spring Boot**
