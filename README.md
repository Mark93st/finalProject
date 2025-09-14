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
