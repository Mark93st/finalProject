# Student Management System

A comprehensive full-stack web application for managing students, courses, and enrollments with authentication and authorization.

## 🚀 Features

- **User Authentication & Authorization**
  - Admin and Student roles
  - Secure login/logout
  - Role-based access control

- **Student Management**
  - Create, view, update, delete students
  - Student profiles with personal information

- **Course Management** 
  - Create, view, update, delete courses
  - Course catalog with descriptions and credits

- **Enrollment System**
  - Students can enroll in courses
  - Students can drop courses
  - Grade assignment and tracking

- **User Interface**
  - Responsive Bootstrap design
  - Role-specific dashboards
  - Intuitive navigation

## 🛠️ Technologies Used

- **Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5, Bootstrap Icons
- **Database**: MySQL with Hibernate ORM
- **Authentication**: Spring Security with session management

## 📦 Installation & Setup

1. **Prerequisites**
   - Java 17 or higher
   - MySQL Server
   - Maven

2. **Database Setup**
   ```sql
   CREATE DATABASE student_management_db;

3. **Configuration**
    - spring.datasource.url=jdbc:mysql://localhost:3306/student_management_db
    - spring.datasource.username=your_username
    - spring.datasource.password=your_password

4. **Run the application**
    - mvn spring-boot:run

5. **Access the application**
    - Open: http://localhost:8080
    - First registered user becomes Admin
    - Subsequent users become Students

6. **Default roles**
    - Admin: Full access to all features (students, courses, enrollments)
    - Student: Can view their courses, grades, and enroll in new courses

7. **Usage**
    - Registration: Create a new account
    - Login: Sign in with your credentials
    - Admin Features: Manage students, courses, and view all enrollments
    - Student Features: View courses, enroll in courses, check grades

##Project Structure
src/
├── main/
│   ├── java/
│   │   └── com/example/studentmanagement/
│   │       ├── controller/     # MVC Controllers
│   │       ├── entity/         # JPA Entities
│   │       ├── repository/     # Data Access Layer
│   │       ├── service/        # Business Logic
│   │       └── config/         # Configuration
│   └── resources/
│       ├── templates/          # Thymeleaf templates
│       ├── static/             # Static resources
│       └── application.properties
