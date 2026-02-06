# Student Management System

A modern, full-stack web application designed for educational institutions to manage students, courses, and enrollments efficiently. Built with Spring Boot and enhanced with modern frontend technologies like Tailwind CSS and HTMX for a responsive and dynamic user experience.

## 🚀 Key Features

### 🛡️ Authentication & Security
- **Secure Access:** Role-based authentication (Admin & Student) using Spring Security.
- **Registration:** Public registration for new students.
- **Session Management:** Secure session handling for logged-in users.

### 👨‍💼 Admin Capabilities
- **Dashboard:** Centralized admin panel for system oversight.
- **Student Management:** View, search, and manage student profiles.
- **Course Management:** Create, update, and delete courses.
- **Enrollment Oversight:** Monitor course enrollments across the system.

### 🎓 Student Features
- **Course Catalog:** Browse available courses with detailed descriptions and credits.
- **Enrollment:** Self-service enrollment in open courses.
- **My Courses:** Personalized view of enrolled courses and grades.
- **Grade Tracking:** View academic performance and grades.

## 🛠️ Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.x** (Web, Security, Data JPA, Validation)
- **Database:** MySQL (Production) / H2 (Development/Testing)
- **Build Tool:** Maven

### Frontend
- **Thymeleaf:** Server-side templating engine.
- **Tailwind CSS:** Utility-first CSS framework for modern, responsive design.
- **HTMX:** High-power tools for HTML to enable dynamic interactions without complex JavaScript.
- **Bootstrap:** (Included for legacy/utility support).

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
- **Java Development Kit (JDK) 17** or higher
- **MySQL Server** (8.0 recommended)

> **Note:** Node.js and NPM are managed automatically by the Maven build process, so you don't need to install them manually.

## ⚙️ Installation & Setup

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/yourusername/student-management-application.git
    cd student-management-application
    ```

2.  **Configure Database**
    - Create a MySQL database named `student_management_db`.
    - Open `src/main/resources/application.properties` and update your MySQL credentials:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/student_management_db...
      spring.datasource.username=YOUR_USERNAME
      spring.datasource.password=YOUR_PASSWORD
      ```

3.  **Build and Run**
    The application uses a Maven wrapper and a frontend plugin to handle all dependencies (including Tailwind CSS generation).
    ```bash
    # On Windows
    .\mvnw.cmd spring-boot:run

    # On macOS/Linux
    ./mvnw spring-boot:run
    ```

4.  **Access the Application**
    - Open your browser and navigate to: [http://localhost:8080](http://localhost:8080)

## 📁 Project Structure

```text
src/main/
├── java/gr/aueb/finalProject/
│   ├── controller/   # Web controllers (Admin, Student, Auth, etc.)
│   ├── model/        # JPA Entities (Student, Course, Enrollment)
│   ├── repository/   # Data Access Layer
│   ├── service/      # Business Logic
│   └── security/     # Spring Security Configuration
└── resources/
    ├── static/       # Generated CSS, JS (HTMX)
    └── templates/    # Thymeleaf HTML views
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request